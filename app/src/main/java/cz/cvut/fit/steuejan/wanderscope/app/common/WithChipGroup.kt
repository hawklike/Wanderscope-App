package cz.cvut.fit.steuejan.wanderscope.app.common

import androidx.core.view.allViews
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import cz.cvut.fit.steuejan.wanderscope.app.extension.withDefault

interface WithChipGroup {

    suspend fun extractChips(chipGroup: ChipGroup): List<String> {
        return withDefault {
            chipGroup.allViews.fold(listOf()) { acc, view ->
                val text = (view as? Chip)?.text?.toString()
                text?.let { acc + it } ?: acc
            }
        }
    }
}