package cz.cvut.fit.steuejan.wanderscope

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cz.cvut.fit.steuejan.wanderscope.app.arch.mwwm.MvvmActivity
import cz.cvut.fit.steuejan.wanderscope.app.nav.WithBottomNavigationBar
import cz.cvut.fit.steuejan.wanderscope.app.toolbar.WithToolbar
import cz.cvut.fit.steuejan.wanderscope.auth.WithLogin
import cz.cvut.fit.steuejan.wanderscope.databinding.ActivityMainBinding

class MainActivity : MvvmActivity<ActivityMainBinding, MainActivityVM>(
    R.layout.activity_main,
    MainActivityVM::class
), WithBottomNavigationBar, WithToolbar, WithLogin {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        handleSplashScreen(splashScreen)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as? NavHostFragment ?: return

        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)

        val mainFragments = setOf(R.id.homeFragment, R.id.accountFragment, R.id.loginFragment, R.id.tripsFragment)
        val appBarConfiguration = AppBarConfiguration(mainFragments)
        setupActionBarWithNavController(navController, appBarConfiguration)

        observeLogout()
    }

    private fun handleSplashScreen(splashScreen: SplashScreen) {
        viewModel.showSplashScreen.safeObserve { show ->
            splashScreen.setKeepOnScreenCondition {
                return@setKeepOnScreenCondition show
            }
        }
    }

    private fun observeLogout() {
        viewModel.shouldLogoutUser().safeObserve { shouldLogout ->
            if (shouldLogout) {
                logout()
            }
        }
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

    override fun setTitle(title: String?) {
        binding.toolbar.title = title
    }

    override fun login() {
        val startDestination = navController.graph.startDestinationId
        navController.popBackStack(startDestination, true)
        navigateTo(R.id.loginFragment)
    }

    override fun logout() {
        viewModel.logout()
        login()
    }
}