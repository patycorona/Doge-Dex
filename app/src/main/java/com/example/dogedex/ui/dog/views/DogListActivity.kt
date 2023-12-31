package com.example.dogedex.ui.dog.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dogedex.R
import com.example.dogedex.data.model.response.ApiServiceInterceptor
import com.example.dogedex.databinding.ActivityDogListBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DOG_KEY
import com.example.dogedex.domain.model.ConstantGeneral.Companion.GRID_SPAN_COUNT
import com.example.dogedex.domain.model.ConstantGeneral.Companion.USER_KEY
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.ui.dog.adapter.DogAdapter
import com.example.dogedex.ui.dog.viewmodel.DogListViewModel
import com.example.dogedex.ui.dog.views.compose.DogDetailComposeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogListActivity : AppCompatActivity() {

    lateinit var binding: ActivityDogListBinding
    private val dogListViewModel: DogListViewModel by viewModels()

    private val listDogsObserver = Observer<List<DogModel>> { dogModel ->
        dogModel?.let {
            val adapter = DogAdapter(
                it,
                this,
                onItemClickListener
            )
            binding?.dogRecyclerview?.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        binding.loadingWheel.visibility = View.GONE
    }



    private val onItemClickListener: ((dogModel: DogModel) -> Unit) = { dogModel ->
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DOG_KEY,dogModel)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent?.extras?.getParcelable<AuthModel>(USER_KEY)

        if(user == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }else{
            ApiServiceInterceptor.setSessionToken(user.authentication_token)
        }

        dogListViewModel.getdogColection()

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


