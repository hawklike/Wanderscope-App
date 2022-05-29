package cz.cvut.fit.steuejan.wanderscope.document.api.response

import com.squareup.moshi.Json

data class DocumentsMetadataResponse(
    @Json(name = "documents")
    val documents: List<DocumentMetadataResponse>
)
