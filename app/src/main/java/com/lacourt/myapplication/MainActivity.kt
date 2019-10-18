package com.lacourt.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lacourt.myapplication.ui.details.DetailsActivity
import com.lacourt.myapplication.ui.mylist.MyListFragment
import com.lacourt.myapplication.ui.home.HomeFragment
import com.lacourt.myapplication.ui.search.SearchActivity
import kotlinx.android.synthetic.main.bottom_nav.*
import kotlinx.android.synthetic.main.tool_bar_layout.*

class MainActivity : AppCompatActivity() {

    private var home: HomeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.my_tool_bar)
        setSupportActionBar(toolbar)

        Log.d("callstest", "MainActivity onCreate called\n")


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_my_list
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_search  -> {startActivity(this, Intent(this, DetailsActivity::class.java))}
                R.id.navigation_my_list -> {}
                R.id.navigation_home -> {}

            }
        }


//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.nav_host_fragment, HomeFragment(), "home")
//            .commit()
//
//        descButton.setOnClickListener {
//            val home = supportFragmentManager.findFragmentByTag("home")
//            if (home != null)
//                (home as HomeFragment).orderDateDesc()
//        }
//
//        ascButton.setOnClickListener {
//            val home = supportFragmentManager.findFragmentByTag("home")
//            if (home != null) {
//                (home as HomeFragment)?.orderDateAsc()
//            }
//        }
//
//        search_btn.setOnClickListener {
//            var i = Intent(this, SearchActivity::class.java)
//            startActivityForResult(i, 1)
//        }

    }

    /*     onActivityResult replaces myListFragment wiht itself for updating
     the list in realtime when coming back from search coming from details.  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, MyListFragment(), "my_list")
                .commit()
        }
    }

    fun onHomeClick(view: View) {
        var homeFragment = supportFragmentManager.findFragmentByTag("home")

        if (homeFragment == null || !homeFragment.isAdded) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment(), "home")
                .commit()
        }

    }

    fun onMyListClick(view: View) {
        var myListFragment = supportFragmentManager.findFragmentByTag("my_list")

        if (myListFragment == null || !myListFragment.isAdded) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, MyListFragment(), "my_list")
                .commit()
        }
    }
}
