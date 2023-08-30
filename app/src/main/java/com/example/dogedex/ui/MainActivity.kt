package com.example.dogedex.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.dogedex.R
import com.example.dogedex.data.model.response.ApiServiceInterceptor
import com.example.dogedex.databinding.ActivityMainBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ERROR_TAKE_PHOTO
import com.example.dogedex.domain.model.ConstantGeneral.Companion.EXTENSION
import com.example.dogedex.domain.model.ConstantGeneral.Companion.PHOTO_URI_KEY
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var imageCapture:ImageCapture
    private lateinit var cameraExecutor:ExecutorService
    private var isCamaraReady = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)

        val user = intent?.extras?.getParcelable<AuthModel>(ConstantGeneral.USER_KEY)

        if(user == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }else{
            ApiServiceInterceptor.setSessionToken(user.authentication_token)
        }

        requestCameraPermission()
        initListener()
    }

    private fun initListener(){

        binding.fabTakePhoto.setOnClickListener {
            if (isCamaraReady) {
                takePhoto()
            }
        }
    }

    private fun takePhoto(){
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
         object :ImageCapture.OnImageSavedCallback{

                override fun onError(error : ImageCaptureException){
                    Toast.makeText(this@MainActivity, ERROR_TAKE_PHOTO + "${error.message}", Toast.LENGTH_SHORT).show()
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val photoUri = outputFileResults.savedUri

                    openWholeImageActivity(photoUri.toString())
                }
        })
    }

    private fun openWholeImageActivity(photoUri:String){

        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(PHOTO_URI_KEY, photoUri)
        startActivity(intent)
    }

    private fun getOutputPhotoFile(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let{
            File(it,resources.getString(R.string.app_name) + EXTENSION).apply { mkdirs() }
        }

        return if(mediaDir != null && mediaDir.exists()){
            mediaDir
        }
        else{
            filesDir
        }
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
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees

                imageProxy.close()
            }

            cameraProvider.bindToLifecycle(
                this, cameraSelector,
                preview, imageCapture, imageAnalisis
            )
        },ContextCompat.getMainExecutor(this))
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                 setUpCamera()
            } else {
                Toast.makeText(this, ConstantGeneral.REQUIRES_PERMITS, Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestCameraPermission(){
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
                        .setTitle(ConstantGeneral.ACCEPT_PERMISSION)
                        .setMessage(ConstantGeneral.ACCEPT_PERMISSION_MSG)
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