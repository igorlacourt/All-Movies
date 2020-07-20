package com.movies.allmovies.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.movies.allmovies.ui.OnMovieClick
import com.movies.allmovies.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import javax.inject.Inject

class SearchFragment : Fragment(), OnMovieClick {

    // Dagger code
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    private lateinit var recyclerView: RecyclerView
    private var progressBar: ProgressBar? = null
    private lateinit var edtSearch: EditText
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

        edtSearch = view.findViewById(R.id.edt_search)
        progressBar = view.findViewById(R.id.search_progress_bar)
        progressBar?.visibility = View.INVISIBLE
        adapter = SearchAdapter(context, this, ArrayList())

        recyclerView = view.findViewById(R.id.searched_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        (activity as MainActivity).showRatingDialog()

        showKeyBoard()

        viewModel.searchResult.observe(viewLifecycleOwner, Observer { resultList ->
            adapter.setList(resultList)
            if (resultList.isNullOrEmpty())
                search_no_results.visibility = View.VISIBLE
            else
                search_no_results.visibility = View.INVISIBLE

            progressBar?.visibility = View.INVISIBLE
        })

        edt_search.addTextChangedListener(searchTextWatcher())
    }

    private fun showKeyBoard() {
        edtSearch.requestFocus()
        val imgr = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT)
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

    override fun onClick(id: Int) {
        if (id != 0) {
            val myListToDetailsFragment =
                SearchFragmentDirections.actionNavigationSearchToDetailsFragment(id)
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
