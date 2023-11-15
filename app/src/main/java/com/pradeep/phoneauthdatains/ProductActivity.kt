package com.pradeep.phoneauthdatains

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.BottomNavigationView)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.Home ->
                    changeFragment(HomeFragment())
                R.id.AddProduct -> changeFragment(AddProductFragment())
                R.id.Profile -> changeFragment(ProfileFragment())
            }
            false
        }
    }
    fun changeFragment (fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
    }

}
