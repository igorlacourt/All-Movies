package com.movies.allmovies.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.movies.allmovies.MainActivity
import com.movies.allmovies.R
import com.movies.allmovies.databinding.FragmentSearchBinding
import com.movies.allmovies.ui.OnMovieClick
import com.movies.allmovies.viewmodel.SearchViewModel
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

    private lateinit var adapter: SearchAdapter
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecyclerView()

        (activity as MainActivity).showRatingDialog()
        showKeyBoard()

        attachSearchObserver()
        attachRetryClickListener()

        binding.etSearch.addTextChangedListener(searchTextWatcher())

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvSearchedList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = SearchAdapter(requireContext(), this, ArrayList())
        binding.rvSearchedList.adapter = adapter
    }

    private fun attachSearchObserver() {
        viewModel.searchResult.observe(viewLifecycleOwner, Observer { resultList ->
            adapter.setList(resultList)
        })
    }

    private fun attachRetryClickListener() {
        binding.iErrorScreen.btRetry.setOnClickListener {
            viewModel.searchMovie(binding.etSearch.text.toString())
        }
    }

    private fun showKeyBoard() {
        binding.etSearch.requestFocus()
        val imgr = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun searchTextWatcher() = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isNotEmpty())
                viewModel.searchMovie(s.toString())
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
}