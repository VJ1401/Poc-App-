package `in`.redbus.trendingapp

import `in`.redbus.trendingapp.databinding.ActivityMainBinding
import `in`.redbus.trendingapp.network.ApiAdapter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.ActionBar


class MainActivity : AppCompatActivity(),Adapter.AdapterCallBack {
    private lateinit var binding:ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private lateinit var recyclerAdapter: Adapter
    private var selectedPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val mainRepository = Repository(ApiAdapter.apiClient)
        viewModel = ViewModelProvider(this, ViewModelFactory(mainRepository)).get(MainViewModel::class.java)

        val actionBar: ActionBar? = supportActionBar

        val colorDrawable = ColorDrawable(Color.parseColor("#3FC299"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        // initialize recycler view
        initializeRecyclerView(savedInstanceState)
        viewModelObservers()
        if(savedInstanceState != null){
            selectedPos = savedInstanceState.getInt("position")
        }
        initializeSearchView()
        initialView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("position", selectedPos)
    }

    override fun onClick(position: Int) {
        selectedPos  = position
    }

    private fun viewModelObservers() {
        viewModel.listDetails.observe(this, Observer { it ->
            it.data?.let {
                if(it.total_count > 0){
                    binding.rvList.visibility = View.VISIBLE
                    recyclerAdapter.updateData(ArrayList(it.items),selectedPos)
                    binding.rvList.scrollToPosition(selectedPos);
                    binding.searchView.visibility = View.VISIBLE
                    binding.homeLayout.root.visibility = View.GONE
                } else {
                    binding.rvList.visibility = View.GONE
                    binding.searchView.visibility = View.GONE
                    binding.homeLayout.root.visibility = View.VISIBLE
                    Toast.makeText(this, "No Results Found", Toast.LENGTH_SHORT).show()
                }
            }
            if (it.isLoading) {
                binding.rvList.visibility = View.GONE
                binding.searchView.visibility = View.GONE
                binding.homeLayout.root.visibility = View.VISIBLE
            } else {

            }
            if (it.isError) {
                binding.rvList.visibility = View.GONE
                binding.searchView.visibility = View.GONE
                binding.homeLayout.root.visibility = View.VISIBLE
                Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeRecyclerView(savedInstanceState: Bundle?) {
        recyclerAdapter =
            Adapter(
                ArrayList(),this,selectedPos
            )
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        with(binding.rvList) {
            layoutManager = manager
            addItemDecoration(itemDecorator)
            adapter = recyclerAdapter
        }


    }

    private fun initializeSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recyclerAdapter.filter.filter(newText)
                return false
            }

        })
    }

    private fun initialView() {
        binding.homeLayout.search.setOnClickListener {
            viewModel.getAllList()
            binding.homeLayout.progressBar.visibility = View.VISIBLE
        }
    }

}