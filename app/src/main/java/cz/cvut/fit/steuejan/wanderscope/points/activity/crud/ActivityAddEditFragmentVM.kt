package cz.cvut.fit.steuejan.wanderscope.points.activity.crud

import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.steuejan.wanderscope.points.activity.api.request.ActivityRequest
import cz.cvut.fit.steuejan.wanderscope.points.activity.repository.ActivityRepository
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragmentVM

class ActivityAddEditFragmentVM(
    repository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : AbstractPointAddEditFragmentVM<ActivityRequest>(
    repository,
    savedStateHandle
) {

}