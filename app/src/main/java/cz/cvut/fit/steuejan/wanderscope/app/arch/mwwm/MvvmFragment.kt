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
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseFragment
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.FileManager
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.WithLoading
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
        saveAndOpenFile()
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
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
                            openFile(downloaded, fileManager)
                        }
                    }
                }
            }
        }
    }

    protected fun openFile(file: DownloadedFile, fileManager: FileManager): Boolean {
        return fileManager.openFile(file.filename)
    }
}