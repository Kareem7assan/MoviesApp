package com.kareem.moviesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kareem.moviesapp.R
import com.kareem.moviesapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private  var binding: ActivityHomeBinding?=null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding = ActivityHomeBinding.bind(findViewById(R.id.mainContent))
        setActions()
    }



    private fun setActions(){
        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        binding?.mainBottomNavigation?.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.movies -> {
                    navController.navigate(R.id.action_global_moviesFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourites -> {
                    navController.navigate(R.id.action_global_favsFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}