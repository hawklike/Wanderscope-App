package cz.cvut.fit.steuejan.wanderscope.home.model

import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.common.recycler_item.EmptyItem

class HomeScreenEmptyItem : EmptyItem(
    R.string.home_screen_empty_item_title,
    R.drawable.image_not_found,
    marginTop = R.dimen.app_first_item_overview_margin,
    buttonText = R.string.add_trip
)