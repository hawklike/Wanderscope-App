package cz.cvut.fit.steuejan.wanderscope.app.bussiness.loading

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout

interface WithLoading {

    val content: View
    val shimmer: ShimmerFrameLayout

    fun showLoading() {
        content.visibility = View.GONE
        shimmer.visibility = View.VISIBLE
    }

    fun hideLoading() {
        shimmer.visibility = View.GONE
        content.visibility = View.VISIBLE
    }
}