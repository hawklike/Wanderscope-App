package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.room

import androidx.lifecycle.MutableLiveData
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.LoadingMutableLiveData
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.request.ExpenseRoomRequest
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.model.Currency
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.repository.ExpenseRepository

class ExpenseRoomAddEditFragmentVM(
    private val expenseRepository: ExpenseRepository
) : BaseViewModel() {

    val name = MutableLiveData<String>()

    val member = MutableLiveData<String?>(null)
    val memberChip = MutableLiveData<ChipInfo?>()
    val showMembers = MutableLiveData<Boolean>()

    val validateName = name.switchMapSuspend {
        validator.validateIfNotEmpty(it)
    }

    val validateMember = member.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateExpenseMember(it)
    }

    val enableAddMember = ValidationMediator(validateMember)

    val enableSubmit = ValidationMediator(
        validateName,
        validateMember
    )

    val loading = LoadingMutableLiveData()

    val submitEvent = SingleLiveEvent<ExpenseRoomRequest>()

    private var selectedCurrencyPos: Int? = null

    fun selectCurrency(position: Int) {
        selectedCurrencyPos = position
    }

    fun addMember() {
        val memberValue = member.value
        if (!memberValue.isNullOrBlank()) {
            addMemberChip(memberValue)
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
            ?: Currency.EURO
    }

    fun submit(request: ExpenseRoomRequest) {
        //todo
    }
}