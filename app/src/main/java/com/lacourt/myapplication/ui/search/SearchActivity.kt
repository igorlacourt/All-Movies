package com.lacourt.myapplication.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lacourt.myapplication.R
import com.lacourt.myapplication.model.dto.Movie
import com.lacourt.myapplication.ui.details.DetailsActivity
import com.lacourt.myapplication.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), OnSearchedItemClick{
    private lateinit var viewModel: SearchViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.search_progress_bar)
        setUpReyclerview()
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        progressBar.visibility = View.INVISIBLE

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
        val i = Intent(this, DetailsActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }


    private fun setUpReyclerview(){
        recyclerView = findViewById(R.id.searched_list)
        adapter = SearchAdapter(this, ArrayList<Movie>())

        recyclerView.computeVerticalScrollOffset()

        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = adapter
        progressBar.visibility = View.VISIBLE
    }
}
