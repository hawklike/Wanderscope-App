package cz.cvut.fit.steuejan.wanderscope.document.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.document.api.request.DocumentMetadataRequest
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import okhttp3.MultipartBody
import timber.log.Timber

class DocumentRepository(private val documentApi: DocumentApi) {

    @OptIn(FlowPreview::class)
    suspend fun uploadDocument(
        tripId: Int,
        request: DocumentMetadataRequest,
        file: MultipartBody.Part
    ): Flow<Result<*>> = performCall {
        documentApi.postTripDocumentMetadata(tripId, request)
    }.flatMapConcat {
        Timber.d("here1")
        if (it is Result.Success) {
            Timber.d("here2")
            performCall {
                documentApi.postTripDocument(tripId, it.data.id, file)
            }.toUnitIfSuccess()
        } else {
            flowOf(it)
        }
    }

}