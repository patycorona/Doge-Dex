package com.example.dogedex.ui.dog.views.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.dogedex.R
import com.example.dogedex.data.model.response.DefaultResponse
import com.example.dogedex.domain.model.ConstantGeneral
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.ui.dog.viewmodel.DogDetailViewModel
import com.example.dogedex.ui.dog.views.compose.ui.theme.DogeDexTheme

class DogDetailComposeActivity : ComponentActivity() {

    private val dogDetailViewModel: DogDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dogItem = intent?.extras?.getParcelable<DogModel>(ConstantGeneral.DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(ConstantGeneral.IS_RECOGNITION_KEY, false) ?: false

        if(dogItem == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            DogeDexTheme {

               DogDetailScreen(
                   dog = dogItem,
                   onButtonClicked = {
                       onButtonClicked(dogItem.id,isRecognition)
                   },
                   onErrorDialogDismiss = {
                       Toast.makeText(this, R.string.try_again, Toast.LENGTH_SHORT).show()
                   }
               )
            }
        }
    }

    private val addDogObserver:((defaultResponse: DefaultResponse) -> Unit) = { addDog ->
        if(addDog.isSuccess) Toast.makeText(this, getString(R.string.text_add_dog),Toast.LENGTH_SHORT).show()
    }

    private fun initObserver() {
        dogDetailViewModel.add_Dog.observe(this,addDogObserver)
    }

    private fun onButtonClicked(dogId: Long,isRecognition :Boolean){
        if(isRecognition) dogDetailViewModel.addDogToUser(dogId)
        else finish()

        initObserver()
    }
}
