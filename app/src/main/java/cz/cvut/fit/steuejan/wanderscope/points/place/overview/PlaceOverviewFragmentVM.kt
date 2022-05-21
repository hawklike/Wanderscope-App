package cz.cvut.fit.steuejan.wanderscope.points.place.overview

import cz.cvut.fit.steuejan.wanderscope.points.common.overview.AbstractPointOverviewFragmentVM
import cz.cvut.fit.steuejan.wanderscope.points.place.api.response.PlaceResponse
import cz.cvut.fit.steuejan.wanderscope.points.place.repository.PlaceRepository

class PlaceOverviewFragmentVM(placeRepository: PlaceRepository) :
    AbstractPointOverviewFragmentVM<PlaceResponse>(placeRepository) {


    override suspend fun customizePointOverviewSuccess(data: PlaceResponse) {
        TODO("Not yet implemented")
    }
}