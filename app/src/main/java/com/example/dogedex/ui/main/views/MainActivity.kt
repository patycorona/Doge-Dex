package com.example.dogedex.ui.main.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.dogedex.R
import com.example.dogedex.data.model.response.ApiServiceInterceptor
import com.example.dogedex.databinding.ActivityMainBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DOG_KEY
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ERROR_PHOTO_URI
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ERROR_TAKE_PHOTO
import com.example.dogedex.domain.model.ConstantGeneral.Companion.EXTENSION
import com.example.dogedex.domain.model.ConstantGeneral.Companion.LABEL_PATH
import com.example.dogedex.domain.model.ConstantGeneral.Companion.MODEL_PATH
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.ui.dog.viewmodel.DogListViewModel
import com.example.dogedex.ui.dog.views.DogDetailItemActivity
import com.example.dogedex.ui.machinelearning.ClassFier
import com.example.dogedex.ui.machinelearning.DogRecognition
import com.example.dogedex.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.common.FileUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var imageCapture:ImageCapture
    private lateinit var cameraExecutor:ExecutorService
    private lateinit var classfier: ClassFier
    private var isCamaraReady = false

    private val mainViewModel:MainViewModel by viewModels()

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

        initObserver()
        requestCameraPermission()
    }

    private val DogObserver = Observer<DogModel>{ dog ->

        if(dog != null){
            binding.loadingWheel.visibility = View.GONE
            Toast.makeText(this,ERROR_PHOTO_URI,Toast.LENGTH_SHORT).show()
            return@Observer
        }
        else{
            openDogDetailActivity(dog)
        }

    }


    private fun initObserver() = mainViewModel.dog.observe(this, DogObserver)

    override fun onStart() {
        super.onStart()
         classfier = ClassFier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity,LABEL_PATH)
        )
    }

    /*private fun takePhoto(){
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
         object :ImageCapture.OnImageSavedCallback{

                override fun onError(error : ImageCaptureException){
                    Toast.makeText(this@MainActivity, ERROR_TAKE_PHOTO + "${error.message}", Toast.LENGTH_SHORT).show()
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                }
        })
    }*/

    /*private fun getOutputPhotoFile(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let{
            File(it,resources.getString(R.string.app_name) + EXTENSION).apply { mkdirs() }
        }

        return if(mediaDir != null && mediaDir.exists()){
            mediaDir
        }
        else{
            filesDir
        }
    }*/

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

                val bitmap = convertImageProxyToBitmap(imageProxy)
                    if(bitmap != null){
                        val dogRecognition = classfier.recognizeImage(bitmap).first()
                        enableTakePhotoButtom(dogRecognition)
                    }

                imageProxy.close()
            }

            cameraProvider.bindToLifecycle(
                this, cameraSelector,
                preview, imageCapture, imageAnalisis
            )
        },ContextCompat.getMainExecutor(this))
    }

    private fun enableTakePhotoButtom(dogRecognition: DogRecognition) {
       if(dogRecognition.confidence > 70.0){
           binding.fabTakePhoto.alpha = 1f

           binding.fabTakePhoto.setOnClickListener {
               mainViewModel.getDogByMlid(dogRecognition.id)
           }

       }else{
           binding.fabTakePhoto.alpha = 0.2f
           binding.fabTakePhoto.setOnClickListener(null)
       }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun convertImageProxyToBitmap(imageProxy: ImageProxy): Bitmap?{
        val image = imageProxy.image ?: return null
        val yBuffer = image.planes[0].buffer //Y
        val uBuffer = image.planes[1].buffer //u
        val vBuffer = image.planes[3].buffer //V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped\
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(0, 0, yuvImage.width, yuvImage.height), 100, out
        )
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    }



    private fun openDogDetailActivity(dog:DogModel){
        val intent = Intent(this,DogDetailItemActivity::class.java)
        intent.putExtra(DOG_KEY, dog)
        startActivity(intent)
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