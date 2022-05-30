package cz.cvut.fit.steuejan.wanderscope.document.model

import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import okio.BufferedSource

data class DownloadedFile(
    val data: BufferedSource,
    val filename: String,
    val type: DocumentType? = null
)