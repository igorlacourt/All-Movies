package com.movies.allmovies.ui.mylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.domainmodel.DomainMovie
import com.movies.allmovies.ui.GridAdapter
import com.movies.allmovies.ui.OnItemClick
import com.movies.allmovies.viewmodel.MyListViewModel

class MyListFragment : Fragment(), OnItemClick {
    private val onItemClick = this as OnItemClick
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MyListViewModel
    private lateinit var adapter: GridAdapter
    private var adViewBottomRv: AdView? = null
    private var adViewBottomScreen: AdView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mylist, container, false)
        val progressBar: ProgressBar = root.findViewById(R.id.my_list_progress_circular)

        Log.d("myadlog", "view = $view")
        Log.d("myadlog", "view = ${view.toString()}")

        adViewBottomRv = root.findViewById(R.id.mylist_adview_bottom_rv)
        adViewBottomScreen = root.findViewById(R.id.mylist_adview_bottom_screen)

        val emptyList = root.findViewById<TextView>(R.id.edt_my_list_empty)
        emptyList.visibility = View.VISIBLE

        progressBar.visibility = View.VISIBLE
        adapter = GridAdapter(context, onItemClick, ArrayList())
        recyclerView = root.findViewById(R.id.my_list_list)

        setUpRecyclerView()
        viewModel =
            ViewModelProviders.of(this).get(MyListViewModel::class.java)
        viewModel.myList.observe(this, Observer { list ->
            Log.d("receivertest", "onChange, list.size = ${list.size}")
            adapter.setList(list)

            if (list.isNullOrEmpty()) {
                emptyList.visibility = View.VISIBLE
                makeAdsInvisible()
            }
            else {
                emptyList.visibility = View.INVISIBLE
                setAdsVisibility(list)
            }

            progressBar.visibility = View.INVISIBLE
        })

        loadAdsBottomRv()
        loadAdsBottomScreen()

        return root
    }

    private fun makeAdsInvisible() {
        adViewBottomRv?.visibility = View.INVISIBLE
        adViewBottomScreen?.visibility = View.INVISIBLE
    }

    private fun setAdsVisibility(list: List<DomainMovie>) {
        if (list.size >= 7){
            adViewBottomRv?.visibility = View.VISIBLE
            adViewBottomScreen?.visibility = View.INVISIBLE
        } else {
            adViewBottomRv?.visibility = View.INVISIBLE
            adViewBottomScreen?.visibility = View.VISIBLE
        }
    }

    fun loadAdsBottomRv() {
        if (context != null) {
            MobileAds.initialize(context) {}
            val adRequest = AdRequest.Builder().build()
            adViewBottomRv?.loadAd(adRequest)
        }
    }

    fun loadAdsBottomScreen() {
        if (context != null) {
            MobileAds.initialize(context) {}
            val adRequest = AdRequest.Builder().build()
            adViewBottomScreen?.loadAd(adRequest)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).showRatingDialog()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getList()
    }

    private fun setUpRecyclerView() {
        recyclerView.computeVerticalScrollOffset()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(id: Int) {
        val myListToDetailsFragment
                = MyListFragmentDirections.actionNavigationMyListToDetailsFragment(id)
        findNavController().navigate(myListToDetailsFragment)
    }

//    @Test
//    fun testNavigationToInGameScreen() {
//        // Create a mock NavController
////        val mockNavController = mock(NavController::class.java)
////
////        // Create a graphical FragmentScenario for the TitleScreen
////        val titleScenario = launchFragmentInContainer<MyListFragment>()
////
////        // Set the NavController property on the fragment
////        titleScenario.onFragment { fragment ->
////            Navigation.setViewNavController(fragment.requireView(), mockNavController)
////        }
////
////        // Verify that performing a click prompts the correct Navigation action
////        onView(ViewMatchers.withId(R.id.play_btn)).perform(ViewActions.click())
////        verify(mockNavController).navigate(R.id.action_title_screen_to_in_game)
//    }

}