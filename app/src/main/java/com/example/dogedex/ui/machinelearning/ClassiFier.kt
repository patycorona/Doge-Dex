package com.example.dogedex.ui.machinelearning

import android.graphics.Bitmap
import com.example.dogedex.domain.model.ConstantGeneral.Companion.CONFIDENCE_100
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DECUANTIZE_SCALE
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DECUANTIZE_SCALE_1
import com.example.dogedex.domain.model.ConstantGeneral.Companion.DECUANTIZE_SCALE_ZERO
import com.example.dogedex.domain.model.ConstantGeneral.Companion.FIVE
import com.example.dogedex.domain.model.ConstantGeneral.Companion.MAX_RECOGNITION_DOG_RESULTS
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ONE
import com.example.dogedex.domain.model.ConstantGeneral.Companion.TWO
import com.example.dogedex.domain.model.ConstantGeneral.Companion.ZERO
import com.example.dogedex.domain.model.DogRecognition
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.DequantizeOp
import org.tensorflow.lite.support.image.TensorImage
import java.nio.MappedByteBuffer
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.*

class ClassiFier(tfLiteModel: MappedByteBuffer, private val labels: List<String>) {
    /**
     * Image size along the x axis.
     */
    private val imageSizeX: Int

    /**
     * Image size along the y axis.
     */
    private val imageSizeY: Int

    /** Optional GPU delegate for acceleration.  */
    /**
     * An instance of the driver class to run model inference with Tensorflow Lite.
     */
    private var tfLite: Interpreter

    /**
     * Input image TensorBuffer.
     */
    private var inputImageBuffer: TensorImage

    /**
     * Output probability TensorBuffer.
     */
    private val outputProbabilityBuffer: TensorBuffer

    /**
     * Processor to apply post processing of the output probability.
     */
    private val tensorProcessor: TensorProcessor

    init {
        val tfLiteOptions = Interpreter.Options()
        tfLiteOptions.setNumThreads(FIVE)
        tfLite = Interpreter(tfLiteModel, tfLiteOptions)

        val imageTensorIndex = ZERO
        val imageShape = tfLite.getInputTensor(imageTensorIndex).shape()
        imageSizeY = imageShape[ONE]
        imageSizeX = imageShape[TWO]
        val imageDataType = tfLite.getInputTensor(imageTensorIndex).dataType()
        val probabilityTensorIndex = ZERO
        val probabilityShape =
            tfLite.getOutputTensor(probabilityTensorIndex).shape()
        val probabilityDataType = tfLite.getOutputTensor(probabilityTensorIndex).dataType()

        inputImageBuffer = TensorImage(imageDataType)

        outputProbabilityBuffer = TensorBuffer.createFixedSize(
            probabilityShape,
            probabilityDataType
        )

        tensorProcessor = TensorProcessor.Builder().add(DequantizeOp(DECUANTIZE_SCALE_ZERO,
            DECUANTIZE_SCALE_1 / DECUANTIZE_SCALE)).build()
    }

    fun recognizeImage(bitmap: Bitmap): List<DogRecognition> {
        inputImageBuffer = loadImage(bitmap)
        val rewoundOutputBuffer = outputProbabilityBuffer.buffer.rewind()
        tfLite.run(inputImageBuffer.buffer, rewoundOutputBuffer)

        val labeledProbability =
            TensorLabel(labels, tensorProcessor.process(outputProbabilityBuffer)).mapWithFloatValue

        return getTopKProbability(labeledProbability)
    }

    private fun loadImage(bitmap: Bitmap): TensorImage {

        inputImageBuffer.load(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .build()
        return imageProcessor.process(inputImageBuffer)
    }

    private fun getTopKProbability(labelProb: Map<String, Float>): List<DogRecognition> {
        val priorityQueue =
            PriorityQueue(MAX_RECOGNITION_DOG_RESULTS) {
                    lhs: DogRecognition, rhs: DogRecognition ->
                (rhs.confidence).compareTo(lhs.confidence)
            }

        for ((key, value) in labelProb) {
            priorityQueue.add(DogRecognition(key, value * CONFIDENCE_100))
        }

        val recognitions = mutableListOf<DogRecognition>()
        val recognitionsSize = minOf(priorityQueue.size, MAX_RECOGNITION_DOG_RESULTS)
        for (i in ZERO until recognitionsSize) {
            recognitions.add(priorityQueue.poll()!!)
        }

        return recognitions
    }
}