package com.example.dogedex.ui.dog.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dogedex.databinding.ActivityDogListBinding
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DOG_KEY
import com.example.dogedex.domain.model.ConstantGeneral.Companion.GRID_SPAN_COUNT
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.ui.dog.adapter.DogAdapter
import com.example.dogedex.ui.dog.viewmodel.DogListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DogListActivity : AppCompatActivity() {

    lateinit var binding: ActivityDogListBinding
    private val dogListViewModel: DogListViewModel by viewModels()

    private val listDogsObserver = Observer<MutableList<DogModel>> { dogModel ->
        dogModel?.let {
            val adapter = DogAdapter(
                it,
                this,
                onItemClickListener)
            binding?.dogRecyclerview?.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        binding.loadingWheel.visibility = View.GONE
    }

    private val onItemClickListener: ((dogModel: DogModel) -> Unit) = { dogModel ->

        if(dogModel != null){
            val intent = Intent(this, DogDetailItemActivity::class.java)
            intent.putExtra(DOG_KEY,dogModel)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dogListViewModel.getAllDogs()

        binding.loadingWheel.visibility = View.VISIBLE
        initRecycler()
        initObserver()
    }

    private fun initObserver() {
        dogListViewModel.dogList.observe(this, listDogsObserver)
    }

    private fun initRecycler() {
        val recycler = binding.dogRecyclerview
        recycler.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        val linearLayoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        binding?.dogRecyclerview?.apply {
            layoutManager = linearLayoutManager
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

}


