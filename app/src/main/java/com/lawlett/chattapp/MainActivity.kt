package com.lawlett.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.lawlett.chattapp.auth.utils.gone
import com.lawlett.chattapp.auth.utils.visible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()

    }
    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            if (destination.id in arrayOf(
                    R.id.auth_fragment,
                    R.id.verification_fragment
//                    R.id.verificationFragment,
//                    R.id.authFragment,
//                    R.id.profileInfoFragment,
//                    R.id.aboutAppFragment,
//                    R.id.lessonsFragment,
//                    R.id.youtubeFragment,
//                    R.id.editProfileFragment
                )
            ) {
                bottom_navigation.gone()
            } else {
                Log.e("Main", "arguments: $arguments")
                bottom_navigation.visible()
            }
        }
    }
}