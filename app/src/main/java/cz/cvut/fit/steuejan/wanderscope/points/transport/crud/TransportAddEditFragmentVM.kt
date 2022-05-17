package cz.cvut.fit.steuejan.wanderscope.points.transport.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository

class TransportAddEditFragmentVM(
    repository: TransportRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<TransportRequest>(
    repository,
    savedStateHandle
) {

    val car = MutableLiveData<String?>(null)
    val carChip = MutableLiveData<ChipInfo?>()
    val showCars = MutableLiveData<Boolean>()

    val seat = MutableLiveData<String?>(null)
    val seatChip = MutableLiveData<ChipInfo?>()
    val showSeats = MutableLiveData<Boolean>()

    val validateCar = car.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateCarsAndSeats(it)
    }

    val validateSeat = seat.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateCarsAndSeats(it)
    }

    val enableAddCar = ValidationMediator(validateCar)
    val enableAddSeat = ValidationMediator(validateSeat)

    override val enableSubmit = super.enableSubmit.add(
        validateCar,
        validateSeat
    )

    fun addCar() {
        carChip.value = ChipInfo(car.value ?: return, true, textColor = R.color.colorPrimary)
        showCars.value = true
        hideKeyboardEvent.publish()
        car.value = null
    }

    fun addSeat() {
        seatChip.value = ChipInfo(seat.value ?: return, true, textColor = R.color.colorPrimary)
        showSeats.value = true
        hideKeyboardEvent.publish()
        seat.value = null
    }
}