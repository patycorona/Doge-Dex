package com.example.dogedex.ui.dog.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dogedex.R
import com.example.dogedex.databinding.ActivityDogDetailItemBinding
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DOG_KEY
import com.example.dogedex.domain.model.DogModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogDetailItemActivity : AppCompatActivity() {

    lateinit var  binding: ActivityDogDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogDetailItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dogItem = intent?.extras?.getParcelable<DogModel>(DOG_KEY)

        if(dogItem == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        binding.apply {

            dogIndex.text = getString(R.string.dog_index_format, dogItem.index)
            lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, dogItem.life_expectancy)
            dog = dogItem

            Glide.with(this@DogDetailItemActivity)
                .load(dogItem.image_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dogImage)

            closeButton.setOnClickListener { finish() }
        }
    }
}