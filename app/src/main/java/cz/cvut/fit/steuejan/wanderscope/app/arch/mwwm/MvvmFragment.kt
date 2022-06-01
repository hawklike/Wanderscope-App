package cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import cz.cvut.fit.steuejan.wanderscope.BR
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.SnackbarInfo
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.FileManager
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
import cz.cvut.fit.steuejan.wanderscope.app.common.data.DocumentType
import cz.cvut.fit.steuejan.wanderscope.app.common.map.WithMap
import cz.cvut.fit.steuejan.wanderscope.app.extension.withIO
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent
import cz.cvut.fit.steuejan.wanderscope.document.model.DownloadedFile
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class MvvmFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes val layoutId: Int,
    viewModelClass: KClass<VM>
) : BaseFragment() {

    protected lateinit var binding: B private set
    open val viewModel: VM by lazy { getViewModel(clazz = viewModelClass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate<B>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.vm, viewModel)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToNavigate()
        listenToToast()
        listenToSnackbar()
        listenToLoading()
        listenToDatePicker()
        listenToTimePicker()
        listenToAlertDialog()
        listenToHideKeyboard()
        saveAndOpenFile()
        removeFile()
        prepareMap(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this is WithMap) {
            val mapBundle = outState.getBundle(MAP_BUNDLE_KEY) ?: run {
                val bundle = Bundle()
                outState.putBundle(MAP_BUNDLE_KEY, bundle)
                bundle
            }
            map.onSaveInstanceState(mapBundle)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this is WithMap) {
            map.onResume()
        }
    }

    override fun onStart() {
        super.onStart()
        if (this is WithMap) {
            map.onStart()
        }
    }

    override fun onStop() {
        super.onStop()
        if (this is WithMap) {
            map.onStop()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this is WithMap) {
            map.onPause()
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        if (this is WithMap) {
            map.onDestroy()
        }
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (this is WithMap) {
            map.onLowMemory()
        }
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        if (this is WithMap) {
            val mapViewBundle = savedInstanceState?.getBundle(MAP_BUNDLE_KEY)
            map.onCreate(mapViewBundle)
            map.getMapAsync(::onMapReady)
        }
    }

    protected fun updateTrip(update: Boolean = true, onBackground: Boolean = false) {
        if (onBackground) {
            mainViewModel.updateTrip.postValue(update)
        } else {
            mainViewModel.updateTrip.value = update
        }
    }

    protected fun updateTripPoint(update: Boolean = true, onBackground: Boolean = false) {
        if (onBackground) {
            mainViewModel.updateTripPoint.postValue(update)
        } else {
            mainViewModel.updateTripPoint.value = update
        }
    }

    protected fun updateDocument(update: Boolean = true, onBackground: Boolean = false) {
        if (onBackground) {
            mainViewModel.updateDocument.postValue(update)
        } else {
            mainViewModel.updateDocument.value = update
        }
    }


    protected fun updateExpenseRoom(update: Boolean = true, onBackground: Boolean = false) {
        if (onBackground) {
            mainViewModel.updateExpenseRoom.postValue(update)
        } else {
            mainViewModel.updateExpenseRoom.value = update
        }
    }

    private fun listenToNavigate() {
        viewModel.navigateEvent.safeObserve {
            when (it) {
                is NavigationEvent.Action -> navigateTo(it.action)
                is NavigationEvent.Destination -> navigateTo(it.destinationId, it.bundle)
                is NavigationEvent.Back -> navigateBack()
            }
        }
    }

    private fun listenToToast() {
        viewModel.toastEvent.safeObserve { toast ->
            showToast(toast)
        }
    }

    private fun listenToAlertDialog() {
        viewModel.showAlertDialogEvent.safeObserve { alertInfo ->
            showAlertDialog(alertInfo)
        }
    }

    private fun listenToSnackbar() {
        viewModel.snackbarEvent.safeObserve { snackbar ->
            showSnackbar(snackbar)
        }
    }

    private fun listenToHideKeyboard() {
        viewModel.hideKeyboardEvent.safeObserve {
            hideKeyboard()
        }
    }

    private fun listenToLoading() {
        viewModel.showLoading.safeObserve { show ->
            if (this is WithLoading) {
                if (show) showLoading() else hideLoading()
            }
        }
    }

    private fun listenToDatePicker() {
        viewModel.datePickerEvent.safeObserve { date ->
            val datePicker = date.customDatePicker ?: MaterialDatePicker.Builder.datePicker()
                .setSelection(date.initialDate)
                .setTitleText(date.title ?: 0)
                .setCalendarConstraints(date.constraints?.build())
                .build()

            datePicker.apply {
                addOnPositiveButtonClickListener(date.onPickedDate)
                show(this@MvvmFragment.parentFragmentManager, "datePicker")
            }
        }
    }

    private fun listenToTimePicker() {
        viewModel.timePickerEvent.safeObserve { time ->
            val clockFormat = if (DateFormat.is24HourFormat(requireContext())) {
                TimeFormat.CLOCK_24H
            } else {
                TimeFormat.CLOCK_12H
            }

            val timePicker = time.customTimePicker
                ?: MaterialTimePicker.Builder().apply {
                    setTimeFormat(time.timeFormat ?: clockFormat)
                    setTitleText(time.title ?: 0)
                    time.hour?.let { setHour(it) }
                    time.minute?.let { setMinute(it) }
                }.build()

            timePicker.apply {
                addOnPositiveButtonClickListener {
                    time.onPickedTime.invoke(timePicker.hour, timePicker.minute)
                }
                show(this@MvvmFragment.parentFragmentManager, "timePicker")
            }
        }
    }

    private fun saveAndOpenFile() {
        viewModel.saveAndOpenFileEvent.safeObserve { downloaded ->
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                withIO {
                    downloaded.data.use {
                        val fileManager = FileManager(requireContext())
                        if (fileManager.saveDataToFile(downloaded)) {
                            if (!openFile(downloaded, fileManager, downloaded.type)) {
                                showSnackbar(SnackbarInfo.error(R.string.open_document_fail))
                            }
                        } else {
                            showSnackbar(SnackbarInfo.error(R.string.save_file_error_document))
                        }
                    }
                }
            }
        }
    }

    private fun removeFile() {
        viewModel.removeFileEvent.safeObserve { filename ->
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                FileManager(requireContext()).deleteFile(filename)
            }
        }
    }

    /**
     * Called on **IO thread**.
     */
    protected open fun openFile(
        file: DownloadedFile,
        fileManager: FileManager,
        type: DocumentType?
    ): Boolean {
        return fileManager.openFile(file.filename, type)
    }

    /**
     * Called on **IO thread**.
     */
    protected open fun savingFileFailed() {
        showSnackbar(SnackbarInfo.error(R.string.save_file_error_document))
    }

    companion object {
        protected const val MAP_BUNDLE_KEY = "mapBundleKey"
    }
}