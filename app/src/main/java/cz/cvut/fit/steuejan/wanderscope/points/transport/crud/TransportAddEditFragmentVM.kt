package cz.cvut.fit.steuejan.wanderscope.points.transport.crud

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.Companion.OK
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.InputValidator.ValidateDates
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.validation.ValidationMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Address
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates
import cz.cvut.fit.steuejan.wanderscope.app.common.data.Duration
import cz.cvut.fit.steuejan.wanderscope.app.extension.getOrNullIfBlank
import cz.cvut.fit.steuejan.wanderscope.app.extension.switchMapSuspend
import cz.cvut.fit.steuejan.wanderscope.app.livedata.SingleLiveEvent
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing
import cz.cvut.fit.steuejan.wanderscope.app.util.multipleLet
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.response.TransportResponse
import cz.cvut.fit.steuejan.wanderscope.points.transport.model.TransportType
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository
import kotlinx.coroutines.launch

class TransportAddEditFragmentVM(
    repository: TransportRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<TransportRequest, TransportResponse>(
    repository,
    savedStateHandle
) {

    val car = MutableLiveData<String?>(null)
    val carChip = MutableLiveData<ChipInfo?>()
    val showCars = MutableLiveData<Boolean>()
    val carChips = MutableLiveData<List<ChipInfo>?>(null)

    val seat = MutableLiveData<String?>(null)
    val seatChip = MutableLiveData<ChipInfo?>()
    val showSeats = MutableLiveData<Boolean>()
    val seatChips = MutableLiveData<List<ChipInfo>?>(null)

    private var findOption: FindOption? = null
    private var fromName: String? = null
    private var toName: String? = null
    private var fromId: String? = null
    private var toId: String? = null
    private var fromCoordinates: Coordinates? = null
    private var toCoordinates: Coordinates? = null

    val from = MutableLiveData<String?>(null)
    val to = MutableLiveData<String?>(null)

    val submitEvent = SingleLiveEvent<TransportRequest>()

    val validateCar = car.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateCarsAndSeats(it)
    }

    val validateSeat = seat.switchMapSuspend {
        if (it.isNullOrBlank()) OK else validator.validateCarsAndSeats(it)
    }

    val validateFrom = from.switchMapSuspend {
        if (it.isNullOrBlank()) {
            fromName = null
        }
        validateAddress(it)
    }

    val searchIconFromActive = from.map {
        !it.isNullOrBlank()
    }

    val searchIconToActive = to.map {
        !it.isNullOrBlank()
    }

    val validateTo = to.switchMapSuspend {
        if (it.isNullOrBlank()) {
            toName = null
        }
        validateAddress(it)
    }

    val enableAddCar = ValidationMediator(validateCar)
    val enableAddSeat = ValidationMediator(validateSeat)

    override val enableSubmit = super.enableSubmit.add(
        validateFrom,
        validateTo,
        validateCar,
        validateSeat
    )

    override suspend fun validateDates(startDate: String?, endDate: String?, type: ValidateDates): Int {
        return super.validateDates(startDate, endDate, ValidateDates.DEPARTURE)
    }

    override fun setupEdit(point: TransportResponse, title: Int) {
        super.setupEdit(point, title)
        viewModelScope.launch {
            point.address.let {
                from.value = it.name
                fromId = it.googlePlaceId
            }
            point.to.let {
                to.value = it.name
                toId = it.googlePlaceId
            }
            fromCoordinates = point.coordinates
            toCoordinates = point.toCoordinates
            point.cars?.let {
                showCars.value = true
                carChips.value = it.map(::createChip)
            }
            point.seats?.let {
                showSeats.value = true
                seatChips.value = it.map(::createChip)
            }
            type.value = point.type.toStringRes()
        }
    }

    fun addCar() {
        val carValue = car.value
        if (!carValue.isNullOrBlank()) {
            addCarChip(carValue)
        }
    }

    private fun addCarChip(car: String) {
        carChip.value = createChip(car)
        showCars.value = true
        hideKeyboard()
        this.car.value = null
    }

    fun addSeat() {
        val seatValue = seat.value
        if (!seatValue.isNullOrBlank()) {
            addSeatChip(seatValue)
        }
    }

    private fun addSeatChip(seat: String) {
        seatChip.value = createChip(seat)
        showSeats.value = true
        hideKeyboard()
        this.seat.value = null
    }

    fun findFrom() {
        val whatToSearch = fromName ?: from.value
        findOption = FindOption.FROM
        findAccommodationEvent.value = whatToSearch
    }

    fun findTo() {
        val whatToSearch = toName ?: to.value
        findOption = FindOption.TO
        findAccommodationEvent.value = whatToSearch
    }

    override fun placeFound(place: Place) {
        when (findOption) {
            FindOption.FROM -> {
                fromName = place.name
                fromId = place.id
                place.address?.let { from.value = it }
                fromCoordinates = createCoordinates(place)
            }
            FindOption.TO -> {
                toName = place.name
                toId = place.id
                place.address?.let { to.value = it }
                toCoordinates = createCoordinates(place)
            }
            else -> doNothing
        }
        multipleLet(fromName, toName) { from, to ->
            name.value = "$from – $to"
        }
    }

    override fun submit() {
        submitLoading.value = true
        viewModelScope.launch {
            val request = createRequest() ?: run {
                submitLoading.value = false
                return@launch
            }
            submitEvent.value = request
        }
    }

    override fun createRequest(): TransportRequest? {
        val name = name.value ?: return null
        return TransportRequest(
            name = name,
            duration = Duration(startDateTime, endDateTime),
            type = getTypeFromSelectedItem(),
            from = Address(fromId, from.value.getOrNullIfBlank()),
            to = Address(toId, to.value.getOrNullIfBlank()),
            cars = null,
            seats = null,
            description = description.value.getOrNullIfBlank(),
            fromCoordinates = fromCoordinates,
            toCoordinates = toCoordinates
        )
    }

    private fun getTypeFromSelectedItem(): TransportType {
        return TransportType.values().getOrNull(selectedTypePosition ?: -1)
            ?: TransportType.OTHER
    }

    enum class FindOption {
        FROM, TO
    }

    private fun createChip(name: String) = ChipInfo(name, true, textColor = R.color.colorPrimary)
}