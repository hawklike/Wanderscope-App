package cz.cvut.fit.steuejan.wanderscope.trip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.DatePickerInfo.Companion.today
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.mediator.PairMediatorLiveData
import cz.cvut.fit.steuejan.wanderscope.app.util.getDateFromDatePicker
import kotlinx.coroutines.launch

class AddTripFragmentVM : BaseViewModel() {

    val name = MutableLiveData<String>()
    val startDate = MutableLiveData<String?>()
    val endDate = MutableLiveData<String?>()

    val validateName = name.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    val validateDates = PairMediatorLiveData(startDate, endDate).switchMapSuspend { (start, end) ->
        if (start.isNullOrBlank()) {
            startDateInMillis = null
        }
        if (end.isNullOrBlank()) {
            endDateInMillis = null
        }
        validator.validateDates(startDateInMillis, endDateInMillis)
    }

    val enableSubmit = ValidationMediator(
        validateName,
        validateDates
    )

    private var startDateInMillis: Long? = null
    private var endDateInMillis: Long? = null

    fun startDatePicker() {
        showDatePicker(DatePickerInfo(
            initialDate = startDateInMillis ?: endDateInMillis ?: today
        ) { dateInMillis ->
            viewModelScope.launch {
                startDateInMillis = dateInMillis
                startDate.value = getDateFromDatePicker(dateInMillis)
            }
        })
    }

    fun endDatePicker() {
        showDatePicker(DatePickerInfo(
            initialDate = endDateInMillis ?: startDateInMillis ?: today
        ) { dateInMillis ->
            viewModelScope.launch {
                endDateInMillis = dateInMillis
                endDate.value = getDateFromDatePicker(dateInMillis)
            }
        })
    }

}