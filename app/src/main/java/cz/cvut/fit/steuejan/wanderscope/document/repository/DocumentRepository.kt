package cz.cvut.fit.steuejan.wanderscope.document.repository

import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.util.performCall
import cz.cvut.fit.steuejan.wanderscope.app.util.toUnitIfSuccess
import cz.cvut.fit.steuejan.wanderscope.document.api.DocumentApi
import cz.cvut.fit.steuejan.wanderscope.document.api.request.DocumentMetadataRequest
import cz.cvut.fit.steuejan.wanderscope.points.common.TripPointType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import okhttp3.MultipartBody

class DocumentRepository(private val documentApi: DocumentApi) {

    @OptIn(FlowPreview::class)
    suspend fun uploadDocument(
        tripId: Int,
        request: DocumentMetadataRequest,
        file: MultipartBody.Part
    ): Flow<Result<*>> = performCall {
        documentApi.postTripDocumentMetadata(tripId, request)
    }.flatMapConcat {
        if (it is Result.Success) {
            performCall {
                documentApi.postTripDocument(tripId, it.data.id, file)
            }.toUnitIfSuccess()
        } else {
            flowOf(it)
        }
    }

    @OptIn(FlowPreview::class)
    suspend fun uploadDocument(
        tripId: Int,
        pointId: Int,
        pointType: TripPointType,
        request: DocumentMetadataRequest,
        file: MultipartBody.Part
    ): Flow<Result<*>> = performCall {
        documentApi.postPointDocumentMetadata(tripId, pointType.toString(), pointId, request)
    }.flatMapConcat {
        if (it is Result.Success) {
            performCall {
                documentApi.postPointDocument(tripId, pointType.toString(), pointId, it.data.id, file)
            }.toUnitIfSuccess()
        } else {
            flowOf(it)
        }
    }

}