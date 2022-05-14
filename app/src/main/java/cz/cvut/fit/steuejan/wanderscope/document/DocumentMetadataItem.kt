package cz.cvut.fit.steuejan.wanderscope.document

import androidx.annotation.DrawableRes
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem

data class DocumentMetadataItem(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val updated: String,
    val hasKey: Boolean,
    @DrawableRes val icon: Int
) : RecyclerItem {

    override val layoutId = R.layout.item_document_metadata

    override fun isSameItem(other: RecyclerItem): Boolean {
        return other is DocumentMetadataItem && id == other.id
    }

    override fun hasSameContent(other: RecyclerItem): Boolean {
        return other is DocumentMetadataItem &&
                name == other.name &&
                updated == other.updated &&
                hasKey == other.hasKey &&
                icon == other.icon
    }
}
