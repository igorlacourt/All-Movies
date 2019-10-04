package com.lacourt.myapplication.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.lacourt.myapplication.R
import kotlinx.android.synthetic.main.search_fragment.*
import android.text.Editable
import android.text.TextWatcher

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        viewModel.searchResult.observe(this, Observer{resultList ->
            resultList.forEach{movie ->
                search_results_tv.append("\n${movie.title}\n")
            }
            //TODO show list of movie
        })

        edt_search.addTextChangedListener(searchTextWatcher())
    }

    fun searchTextWatcher() = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isNotEmpty())
                viewModel.searchMovie(s.toString())
        }
    }
}
