package cz.cvut.fit.steuejan.wanderscope

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmActivity
import cz.cvut.fit.steuejan.wanderscope.app.nav.WithBottomNavigationBar
import cz.cvut.fit.steuejan.wanderscope.app.toolbar.WithToolbar
import cz.cvut.fit.steuejan.wanderscope.databinding.ActivityMainBinding

class MainActivity : MvvmActivity<ActivityMainBinding, MainActivityVM>(
    R.layout.activity_main,
    MainActivityVM::class
), WithBottomNavigationBar, WithToolbar {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as? NavHostFragment ?: return

        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)

        val mainFragments = setOf(R.id.FirstFragment, R.id.SecondFragment)
        val appBarConfiguration = AppBarConfiguration(mainFragments)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun showBottomNavigation() {
        binding.bottomNavigation.visibility = VISIBLE
    }

    override fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = GONE
    }

    override fun showToolbar() {
        binding.toolbar.visibility = VISIBLE
    }

    override fun hideToolbar() {
        binding.toolbar.visibility = GONE
    }
}