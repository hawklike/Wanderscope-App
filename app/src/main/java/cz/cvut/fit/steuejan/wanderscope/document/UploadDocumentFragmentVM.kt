package cz.cvut.fit.steuejan.wanderscope.document

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.extension.*
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.document.api.request.DocumentMetadataRequest
import cz.cvut.fit.steuejan.wanderscope.document.bussiness.FileParser
import cz.cvut.fit.steuejan.wanderscope.document.repository.DocumentRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber

class UploadDocumentFragmentVM(
    private val fileParser: FileParser,
    private val documentRepository: DocumentRepository
) : BaseViewModel() {

    val filename = MutableLiveData<String>()
    val filenameVisibility = MutableLiveData(false)
    val key = MutableLiveData<String?>(null)

    val validateFilename = filename.switchMapSuspend {
        customError ?: validator.validateIfNotEmpty(it)
    }

    val validateKey = key.switchMapSuspend {
        it ?: return@switchMapSuspend OK
        validator.validateDocumentKey(it)
    }

    val enableSubmit = ValidationMediator(
        validateFilename,
        validateKey
    )

    val loading = LoadingMutableLiveData()

    val requestIsSuccess = AnySingleLiveEvent()

    private var customError: Int? = null
    private var fileInfo: FileParser.FileInfo? = null
    private var tripId: Int? = null
    private var pointId: Int? = null
    private var pointType: TripPointType? = null

    fun init(tripId: Int, pointId: Int?, pointType: TripPointType?) {
        this.tripId = tripId
        this.pointId = pointId
        this.pointType = pointType
    }

    fun analyzePickedFile(file: Uri) {
        customError = null
        viewModelScope.launch {
            filenameVisibility.value = true
            fileInfo = fileParser.parseFile(file)
            validateSize(fileInfo?.sizeInBytes)
            validateExtension(fileInfo?.extension)
            fileInfo?.filename?.let { filename.value = it }
        }
    }

    private fun validateExtension(extension: String?) {
        val validation = validator.validateExtension(extension)
        if (validation != OK) {
            customError = validation
            validateFilename.value = validation
        }
    }

    private fun validateSize(size: Int?) {
        val validation = validator.validateDocumentSize(size)
        if (validation != OK) {
            customError = validation
            validateFilename.value = validation
        }
    }

    fun uploadFile() {
        loading.value = true
        viewModelScope.launchIO {
            val uri = fileInfo?.uri ?: run {
                loading.postValue(false)
                return@launchIO
            }

            val filename = filename.value ?: run {
                loading.postValue(false)
                return@launchIO
            }

            val realFilename = fileInfo?.filename ?: run {
                loading.postValue(false)
                return@launchIO
            }

            val metadataRequest = DocumentMetadataRequest(
                filename,
                DocumentType.fromExtension(fileInfo?.extension),
                key.value.getOrNullIfBlank()
            )

            val fileInputStream = fileParser.getInputStream(uri)
            val fileBytes = fileInputStream?.use { it.readBytes() } ?: run {
                loading.postValue(false)
                return@launchIO
            }

            //last check of size
            if (fileBytes.size > Constants.DOCUMENT_MAX_SIZE) {
                loading.postValue(false)
                validateFilename.postValue(R.string.upload_document_too_large)
                return@launchIO
            }

            val filePart = MultipartBody.Part.createFormData(
                "file",
                realFilename,
                RequestBody.create(MediaType.parse("multipart/form-data"), fileBytes)
            )

            val tripId = this@UploadDocumentFragmentVM.tripId ?: run {
                loading.postValue(false)
                return@launchIO
            }

            multipleLet(pointId, pointType) { pointId, pointType ->
                uploadFile(tripId, pointId, pointType, metadataRequest, filePart)
            } ?: uploadFile(tripId, metadataRequest, filePart)
        }
    }

    private suspend fun uploadFile(tripId: Int, request: DocumentMetadataRequest, file: MultipartBody.Part) {
        withIO {
            documentRepository.uploadDocument(tripId, request, file)
                .safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> uploadFileFailure(it.error)
                        is Result.Loading -> doNothing
                        is Result.Success -> uploadFileSuccess()
                    }
                }
        }
    }

    private suspend fun uploadFile(
        tripId: Int,
        pointId: Int,
        pointType: TripPointType,
        request: DocumentMetadataRequest,
        file: MultipartBody.Part
    ) {
        withIO {
            documentRepository.uploadDocument(tripId, pointId, pointType, request, file)
                .safeCollect(this) {
                    when (it) {
                        is Result.Cache -> TODO()
                        is Result.Failure -> uploadFileFailure(it.error)
                        is Result.Loading -> doNothing
                        is Result.Success -> uploadFileSuccess()
                    }
                }
        }
    }

    private fun uploadFileSuccess() {
        loading.value = false
        requestIsSuccess.publish()
    }

    private fun uploadFileFailure(error: Error) {
        loading.value = false
        error.reason?.let { Timber.e(it.message) }
        unexpectedError()
    }
}