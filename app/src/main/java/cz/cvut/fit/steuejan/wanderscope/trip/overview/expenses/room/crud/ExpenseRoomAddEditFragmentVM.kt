package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.livedata.AnySingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.request.ExpenseRoomRequest
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.model.Currency
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseRoomAddEditFragmentVM(
    private val expenseRepository: ExpenseRepository
) : BaseViewModel() {

    val name = MutableLiveData<String>()
    val currency = MutableLiveData(DEFAULT_CURRENCY.getCode())

    val member = MutableLiveData<String?>(null)
    val memberChip = MutableLiveData<ChipInfo?>()
    val showMembers = MutableLiveData<Boolean>()
    private val members = mutableSetOf<String>()

    val validateName = name.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    val validateCurrency = currency.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    val validateMember = member.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateExpenseMember(it)
    }

    val enableAddMember = ValidationMediator(validateMember)

    val enableSubmit = ValidationMediator(
        validateName,
        validateMember,
        validateCurrency
    )

    val loading = LoadingMutableLiveData()

    val submitEvent = SingleLiveEvent<ExpenseRoomRequest>()
    val requestIsSuccess = AnySingleLiveEvent()

    private var selectedCurrencyPos: Int? = null

    fun selectCurrency(position: Int) {
        selectedCurrencyPos = position
    }

    fun addMember() {
        val memberValue = member.value
        if (!memberValue.isNullOrBlank()) {
            viewModelScope.launch {
                val added = withDefault { members.add(memberValue) }
                if (!added) {
                    validateMember.postValue(R.string.member_must_be_unique)
                } else {
                    addMemberChip(memberValue)
                }
            }
        }
    }

    private fun addMemberChip(member: String) {
        memberChip.value = ChipInfo(member, true, textColor = R.color.colorPrimary)
        showMembers.value = true
        hideKeyboard()
        this.member.value = null
    }

    fun addExpenseRoom() {
        loading.value = true
        val request = ExpenseRoomRequest(
            name.value ?: return,
            getSelectedCurrency().getCode(),
            emptyList()
        )
        submitEvent.value = request
    }

    private fun getSelectedCurrency(): Currency {
        return Currency.values().getOrNull(selectedCurrencyPos ?: -1)
            ?: DEFAULT_CURRENCY
    }

    fun submit(request: ExpenseRoomRequest, tripId: Int) {
        viewModelScope.launch {
            expenseRepository.createExpenseRoom(tripId, request).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> createRoomFailure(it.error)
                    is Result.Loading -> loading.value = true
                    is Result.Success -> requestIsSuccess.publish()
                }
            }
        }
    }

    private fun createRoomFailure(error: Error) {
        loading.value = false
        unexpectedError(error)
    }

    companion object {
        private val DEFAULT_CURRENCY = Currency.EURO
    }
}