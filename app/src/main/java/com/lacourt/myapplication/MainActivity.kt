package com.lacourt.myapplication

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
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
import androidx.annotation.NonNull
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


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

        //JSOUP
        MyAsyncTask().execute()





    }
    public class MyAsyncTask: AsyncTask<Void, Void, Void>() {
        var text: String? = null

        override fun doInBackground(vararg params: Void?): Void? {
            var doc: Document  = Jsoup.connect("https://www.google.com/search?client=firefox-b-d&ei=6tLJXZf3NvGz5OUP8c6r6AM&q=amazon+hobbs+and+shaw+rent+itunes+%24&oq=amazon+hobbs+and+shaw+rent+itunes+%24&gs_l=psy-ab.3...2102.2102..2333...0.3..0.191.191.0j1......0....1..gws-wiz.......0i71.0SXi9V0Qc8U&ved=0ahUKEwiXlOO0jePlAhXxGbkGHXHnCj0Q4dUDCAo&uact=5").get()
            Log.d("jsouplog", "${doc.text()}")
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

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
