package com.movies.allmovies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.movies.allmovies.ui.mylist.MyListFragment
import com.movies.allmovies.ui.home.HomeFragment
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.movies.allmovies.di.MoviesApplication
import com.movies.allmovies.di_subcomponent.MainComponent
import com.movies.allmovies.util.SharedPrefManager
import com.movies.allmovies.util.Util



class MainActivity : AppCompatActivity() {

    lateinit var mainComponent: MainComponent

    private var home: HomeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //Tem q ser antes do super
        mainComponent = (applicationContext as MoviesApplication).appComponent.mainComponent().create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.my_tool_bar)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.INVISIBLE

        Log.d("callstest", "MainActivity onCreate called\n")
        // get device dimensions

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_my_list, R.id.navigation_search
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        SharedPrefManager.getInstance(this).countLaunch()
    }

    fun showRatingDialog() {
        if (SharedPrefManager.getInstance(this).isRatingAllowed()) {
            Util.showRatingDialog(
                this,
                getString(R.string.rate_play_store_popup_title),
                getString(R.string.rate_play_store_message),
                getString(R.string.rate_play_store_btn_dont_show_again), onDontShowAgain(),
                getString(R.string.rate_play_store_btn_later), onLater(),
                getString(R.string.rate_play_store_btn_rate), onRate()
            )
        }
    }

    private fun onRate(): View.OnClickListener {
        return View.OnClickListener {
            Log.d("limitelog", "onAvaliar")
            SharedPrefManager.getInstance(this).countLaunch()
            openPlayStore()
        }
    }

    private fun onLater(): View.OnClickListener {
        return View.OnClickListener {
            SharedPrefManager.getInstance(this).countLaunch()
        }
    }

    private fun onDontShowAgain(): View.OnClickListener {
        return View.OnClickListener {
            SharedPrefManager.getInstance(this).dontAllowRating()
        }
    }

    private fun openPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.play_store_uri))
        if(intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else {
            var toast =Toast.makeText(this, "You don't have Play Store installed", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

    }

    /*     onActivityResult replaces myListFragment with itself for updating
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

    fun onHomeClick() {
        var homeFragment = supportFragmentManager.findFragmentByTag("home")

//        if (homeFragment == null || !homeFragment.isAdded) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.nav_host_fragment, HomeFragment(), "home")
//                .commit()
//        }

    }

    fun onMyListClick() {
        var myListFragment = supportFragmentManager.findFragmentByTag("my_list")

//        if (myListFragment == null || !myListFragment.isAdded) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.nav_host_fragment, MyListFragment(), "my_list")
//                .commit()
//        }
//        val layout = resources.getLayout(R.layout.fragment_mylist).id
//        findNavController(R.layout.fragment_mylist).navigate(R.id.action_navigation_home_to_detailsActivity)
    }

}