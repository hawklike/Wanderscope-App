package cz.cvut.fit.steuejan.wanderscope.app.common.map

import cz.cvut.fit.steuejan.wanderscope.app.common.data.Coordinates

data class LocationBundle(
    val coordinates: Coordinates?,
    val title: String?
)