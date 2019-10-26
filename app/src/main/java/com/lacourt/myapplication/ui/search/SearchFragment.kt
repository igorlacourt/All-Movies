package com.lacourt.myapplication.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.lacourt.myapplication.R
import com.lacourt.myapplication.ui.OnItemClick
import com.lacourt.myapplication.ui.mylist.MyListFragmentDirections
import com.lacourt.myapplication.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), OnItemClick {
    private lateinit var viewModel: SearchViewModel
    private lateinit var recyclerView: RecyclerView
    private var progressBar: ProgressBar? = null
    private lateinit var adapter: SearchAdapter

    companion object {
        fun newInstance() = SearchFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val type: String? = arguments?.getString("item_type", null)

        progressBar = view.findViewById(R.id.search_progress_bar)
        progressBar?.visibility = View.INVISIBLE
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        adapter = SearchAdapter(context,this, ArrayList())

        recyclerView = view.findViewById(R.id.searched_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        progressBar?.visibility = View.VISIBLE

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.searchResult.observe(this, Observer { resultList ->
            adapter.setList(resultList)
            progressBar?.visibility = View.INVISIBLE

            if (resultList == null || resultList.isEmpty()) {
                Log.d("searchlog", "observe, resultList.size = ${resultList.size}")
                search_no_results.visibility = View.VISIBLE
            } else {
                Log.d("searchlog", "observe, resultList.size = ${resultList.size}")
                search_no_results.visibility = View.INVISIBLE
            }
        })

        edt_search.addTextChangedListener(searchTextWatcher())
    }

    fun searchTextWatcher() = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isNotEmpty())
                viewModel.searchMovie(s.toString())
            progressBar?.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(id: Int) {
        if (id != 0) {
            val myListToDetailsFragment = MyListFragmentDirections.actionNavigationMyListToDetailsFragment(id)
            findNavController().navigate(myListToDetailsFragment)
        } else {
            Toast.makeText(context, "Sorry. Can not load this movie. :/", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
//        setResult(Activity.RESULT_CANCELED, Intent())
    }

}
