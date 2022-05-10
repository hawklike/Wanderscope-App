package cz.cvut.fit.steuejan.wanderscope.trip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel.DatePickerInfo.Companion.today
import cz.cvut.fit.steuejan.wanderscope.app.util.getDateFromDatePicker
import kotlinx.coroutines.launch

class AddTripFragmentVM : BaseViewModel() {

    val startDate = MutableLiveData<String?>()

    private var startDateInMillis: Long? = null

    fun startDatePicker() {
        showDatePicker(DatePickerInfo(initialDate = startDateInMillis ?: today) { dateInMillis ->
            viewModelScope.launch {
                startDateInMillis = dateInMillis
                startDate.value = getDateFromDatePicker(dateInMillis)
            }
        })
    }

}