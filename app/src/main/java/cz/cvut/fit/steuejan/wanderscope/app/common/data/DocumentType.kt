package cz.cvut.fit.steuejan.wanderscope.app.common.data

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.WithIcon

enum class DocumentType : WithIcon {
    TEXT, IMAGE, DOCUMENT, GPX;

    override fun toIcon(): Int {
        return when (this) {
            TEXT -> R.drawable.ic_file_text
            IMAGE -> R.drawable.ic_file_image
            DOCUMENT -> R.drawable.ic_file_pdf
            GPX -> R.drawable.ic_file_other
        }
    }

    companion object {
        fun fromExtension(extension: String?): DocumentType {
            return when (extension?.lowercase()) {
                "pdf" -> DOCUMENT
                "jpg", "jpeg", "png", "gif" -> IMAGE
                "txt" -> TEXT
                else -> GPX
            }
        }
    }
}