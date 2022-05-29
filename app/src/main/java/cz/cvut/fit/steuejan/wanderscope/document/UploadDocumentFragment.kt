package cz.cvut.fit.steuejan.wanderscope.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentDocumentUploadBinding

class UploadDocumentFragment : MvvmFragment<
        FragmentDocumentUploadBinding,
        UploadDocumentFragmentVM>(
    R.layout.fragment_document_upload,
    UploadDocumentFragmentVM::class
) {
    override val hasBottomNavigation = false
    override val hasTitle = false

    private val args: UploadDocumentFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(args.tripId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseFile()
    }

    @Suppress("DEPRECATION")
    private fun chooseFile() {
        binding.uploadFileChooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
            }
            startActivityForResult(intent, PICK_FILE_REQUEST)
        }
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let(viewModel::analyzePickedFile)
        }
    }

    companion object {
        const val PICK_FILE_REQUEST = 1212
    }
}