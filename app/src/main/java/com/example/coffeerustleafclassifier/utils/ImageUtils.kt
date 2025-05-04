package com.example.coffeerustleafclassifier.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

fun uriToBitmap(context: Context, uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT < 28) {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

fun classifyImage(context: Context, bitmap: Bitmap): String {
    val modelBuffer = loadModelFile(context, "coffee_leaf_classifier.tflite")
    val model = Interpreter(modelBuffer)

    val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
    val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }

    for (x in 0 until 224) {
        for (y in 0 until 224) {
            val pixel = resized.getPixel(x, y)
            input[0][x][y][0] = Color.red(pixel) / 255f
            input[0][x][y][1] = Color.green(pixel) / 255f
            input[0][x][y][2] = Color.blue(pixel) / 255f
        }
    }

    val output = Array(1) { FloatArray(2) } // Change to match your model
    model.run(input, output)

    val predictionIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
    return when (predictionIndex) {
        0 -> "Healthy"
        1 -> "Leaf Rust"
        else -> "Unknown"
    }
}

fun loadModelFile(context: Context, modelName: String): ByteBuffer {
    val fileDescriptor = context.assets.openFd(modelName)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

