package cz.cvut.fit.steuejan.wanderscope.points.common.overview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.AlertDialogInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.WithRecycler
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmFragment
import cz.cvut.fit.steuejan.wanderscope.app.binding.visibleOrGone
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.FileManager
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.common.data.UserRole
import cz.cvut.fit.steuejan.wanderscope.app.common.map.WithMap
import cz.cvut.fit.steuejan.wanderscope.app.extension.addMarker
import cz.cvut.fit.steuejan.wanderscope.app.extension.adjustZoom
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.document.DocumentMetadataItem
import cz.cvut.fit.steuejan.wanderscope.document.model.DownloadedFile
import cz.cvut.fit.steuejan.wanderscope.points.common.api.response.PointResponse
import cz.cvut.fit.steuejan.wanderscope.points.common.overview.bundle.PointOverviewBundle
import kotlin.reflect.KClass

abstract class AbstractPointOverviewFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>
) : MvvmFragment<B, VM>(
    layoutId,
    viewModelClass
), WithLoading, WithRecycler, WithMap {

    override val hasBottomNavigation = false
    override val hasTitle = false

    protected abstract val pointOverview: PointOverviewBundle

    abstract override val map: MapView

    protected abstract val menuEditItem: Int
    protected abstract val menuDeleteItem: Int

    protected abstract val addDocumentButton: MaterialTextView

    protected abstract val documentsRecycler: RecyclerView

    abstract fun setFragmentResult()

    private val abstractViewModel by lazy {
        viewModel as? AbstractPointOverviewFragmentVM<*>
    }

    protected abstract fun setTitle(title: String)

    protected abstract fun handleResponse(response: PointResponse)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(pointOverview.title)
        handleActionButtons()
        handleDocumentsRecycler()
        retrievePointOverview()
        waitUntilMapAndCoordinatesAreReady()
        goToWebsiteIntent()
        launchMapIntent()
        handleLoadingDelete()
        handleSuccessDelete()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, R.id.point_overview_edit, Menu.FIRST, menuEditItem)
        menu.add(Menu.NONE, R.id.point_overview_delete, Menu.FIRST + 1, menuDeleteItem)
        menu.add(Menu.NONE, R.id.point_overview_add_to_calendar, Menu.FIRST + 2, R.string.save_to_calendar)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        when (pointOverview.userRole) {
            UserRole.VIEWER -> {
                menu.findItem(R.id.point_overview_edit)?.isVisible = false
                menu.findItem(R.id.point_overview_delete)?.isVisible = false
            }
            else -> doNothing
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.point_overview_edit -> editPoint()
            R.id.point_overview_delete -> deletePoint()
            R.id.point_overview_add_to_calendar -> saveToCalendar()
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun editPoint(): Boolean
    abstract fun deletePoint(): Boolean
    abstract fun saveToCalendar(): Boolean

    private fun handleActionButtons() {
        addDocumentButton.visibleOrGone(pointOverview.userRole.canEdit())
    }

    private fun handleDocumentsRecycler() {
        setAdapterListener(
            documentsRecycler,
            onLongClickListener = ::deleteDocument
        ) { item, _ ->
            if (item is DocumentMetadataItem) {
                val filename = DownloadedFile.getDocumentName(item.id, item.name)
                if (!FileManager(requireContext()).openFile(filename, item.type)) {
                    if (item.hasKey) {
                        showDialogWithKeyInput(item.id, item.name, item.type)
                    } else {
                        showDialogBeforeDownload(item.id, item.name, item.type)
                    }
                }
            }
        }
        listenToDocumentSuccessRequest()
    }

    private fun listenToDocumentSuccessRequest() {
        abstractViewModel?.documentSuccessRequest?.safeObserve {
            documentsRecycler.smoothScrollToPosition(0)
        }
    }

    @SuppressLint("InflateParams")
    private fun showDialogWithKeyInput(documentId: Int, filename: String, type: DocumentType) {
        val customView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_dialog_with_password, null, false)
        val keyInput = customView.findViewById<TextInputLayout>(R.id.dialogDocumentKey)

        showAlertDialog(
            customView,
            AlertDialogInfo(
                R.string.download_document_title,
                R.string.download_document_with_key_message,
                positiveButton = R.string.download
            ) { dialog, _ ->
                val key = keyInput.editText?.text?.toString()
                dialog.dismiss()
                abstractViewModel?.downloadDocument(documentId, filename, type, key)
                    ?: showToast(R.string.unexpected_error_short)
            })
    }

    @Suppress("UNUSED_PARAMETER")
    private fun deleteDocument(item: RecyclerItem, position: Int) {
        if (item is DocumentMetadataItem) {
            showAlertDialog(
                AlertDialogInfo(
                    R.string.delete_document_title,
                    R.string.delete_document_message,
                    positiveButton = R.string.delete
                ) { _, _ ->
                    abstractViewModel?.deleteDocument(item.id, item.ownerId, item.name, pointOverview.userRole)
                        ?: showToast(R.string.unexpected_error_short)
                })
        }
    }

    private fun showDialogBeforeDownload(documentId: Int, filename: String, type: DocumentType) {
        showAlertDialog(
            AlertDialogInfo(
                R.string.download_document_title,
                R.string.download_document_message,
                positiveButton = R.string.download
            ) { _, _ ->
                abstractViewModel?.downloadDocument(documentId, filename, type)
                    ?: showToast(R.string.unexpected_error_short)
            }
        )
    }

    override fun openFile(file: DownloadedFile, fileManager: FileManager, type: DocumentType?): Boolean {
        abstractViewModel?.documentActionLoading?.postValue(false)
        return super.openFile(file, fileManager, type)
    }

    override fun savingFileFailed() {
        super.savingFileFailed()
        abstractViewModel?.documentActionLoading?.postValue(false)
    }

    private var deleteSnackbar: Snackbar? = null

    private fun handleLoadingDelete() {
        abstractViewModel?.deleteLoading?.safeObserve { title ->
            deleteSnackbar = showSnackbar(
                SnackbarInfo(
                    title,
                    length = Snackbar.LENGTH_INDEFINITE
                )
            )
        }
    }

    private fun handleSuccessDelete() {
        abstractViewModel?.deleteIsSuccess?.safeObserve {
            deleteSnackbar?.dismiss()
            updateTripPoint()
            updateDocument()
            setFragmentResult()
            navigateBack()
        }
    }

    protected fun pleaseWait(): Boolean {
        showToast(R.string.please_wait)
        return true
    }

    private fun retrievePointOverview() {
        showLoading()
        abstractViewModel?.getPoint(pointOverview.tripId, pointOverview.pointId)

        abstractViewModel?.loading?.safeObserve { loading ->
            if (!loading) {
                hideLoading()
            }
        }

        abstractViewModel?.pointOverview?.safeObserve {
            setTitle(it.name)
            handleResponse(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        abstractViewModel?.mapReady?.value = googleMap
    }

    protected open fun waitUntilMapAndCoordinatesAreReady() {
        abstractViewModel?.mapAndCoordinatesReady?.safeObserve { (googleMap, location) ->
            val marker = googleMap?.addMarker(location?.coordinates, location?.title)
            googleMap?.adjustZoom(map, marker)
        }
    }

    private fun goToWebsiteIntent() {
        abstractViewModel?.goToWebsite?.safeObserve(::startActivitySafe)
    }

    private fun launchMapIntent() {
        abstractViewModel?.launchMap?.safeObserve(::startActivitySafe)
    }
}