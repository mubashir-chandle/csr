package com.csrapp.csr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.csrapp.csr.R
import com.csrapp.csr.utils.ResourceProvider

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment)

        // Set up application context so that other classes can access
        // resources such as strings.
        ResourceProvider.setUpApplicationContext(this.applicationContext)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
