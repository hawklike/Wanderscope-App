package cz.cvut.fit.steuejan.wanderscope.points.place.crud

import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.api.request.PlaceRequest
import cz.cvut.fit.steuejan.wanderscope.points.place.repository.PlaceRepository

class PlaceAddEditFragmentVM(
    repository: PlaceRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<PlaceRequest>(
    repository,
    savedStateHandle
)