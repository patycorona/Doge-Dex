package com.example.dogedex.data.repository

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageProxy
import com.example.dogedex.domain.model.ConstantGeneral
import com.example.dogedex.domain.model.DogRecognition
import com.example.dogedex.ui.machinelearning.ClassiFier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ClassiFierRepositoryImpl @Inject constructor():ClassiFierRepository {

    override suspend fun recognizeImage(imageProxy:ImageProxy, classiFier: ClassiFier): DogRecognition =
        withContext(Dispatchers.IO){
            val bitmap = convertImageProxyToBitmap(imageProxy)
            if (bitmap == null) DogRecognition("",0f)
                else classiFier.recognizeImage(bitmap).first()
        }

    @SuppressLint("UnsafeOptInUsageError")
    private fun convertImageProxyToBitmap(imageProxy: ImageProxy): Bitmap?{
        val image = imageProxy.image ?: return null
        val yBuffer = image.planes[ConstantGeneral.ZERO].buffer
        val uBuffer = image.planes[ConstantGeneral.ONE].buffer
        val vBuffer = image.planes[ConstantGeneral.TWO].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, ConstantGeneral.ZERO, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(ConstantGeneral.ZERO, ConstantGeneral.ZERO, yuvImage.width, yuvImage.height),
            ConstantGeneral.QUALITY, out
        )
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, ConstantGeneral.ZERO, imageBytes.size)
    }
}