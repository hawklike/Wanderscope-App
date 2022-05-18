package cz.cvut.fit.steuejan.wanderscope.app.binding

import android.graphics.drawable.Drawable
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter

@BindingAdapter("dropdownMenuDrawable")
fun AutoCompleteTextView.setDropdownMenuDrawable(drawable: Drawable) {
    setDropDownBackgroundDrawable(drawable)
}

@BindingAdapter("onItemClick")
fun AutoCompleteTextView.setItemClickListener(onClickListener: AdapterView.OnItemClickListener) {
    this.onItemClickListener = onClickListener
}