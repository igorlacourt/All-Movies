package com.lacourt.myapplication.ui.mylist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.MainActivity
import com.lacourt.myapplication.R
import com.lacourt.myapplication.ui.GridAdapter
import com.lacourt.myapplication.ui.OnItemClick
import com.lacourt.myapplication.ui.home.HomeFragment
import com.lacourt.myapplication.ui.home.MovieAdapter
import com.lacourt.myapplication.viewmodel.MyListViewModel
import kotlinx.android.synthetic.main.fragment_mylist.*
import org.junit.Test

class MyListFragment : Fragment(), OnItemClick {
    private val onItemClick = this as OnItemClick
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MyListViewModel
    private lateinit var adapter: GridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mylist, container, false)
        val progressBar: ProgressBar = root.findViewById(R.id.my_list_progress_circular)

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

            if (list.isNullOrEmpty())
                emptyList.visibility = View.VISIBLE
            else{
                emptyList.visibility = View.INVISIBLE
            }

            progressBar.visibility = View.INVISIBLE
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).mostraDialogoAvaliacao()
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

    @Test
    fun testNavigationToInGameScreen() {
        // Create a mock NavController
//        val mockNavController = mock(NavController::class.java)
//
//        // Create a graphical FragmentScenario for the TitleScreen
//        val titleScenario = launchFragmentInContainer<MyListFragment>()
//
//        // Set the NavController property on the fragment
//        titleScenario.onFragment { fragment ->
//            Navigation.setViewNavController(fragment.requireView(), mockNavController)
//        }
//
//        // Verify that performing a click prompts the correct Navigation action
//        onView(ViewMatchers.withId(R.id.play_btn)).perform(ViewActions.click())
//        verify(mockNavController).navigate(R.id.action_title_screen_to_in_game)
    }

}