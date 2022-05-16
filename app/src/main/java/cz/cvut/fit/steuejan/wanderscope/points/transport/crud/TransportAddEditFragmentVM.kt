package cz.cvut.fit.steuejan.wanderscope.points.transport.crud

import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.transport.api.request.TransportRequest
import cz.cvut.fit.steuejan.wanderscope.points.transport.repository.TransportRepository

class TransportAddEditFragmentVM(
    repository: TransportRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<TransportRequest>(
    repository,
    savedStateHandle
)