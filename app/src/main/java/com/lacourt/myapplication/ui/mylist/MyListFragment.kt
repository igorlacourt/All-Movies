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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.ui.home.MovieAdapter
import com.lacourt.myapplication.viewmodel.MyListViewModel
import kotlinx.android.synthetic.main.fragment_mylist.*

class MyListFragment : Fragment(), OnMyListItemClick {
    private val onMyListItemClick = this as OnMyListItemClick
    private lateinit var recyclerView: RecyclerView
    private lateinit var myListViewModel: MyListViewModel
    private lateinit var adapter: MyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_mylist, container, false)
        val progressBar: ProgressBar = root.findViewById(R.id.my_list_progress_circular)

        var emptyList = root.findViewById<TextView>(R.id.edt_my_list_empty)
        emptyList.visibility = View.VISIBLE

        progressBar.visibility = View.VISIBLE
        adapter = MyListAdapter(activity?.applicationContext, onMyListItemClick, ArrayList())
        recyclerView = root.findViewById(R.id.my_list_list)


        setUpRecyclerView()
        myListViewModel =
            ViewModelProviders.of(this).get(MyListViewModel::class.java)
        myListViewModel.myList.observe(this, Observer { list ->
            Log.d("receivertest", "onChange, list.size = ${list.size}")
            adapter.setList(list)

            if (!list.isNullOrEmpty())
                emptyList.visibility = View.INVISIBLE

            progressBar.visibility = View.INVISIBLE
        })

        return root
    }

    private fun setUpRecyclerView() {
        recyclerView.computeVerticalScrollOffset()
        var layoutManager = GridLayoutManager(context, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun onMyListItemClick(id: Int?) {
        if (id != 0) {
            val myListToDetailsFragment =
                id?.let { MyListFragmentDirections.actionNavigationMyListToDetailsFragment(it) }
            myListToDetailsFragment?.let { findNavController().navigate(it) }
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }
}