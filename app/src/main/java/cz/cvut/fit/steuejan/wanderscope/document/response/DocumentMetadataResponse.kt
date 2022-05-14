package cz.cvut.fit.steuejan.wanderscope.document.response

import com.squareup.moshi.Json
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.extension.toNiceString
import cz.cvut.fit.steuejan.wanderscope.document.DocumentMetadataItem
import org.joda.time.DateTime

data class DocumentMetadataResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "ownerId")
    val ownerId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "type")
    val type: DocumentType,
    @Json(name = "updated")
    val updated: DateTime,
    @Json(name = "hasKey")
    val hasKey: Boolean
) {
    suspend fun toOverviewItem() = DocumentMetadataItem(
        id,
        ownerId,
        name,
        updated.toNiceString(),
        hasKey,
        type.toIcon()
    )
}
