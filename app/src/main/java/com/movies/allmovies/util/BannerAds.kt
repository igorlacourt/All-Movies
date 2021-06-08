package com.movies.allmovies.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.movies.allmovies.R

class BannerAds(view: View) {
    private var mAdView = view.findViewById<AdView>(R.id.adView)
    private var adRequest = AdRequest.Builder().build()

    fun loadAds(context: Context?) {
        if (context != null) {
            MobileAds.initialize(context) {}
            mAdView.loadAd(adRequest)
        }
    }
    fun removeAds() {
        if (mAdView != null) {
            (mAdView.parent as ViewGroup).removeView(mAdView)
            mAdView.adListener = null
            mAdView.removeAllViews()
            mAdView.destroy()
            mAdView = null
            adRequest = null
        }
    }
}