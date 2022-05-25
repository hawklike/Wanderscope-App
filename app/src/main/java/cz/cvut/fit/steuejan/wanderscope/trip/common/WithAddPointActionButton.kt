package cz.cvut.fit.steuejan.wanderscope.trip.common

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.app.util.doNothing

interface WithAddPointActionButton {

    fun prepareActionButton(actionButton: SpeedDialView) {
        actionButton.apply {
            createActionItem(R.id.add_point_transport, R.drawable.ic_outline_plane, R.string.add_transport)
            createActionItem(R.id.add_point_accommodation, R.drawable.ic_outline_house, R.string.add_accommodation)
            createActionItem(R.id.add_point_place, R.drawable.ic_outline_location, R.string.add_place)
            createActionItem(R.id.add_point_activity, R.drawable.ic_outline_hiking, R.string.add_activity)
        }

        actionButton.apply {
            setOnActionSelectedListener {
                when (it.id) {
                    R.id.add_point_accommodation -> onClick(::addAccommodation)
                    R.id.add_point_activity -> onClick(::addActivity)
                    R.id.add_point_transport -> onClick(::addTransport)
                    R.id.add_point_place -> onClick(::addPlace)
                    else -> onClick(doNothing)
                }
                false
            }
        }
    }

    fun addAccommodation()
    fun addActivity()
    fun addTransport()
    fun addPlace()

    fun SpeedDialView.onClick(action: () -> Unit): Boolean {
        action.invoke()
        close()
        return false
    }

    fun SpeedDialView.createActionItem(
        @IdRes id: Int,
        @DrawableRes icon: Int,
        @StringRes title: Int
    ) = addActionItem(
        SpeedDialActionItem.Builder(id, icon)
            .setFabImageTintColor(context.getColor(R.color.colorBackground))
            .setLabel(title)
            .setLabelColor(context.getColor(R.color.colorBackground))
            .setLabelBackgroundColor(context.getColor(R.color.colorPrimary))
            .create()
    )
}
