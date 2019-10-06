package com.lacourt.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.lacourt.myapplication.ui.dashboard.DashboardFragment
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
        setSupportActionBar(toolbar);

        supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment, HomeFragment(), "home")
            .commit()

        descButton.setOnClickListener {
            val home = supportFragmentManager.findFragmentByTag("home")
            if (home != null)
                (home as HomeFragment).orderDateDesc()
        }

        ascButton.setOnClickListener {
            val home = supportFragmentManager.findFragmentByTag("home")
            if (home != null) {
                (home as HomeFragment)?.orderDateAsc()
            }
        }

        search_btn.setOnClickListener {
            var i = Intent(this, SearchActivity::class.java)
            startActivity(i)
        }
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
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
                .replace(R.id.nav_host_fragment, DashboardFragment(), "my_list")
                .commit()
        }
    }
}
