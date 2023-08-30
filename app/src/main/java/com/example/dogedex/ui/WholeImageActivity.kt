package com.example.dogedex.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.dogedex.R
import com.example.dogedex.databinding.ActivityWholeImageBinding
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ERROR_PHOTO_URI
import com.example.dogedex.domain.model.ConstantGeneral.Companion.PHOTO_URI_KEY
import java.io.File

class WholeImageActivity : AppCompatActivity() {

    lateinit var binding: ActivityWholeImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityWholeImageBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_whole_image)

        val photoUri = intent.extras?.getString(PHOTO_URI_KEY)
        val uri = Uri.parse(photoUri)
        val path = uri.path

        if( path == null){

            Toast.makeText(this, ERROR_PHOTO_URI,Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.wholeImage.load(File(path))

    }
}