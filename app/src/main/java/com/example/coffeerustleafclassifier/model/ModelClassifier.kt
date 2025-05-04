package com.example.coffeerustleafclassifier.model

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer


class ModelClassifier(context: Context) {

    private val interpreter: Interpreter

    init {
        val assetFileDescriptor = context.assets.openFd("coffee_leaf_classifier.tflite")
        val fileInputStream = assetFileDescriptor.createInputStream()
        val byteArray = fileInputStream.readBytes()
        val modelBuffer = ByteBuffer.allocateDirect(byteArray.size)
        modelBuffer.put(byteArray)
        modelBuffer.rewind()

        interpreter = Interpreter(modelBuffer)
    }

    fun classify(bitmap: Bitmap): String {
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val image = TensorImage(DataType.FLOAT32)
        image.load(resized)

        val inputBuffer = image.buffer

        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 2), DataType.FLOAT32)
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        val output = outputBuffer.floatArray
        val maxIdx = output.indices.maxByOrNull { output[it] } ?: -1

        return when (maxIdx) {
            0 -> "Healthy"
            1 -> "Rust-Affected"
            else -> "Unknown"
        }
    }
}
