package cz.cvut.fit.steuejan.wanderscope.document.bussiness

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import org.apache.commons.io.FilenameUtils
import java.io.InputStream

class FileParser(private val context: Context) {

    private val contentResolver by lazy {
        context.contentResolver
    }

    suspend fun parseFile(file: Uri): FileInfo {
        return withDefault {
            var displayName: String? = null
            var extension: String? = null
            var size: Int? = null
            val cursor = contentResolver.querySingle(file)
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
            FileInfo(file, displayName, extension, size)
        }
    }

    suspend fun getInputStream(uri: Uri): InputStream? {
        return withIO {
            @Suppress("BlockingMethodInNonBlockingContext")
            contentResolver.openInputStream(uri)
        }
    }

    data class FileInfo(
        val uri: Uri,
        val filename: String?,
        val extension: String?,
        val sizeInBytes: Int?
    )

    private fun ContentResolver.querySingle(uri: Uri) =
        this.query(uri, null, null, null, null)
}