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
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog

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

        mostraDialogoAvaliacao()
    }

    private fun app_launched(mContext: Context) {
        var prefs: SharedPreferences = mContext.getSharedPreferences("apprater", 0);

        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        var editor = prefs.edit()

        // Increment launch counter
        var launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        var date_firstLaunch = prefs.getLong("date_firstlaunch", 0)
        if (date_firstLaunch.toInt() == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                mostraDialogoAvaliacao()
            }
        }

        editor.commit()
    }

    private fun mostraDialogoAvaliacao() {
        Util.mostraDialogoAvaliacao(
            this,
            getString(R.string.avalia_play_store_popup_titulo),
            getString(R.string.avalia_play_store_mensagem),
            getString(R.string.avalia_play_store_btn_nunca), onNunca(),
            getString(R.string.avalia_play_store_btn_mais_tarde), onMaisTarde(),
            getString(R.string.avalia_play_store_btn_avaliar), onAvaliar()
        )
    }

    private fun onAvaliar(): View.OnClickListener {
        return View.OnClickListener {
            Log.d("limitelog", "onAvaliar")
            abrePlayStore()
        }
    }

    private fun onMaisTarde(): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    private fun onNunca(): View.OnClickListener {
        return View.OnClickListener {

        }
    }

    private fun abrePlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(getString(R.string.play_store_uri))
        startActivity(intent)
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

object Util {
    fun mostraDialogoAvaliacao(
        context: Context,
        titulo: String, mensagem: String,
        bt1: String, l1: View.OnClickListener,
        bt2: String, l2: View.OnClickListener,
        bt3: String, l3: View.OnClickListener
    ) {
        val dialog = DialogHelper.getInstance(
            context,
            titulo,
            mensagem,
            bt1, l1,
            bt2, l2,
            bt3, l3
        )
        if (context !is Activity || !context.isFinishing)
            dialog.show()
    }
}

object DialogHelper {
    fun getInstance(
        context: Context, titulo: String, mensagem: CharSequence,
        labelB1: String, acaoButton1: View.OnClickListener?,
        labelB2: String?, acaoButton2: View.OnClickListener?,
        labelB3: String?, acaoButton3: View.OnClickListener?
    ): AlertDialog {
        val aviso: AlertDialog.Builder = AlertDialog.Builder(context)

        val view = View.inflate(context, R.layout.dialogo_avalia_play_store, null)
        val tituloField = view.findViewById<TextView>(R.id.title_rating_play_store)
        tituloField.text = titulo

        val mensagemField = view.findViewById<TextView>(R.id.message_rating_play_store)
        mensagemField.text = mensagem

        val b1 = view.findViewById<TextView>(R.id.btn_nunca)
        b1.text = labelB1
        b1.visibility = View.VISIBLE
        aviso.setView(view)

        var b2: TextView? = null
        if (labelB2 != null) {
            b2 = view.findViewById(R.id.btn_mais_tarde)
            b2!!.text = labelB2
            b2.visibility = View.VISIBLE
        }

        var b3: TextView? = null
        if (labelB3 != null) {
            b3 = view.findViewById(R.id.btn_avaliar)
            b3!!.text = labelB3
            b3.visibility = View.VISIBLE
        }

        val dialog = aviso.create()

        b1.setOnClickListener { v ->
            dialog.dismiss()
            acaoButton1?.onClick(v)
        }

        b2?.setOnClickListener { v ->
            dialog.dismiss()
            acaoButton2?.onClick(v)
        }

        b3?.setOnClickListener { v ->
            dialog.dismiss()
            acaoButton3?.onClick(v)
        }

        val estrelas = view.findViewById<View>(R.id.btn_go_to_play_store_estrelas) as LinearLayout
        estrelas.setOnClickListener { v ->
            dialog.dismiss()
            acaoButton3?.onClick(v)
        }

        return dialog
    }
}