package com.lacourt.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lacourt.myapplication.ui.mylist.MyListFragment
import com.lacourt.myapplication.ui.home.HomeFragment
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.lacourt.myapplication.util.SharedPrefManager
import com.lacourt.myapplication.util.Util



class MainActivity : AppCompatActivity() {

    private var home: HomeFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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

    fun mostraDialogoAvaliacao() {
        if (SharedPrefManager.getInstance(this).isRatingAllowed()) {
            Util.mostraDialogoAvaliacao(
                this,
                getString(R.string.avalia_play_store_popup_titulo),
                getString(R.string.avalia_play_store_mensagem),
                getString(R.string.avalia_play_store_btn_nunca), onNunca(),
                getString(R.string.avalia_play_store_btn_mais_tarde), onMaisTarde(),
                getString(R.string.avalia_play_store_btn_avaliar), onAvaliar()
            )
        }
    }

    private fun onAvaliar(): View.OnClickListener {
        return View.OnClickListener {
            Log.d("limitelog", "onAvaliar")
            openPlayStore()
        }
    }

    private fun onMaisTarde(): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    private fun onNunca(): View.OnClickListener {
        return View.OnClickListener {
            SharedPrefManager.getInstance(this).neverAllowRating()
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