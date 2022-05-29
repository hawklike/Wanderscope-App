package cz.cvut.fit.steuejan.wanderscope.app.bussiness

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import cz.cvut.fit.steuejan.wanderscope.BuildConfig
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

    fun openFile(filename: String): Boolean {
        val fileUri = runOrNull {
            val file = File(context.filesDir, filename)
            if (!file.exists()) {
                return false
            }
            FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}$PROVIDER", file)
        } ?: return false

        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(fileUri, context.contentResolver.getType(fileUri))
        }

        return runOrNull {
            context.startActivity(intent)
            true
        } ?: false
    }

    companion object {
        private const val PROVIDER = ".fileprovider"
    }

}