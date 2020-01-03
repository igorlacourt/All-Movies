package com.movies.allmovies.util

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {
    var prefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    fun getInstance(ctx: Context): Prefs {
        if (prefs == null) {
            prefs = ctx.getSharedPreferences("apprater", 0)
        }
        return Prefs
    }

    object Prefs {
        fun countLaunch() {

            if (prefs?.getBoolean("dont_show_again", false)!!) { return ; }

            initEditor()

            if(prefs?.getLong("first_launch_date", 0)?.toInt() == 0) {
                editor?.putLong("first_launch_date", System.currentTimeMillis())
            }

            // Increment launch counter
            var launch_count = prefs!!.getLong("launch_count", 0) + 1
            editor?.putLong("launch_count", launch_count)
            editor?.apply()
        }

        fun isRatingAllowed(): Boolean {
            if (prefs?.getBoolean("dont_show_again", false)!!) { return false }

            var firstLaunchDate = prefs?.getLong("first_launch_date", 0)

            var launchCount = prefs!!.getLong("launch_count", 0)

            var daysInterval: Long = 0

            firstLaunchDate?.let { firstLaunchDate ->
                daysInterval = System.currentTimeMillis() - firstLaunchDate
            }

            if (launchCount > 3) {
                if(daysInterval > 172800000) {
                    editor?.putLong("first_launch_date", System.currentTimeMillis())
                    editor?.apply()
                    return true
                }
            }
            else if (launchCount.toInt() == 3)
                return true

            return false
        }

        fun dontAllowRating() {
            initEditor()
            editor?.putBoolean("dont_show_again", true)
            editor?.apply()
        }

        private fun initEditor() {
            if(editor == null)
                editor = prefs?.edit()
        }
    }
}