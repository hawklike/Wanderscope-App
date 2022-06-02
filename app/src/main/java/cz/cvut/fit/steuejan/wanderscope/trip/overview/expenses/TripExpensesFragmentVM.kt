package cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel
import cz.cvut.fit.steuejan.wanderscope.app.arch.adapter.RecyclerItem
import cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading.LoadingMediator
import cz.cvut.fit.steuejan.wanderscope.app.common.Constants
import cz.cvut.fit.steuejan.wanderscope.app.common.Result
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem
import cz.cvut.fit.steuejan.wanderscope.app.extension.delayAndReturn
import cz.cvut.fit.steuejan.wanderscope.app.extension.launchIO
import cz.cvut.fit.steuejan.wanderscope.app.extension.safeCollect
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault
import cz.cvut.fit.steuejan.wanderscope.app.nav.NavigationEvent.Action
import cz.cvut.fit.steuejan.wanderscope.app.retrofit.response.Error
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.api.response.ExpenseRoomsResponse
import cz.cvut.fit.steuejan.wanderscope.trip.overview.expenses.repository.ExpenseRepository
import cz.cvut.fit.steuejan.wanderscope.trip.overview.root.TripPagerFragmentDirections

class TripExpensesFragmentVM(
    private val expenseRepository: ExpenseRepository
) : BaseViewModel() {

    val rooms = MutableLiveData<List<RecyclerItem>>()

    private val roomsLoading = MutableLiveData<Boolean>()
    val loading = LoadingMediator(roomsLoading).delayAndReturn(Constants.DELAY_LOADING)

    private var tripId: Int? = null

    fun loadRooms(tripId: Int) {
        this.tripId = tripId
        viewModelScope.launchIO {
            expenseRepository.getExpenseRooms(tripId).safeCollect(this) {
                when (it) {
                    is Result.Cache -> TODO()
                    is Result.Failure -> loadRoomsFailure(it.error)
                    is Result.Loading -> roomsLoading.value = true
                    is Result.Success -> loadRoomsSuccess(it.data)
                }
            }
        }
    }

    private suspend fun loadRoomsSuccess(data: ExpenseRoomsResponse) {
        val items = withDefault {
            data.rooms.mapIndexed { index, room ->
                room.toItem(index == 0)
            }
        }
        rooms.value = items.ifEmpty { listOf(EmptyItem.expenseRooms()) }
        roomsLoading.value = false
    }

    private fun loadRoomsFailure(error: Error) {
        roomsLoading.value = false
        unexpectedError(error)
    }

    fun addRoom() {
        navigateTo(
            Action(
                TripPagerFragmentDirections
                    .actionTripPagerFragmentToExpenseRoomAddEditFragment(tripId ?: return)
            )
        )
    }
}