package cz.cvut.fit.steuejan.wanderscope.document

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.document.bussiness.FileInfoParser
import kotlinx.coroutines.launch

class UploadDocumentFragmentVM(
    private val fileInfoParser: FileInfoParser
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

    private var customError: Int? = null

    fun analyzePickedFile(file: Uri) {
        customError = null
        viewModelScope.launch {
            filenameVisibility.value = true
            val info = fileInfoParser.parseFile(file)
            validateSize(info.sizeInBytes)
            validateExtension(info.extension)
            info.filename?.let { filename.value = it }
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
}