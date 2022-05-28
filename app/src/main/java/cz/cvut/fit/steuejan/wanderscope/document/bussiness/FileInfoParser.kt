package cz.cvut.fit.steuejan.wanderscope.document.bussiness

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import org.apache.commons.io.FilenameUtils

class FileInfoParser(private val context: Context) {

    suspend fun parseFile(file: Uri): FileInfo {
        return withDefault {
            var displayName: String? = null
            var extension: String? = null
            var size: Int? = null
            val cursor = context.contentResolver.querySingle(file)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIdx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIdx > 0) {
                        displayName = it.getString(columnIdx)
                        extension = FilenameUtils.getExtension(displayName)
                    }
                    val sizeIdx = it.getColumnIndex(OpenableColumns.SIZE)
                    size = if (!it.isNull(sizeIdx)) it.getString(sizeIdx).toIntOrNull() else null
                }
            }
            FileInfo(displayName, extension, size)
        }
    }

    data class FileInfo(
        val filename: String?,
        val extension: String?,
        val sizeInBytes: Int?
    )

    private fun ContentResolver.querySingle(uri: Uri) =
        this.query(uri, null, null, null, null)
}