package com.gracodev.postkeeper.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gracodev.postkeeper.R
import com.gracodev.postkeeper.Utils.hideFabWithAnimation
import com.gracodev.postkeeper.Utils.isInternetAvailable
import com.gracodev.postkeeper.Utils.revealFabWithAnimation
import com.gracodev.postkeeper.Utils.toVisibility
import com.gracodev.postkeeper.databinding.ActivityMainBinding
import com.gracodev.postkeeper.ui.viewmodels.ConnectivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val connectivityViewModel: ConnectivityViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_list, R.id.navigation_news
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            findNavController(R.id.nav_host_fragment_activity_main)
                .navigate(R.id.action_global_entryFormFragment)
        }

        showNoConnectionMessage(isInternetAvailable())
        ShowHideFAB(isInternetAvailable())

        connectivityViewModel.startMonitoringConnectivity()

        connectivityViewModel.isConnected.observe(this) { isConnected ->
            showNoConnectionMessage(isConnected)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.searchEntriesFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityViewModel.stopMonitoringConnectivity()
    }

    private fun showNoConnectionMessage(internetAvailable: Boolean) {
        binding.lNoConnection.visibility = internetAvailable.toVisibility()
        ShowHideFAB(internetAvailable)
    }

    private fun ShowHideFAB(internetAvailable: Boolean) {
        if (internetAvailable)
            revealFAB()
        else
            hideFAB()
    }

    fun revealFAB() {
        revealFabWithAnimation(R.id.fab, R.anim.revel_fab)
    }

    fun hideFAB() {
        hideFabWithAnimation(R.id.fab, R.anim.hide_fab)
    }
}