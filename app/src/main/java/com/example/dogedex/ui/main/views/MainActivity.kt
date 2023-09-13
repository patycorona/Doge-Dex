package com.example.dogedex.ui.main.views

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.dogedex.R
import com.example.dogedex.data.model.response.ApiServiceInterceptor
import com.example.dogedex.databinding.ActivityMainBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ALPHA_1
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ALPHA_2
import com.example.dogedex.domain.model.ConstantGeneral.Companion.CONFIDENCE
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DOG_KEY
import com.example.dogedex.domain.model.ConstantGeneral.Companion.IS_RECOGNITION_KEY
import com.example.dogedex.domain.model.ConstantGeneral.Companion.LABEL_PATH
import com.example.dogedex.domain.model.ConstantGeneral.Companion.MODEL_PATH
import com.example.dogedex.domain.model.ConstantGeneral.Companion.MSG_RC_
import com.example.dogedex.domain.model.ConstantGeneral.Companion.TAG
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.domain.model.DogRecognition
import com.example.dogedex.ui.dog.views.DogListActivity
import com.example.dogedex.ui.dog.views.compose.DogDetailComposeActivity
import com.example.dogedex.ui.machinelearning.ClassiFier
import com.example.dogedex.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.common.FileUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var imageCapture:ImageCapture
    private lateinit var cameraExecutor:ExecutorService
    private lateinit var classfier: ClassiFier
    private var user : AuthModel = AuthModel()
    private var isCamaraReady = false

    private val mainViewModel:MainViewModel by viewModels()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)

         user = intent?.extras?.getParcelable<AuthModel>(ConstantGeneral.USER_KEY)!!

        if(user == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }else{
            ApiServiceInterceptor.setSessionToken(user!!.authentication_token)
        }

        initListener()
        initObserver()
        requestCameraPermission()
    }

    private fun initListener(){
        binding.fabListDog.setOnClickListener {
            openDogListActivity(user)
        }
    }

    private fun openDogListActivity(authModel: AuthModel?){
        val intent = Intent(this, DogListActivity::class.java)
        intent.putExtra(ConstantGeneral.USER_KEY,authModel)
        startActivity(intent)
    }

    private val DogObserver = Observer<DogModel>{ dog ->
        if(dog != null){
            openDogDetailActivity(dog)
        } else{
            binding.loadingWheel.visibility = View.GONE
            Toast.makeText(this,getString(R.string.error_photo_uri),Toast.LENGTH_SHORT).show()
            return@Observer
        }
    }

    private val dogRecognitionObserver = Observer<DogRecognition>{ Dog_R ->
        enableTakePhotoButtom(Dog_R)
    }

    private fun initObserver() {
        mainViewModel.dog.observe(this, DogObserver)
        mainViewModel.dog_Recognition.observe(this, dogRecognitionObserver)
    }

    override fun onStart() {
        super.onStart()
        classfier = ClassiFier(FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity,LABEL_PATH))
    }

    private fun starCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalisis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalisis.setAnalyzer(cameraExecutor) { imageProxy ->
                mainViewModel.recognizeImage(imageProxy, classfier)
            }
            cameraProvider.bindToLifecycle(
                this, cameraSelector,
                preview, imageCapture, imageAnalisis)

        },ContextCompat.getMainExecutor(this))
    }

    private fun enableTakePhotoButtom(dogRecognition: DogRecognition) {
        binding.apply {
            if(dogRecognition.confidence > CONFIDENCE){
                fabTakePhoto.alpha = ALPHA_1
                fabTakePhoto.setOnClickListener {
                    mainViewModel.getDogByMlid(dogRecognition.ml_id)
                }
            }else{
                fabTakePhoto.alpha = ALPHA_2
                fabTakePhoto.setOnClickListener(null)
            }
        }
    }

    private fun openDogDetailActivity(dog:DogModel){
        val intent = Intent(this, DogDetailComposeActivity::class.java)

        intent.putExtra(DOG_KEY, dog)
        intent.putExtra(IS_RECOGNITION_KEY, true)
        startActivity(intent)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                 setUpCamera()
            } else {
                Toast.makeText(this, getString(R.string.text_requires_permits), Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestCameraPermission(){
        Log.e(TAG, MSG_RC_ + Build.VERSION.SDK_INT , )
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setUpCamera()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.accept_permissions))
                        .setMessage(getString(R.string.accept_permissions_msg))
                        .setPositiveButton(android.R.string.ok){
                                _,_ ->
                            requestPermissionLauncher.launch(
                                android.Manifest.permission.CAMERA
                            )
                        }
                        .setNegativeButton(android.R.string.cancel){
                                _,_ ->
                        }.show()
                }
                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
        }else{
            setUpCamera()
        }
    }

    private fun setUpCamera(){
        binding.cameraPreview.post {
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            starCamera()
            isCamaraReady = true
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        if(::cameraExecutor.isInitialized){
            cameraExecutor.shutdown()
        }
    }
}