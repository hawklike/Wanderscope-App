package cz.cvut.fit.steuejan.wanderscope.points.place.crud

import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointPlaceAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment

class PlaceAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointPlaceAddEditBinding,
        PlaceAddEditFragmentVM>(
    R.layout.fragment_point_place_add_edit,
    PlaceAddEditFragmentVM::class
) {

    private val args: PlaceAddEditFragmentArgs by navArgs()

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_place)
    }
}