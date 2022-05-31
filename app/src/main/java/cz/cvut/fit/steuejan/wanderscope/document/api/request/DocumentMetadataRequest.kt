package cz.cvut.fit.steuejan.wanderscope.document.api.request

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType

data class DocumentMetadataRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "type")
    val type: DocumentType,
    @Json(name = "key")
    val key: String?
)
