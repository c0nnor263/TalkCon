package com.conboi.talkcon.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.conboi.talkcon.R
import com.conboi.talkcon.databinding.ActivityMainBinding
import com.conboi.talkcon.utils.popBackStackAllInstances
import com.conboi.talkcon.utils.shared.FirebaseAuthLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()

        viewModel.authState.observe(this) { state ->
            if (state == FirebaseAuthLiveData.AuthState.UNAUTHENTICATED) {
                navController.popBackStackAllInstances(
                    navController.currentBackStackEntry?.destination?.id!!,
                    true
                )
                if (navController.currentDestination?.id != R.id.loginFragment) {
                    navController.navigate(R.id.loginFragment)
                }
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }
}