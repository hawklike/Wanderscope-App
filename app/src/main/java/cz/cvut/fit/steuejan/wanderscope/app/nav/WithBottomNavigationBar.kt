package cz.cvut.fit.steuejan.wanderscope.app.nav

import com.google.android.material.snackbar.Snackbar

interface WithBottomNavigationBar {
    fun showBottomNavigation()
    fun hideBottomNavigation()
    fun showSnackbar(snackbar: Snackbar)
}