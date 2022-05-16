package cz.cvut.fit.steuejan.wanderscope.points.activity.crud

import androidx.navigation.fragment.navArgs
import cz.cvut.fit.steuejan.wanderscope.R
import cz.cvut.fit.steuejan.wanderscope.databinding.FragmentPointActivityAddEditBinding
import cz.cvut.fit.steuejan.wanderscope.points.common.crud.AbstractPointAddEditFragment

class ActivityAddEditFragment : AbstractPointAddEditFragment<
        FragmentPointActivityAddEditBinding,
        ActivityAddEditFragmentVM>(
    R.layout.fragment_point_activity_add_edit,
    ActivityAddEditFragmentVM::class
) {

    private val args: ActivityAddEditFragmentArgs by navArgs()

    override fun initViewModel() {
        viewModel.init(args.tripId, R.string.add_activity)
    }
}