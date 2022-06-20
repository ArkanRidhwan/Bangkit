package com.inc.vr.corps.storymaps.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object Helper {
    private const val FORMAT_FILE = "dd-MM-yyyy"

    private val timeFormat : String = SimpleDateFormat(FORMAT_FILE, Locale.US).format(System.currentTimeMillis())

    fun createFileTemp(context: Context) : File {
        val storageDirectory : File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeFormat, ".jpg", storageDirectory)
    }

    fun uriToFile(selectedImage : Uri, context: Context) : File{
        val contentResolver : ContentResolver = context.contentResolver
        val file = createFileTemp(context)
        val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
        val outputStream : OutputStream = FileOutputStream(file)

        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return file
    }

    fun reduceImageSize(file : File) : File {
        val imageBitmap = BitmapFactory.decodeFile(file.path)
        var qualityCompress = 100
        var streamLength : Int
        do {
            val bitmapStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityCompress, bitmapStream)
            val bitmapPicByteArray = bitmapStream.toByteArray()
            streamLength = bitmapPicByteArray.size
            qualityCompress -= 5

        } while (streamLength > 1000000)
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, qualityCompress, FileOutputStream(file))
        return file
    }
}