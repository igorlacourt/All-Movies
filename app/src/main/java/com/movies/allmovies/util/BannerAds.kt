package com.movies.allmovies.util

import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.movies.allmovies.R

object BannerAds {
    fun loadAds(context: Context?, view: View) {
        if (context != null) {
            var mAdView = view.findViewById<AdView>(R.id.adView)
            MobileAds.initialize(context) {}
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }
    }
}