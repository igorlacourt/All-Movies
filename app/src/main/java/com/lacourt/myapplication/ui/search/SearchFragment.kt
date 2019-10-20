package com.lacourt.myapplication.ui.search

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.lacourt.myapplication.R
import com.lacourt.myapplication.di.ViewModelProviderFactory
import com.lacourt.myapplication.ui.details.DetailsActivity
import com.lacourt.myapplication.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.search_fragment.*
import javax.inject.Inject

class SearchFragment : Fragment(), OnSearchedItemClick{
    lateinit var viewModel: SearchViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var progressBar: ProgressBar

    @set:Inject
    var providerFactory: ViewModelProviderFactory? = null

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.search_fragment, container, false)

        progressBar = view.findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE

        recyclerView = view.findViewById(R.id.searched_list)
        adapter = SearchAdapter(context, this, ArrayList())

        var linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, providerFactory).get(SearchViewModel::class.java)
        viewModel.searchResult.observe(this, Observer{resultList ->
            adapter.setList(resultList)
            progressBar.visibility = View.INVISIBLE

            if(resultList == null || resultList.isEmpty())
                search_no_results.visibility = View.VISIBLE
            else
                search_no_results.visibility = View.INVISIBLE
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
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onSearchItemClick(id: Int) {
        val i = Intent(context, DetailsActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }

    override fun onStop() {
        super.onStop()
//        setResult(Activity.RESULT_CANCELED, Intent())
    }
}
