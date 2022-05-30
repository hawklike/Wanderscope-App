package cz.cvut.fit.steuejan.wanderscope.app.bussiness

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import cz.cvut.fit.steuejan.wanderscope.BuildConfig
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrLogException
import cz.cvut.fit.steuejan.wanderscope.app.util.runOrNull
import cz.cvut.fit.steuejan.wanderscope.document.model.DownloadedFile
import java.io.File

class FileManager(private val context: Context) {

    suspend fun saveDataToFile(data: DownloadedFile): Boolean {
        return withIO {
            runOrLogException {
                context.openFileOutput(data.filename, Context.MODE_PRIVATE).use {
                    it.write(data.data.readByteArray())
                }
                true
            } ?: false
        }
    }

    fun openFile(filename: String, type: DocumentType? = null): Boolean {
        val fileUri = runOrNull {
            val file = File(context.filesDir, filename)
            if (!file.exists()) {
                return false
            }
            FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}$PROVIDER", file)
        } ?: return false

        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(fileUri, resolveType(fileUri, type))
        }

        return runOrNull {
            context.startActivity(intent)
            true
        } ?: false
    }

    suspend fun deleteFile(filename: String): Boolean {
        return withIO {
            runOrLogException {
                val file = File(context.filesDir, filename)
                DocumentsContract.deleteDocument(context.contentResolver, file.toUri())
            } ?: false
        }
    }

    private fun resolveType(fileUri: Uri, type: DocumentType?): String {
        val contentType = context.contentResolver.getType(fileUri)
        return if (contentType == UNSPECIFIED_TYPE) {
            when (type) {
                DocumentType.TEXT -> TEXT
                DocumentType.IMAGE -> IMAGE
                DocumentType.DOCUMENT -> PDF
                DocumentType.GPX -> ALL
                null -> ALL
            }
        } else contentType ?: ALL
    }

    companion object {
        private const val PROVIDER = ".fileprovider"
        private const val UNSPECIFIED_TYPE = "application/octet-stream"
        private const val ALL = "*/*"
        private const val PDF = "application/pdf"
        private const val IMAGE = "image/*"
        private const val TEXT = "text/*"
    }

}