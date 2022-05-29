package cz.cvut.fit.steuejan.wanderscope.document.model

import okio.BufferedSource

data class DownloadedFile(
    val data: BufferedSource,
    val filename: String
)