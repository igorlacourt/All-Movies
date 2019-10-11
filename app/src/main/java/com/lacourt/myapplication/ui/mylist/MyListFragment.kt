package com.lacourt.myapplication.ui.mylist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.ui.details.DetailsActivity
import com.lacourt.myapplication.ui.home.MovieAdapter
import com.lacourt.myapplication.viewmodel.MyListViewModel

class MyListFragment : Fragment(), OnMyListItemClick{
    override fun onMyListItemClick(id: Int) {
        val i = Intent(activity, DetailsActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }

    private val onMyListItemClick = this as OnMyListItemClick
    private lateinit var recyclerView: RecyclerView
    private lateinit var myListViewModel: MyListViewModel
    private lateinit var adapter: MyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val progressBar: ProgressBar = root.findViewById(R.id.progress_circular)
        adapter = MyListAdapter(activity?.applicationContext, onMyListItemClick, ArrayList())
        recyclerView = root.findViewById(R.id.movie_list)
        myListViewModel =
            ViewModelProviders.of(this).get(MyListViewModel::class.java)

        progressBar.visibility = View.VISIBLE

        setUpRecyclerView()

        myListViewModel.myList.observe(this, Observer { list ->
            adapter.setList(list)
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
}