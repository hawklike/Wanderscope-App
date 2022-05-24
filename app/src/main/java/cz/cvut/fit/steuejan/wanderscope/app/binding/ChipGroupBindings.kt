package cz.cvut.fit.steuejan.wanderscope.app.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import cz.cvut.fit.steuejan.wanderscope.app.arch.BaseViewModel

@BindingAdapter("addChip")
fun ChipGroup.addChip(chipInfo: BaseViewModel.ChipInfo?) {
    chipInfo ?: return
    val chip = Chip(context).apply {
        text = chipInfo.text
        isCloseIconVisible = chipInfo.isCloseIconVisible
        setCloseIconResource(chipInfo.closeIconDrawable)
        setChipBackgroundColorResource(chipInfo.backgroundTint)
        setTextAppearanceResource(chipInfo.textAppearance)
        setTextColor(context.getColor(chipInfo.textColor))
        setCloseIconTintResource(chipInfo.closeIconTint)
        chipMinHeight = chipInfo.minHeight
    }

    chip.setOnCloseIconClickListener {
        this.removeView(it)
    }
    this.addView(chip)
}

@BindingAdapter("addChips")
fun ChipGroup.addChips(chipsInfo: List<BaseViewModel.ChipInfo>?) {
    removeAllViews()
    chipsInfo?.forEach { chipInfo ->
        addChip(chipInfo)
    }
}