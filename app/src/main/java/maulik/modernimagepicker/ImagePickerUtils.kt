package maulik.modernimagepicker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.InputStream

/**
 * Saves bitmap as the image file
 *
 * Note: This is a IO operation, it's not recommended to call this from main thread
 *
 * @param bitmap input image bitmap
 * @param format format of the image file - PNG/JPEG
 * @param quality image quality between 1 to 100
 */
fun File.writeBitmap(
    bitmap: Bitmap,
    format: Bitmap.CompressFormat, quality: Int
) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

/**
 * Gets rotation degree of the image, i.e 0, 90, 180, 270
 *
 * Note: This is a IO operation, it's not recommended to call this from main thread
 *
 * @param file input image file
 * @return rotation degree
 */
fun getRotationFromFile(file: File): Int {
    try {
        val exifInterface = ExifInterface(file)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return 0
    }
}

/**
 * Corrects the rotation of the image file
 *
 * Note: This is a IO operation, it's not recommended to call this from main thread
 *
 * Sometimes the raw image we get from camera doesn't have the correct rotation
 * The actual rotation information is saved in image metadata.
 * So, we use the metadata of the image and rotate the image and save it again to same file
 *
 */
fun File.correctRotation() {
    val rotationDegree = getRotationFromFile(this)
    if (rotationDegree != 0) {
        val matrix = Matrix()
        matrix.setRotate(rotationDegree.toFloat())
        val originalBitmap = BitmapFactory.decodeFile(this.absolutePath)
        val rotatedBitmap = Bitmap.createBitmap(
            originalBitmap,
            0,
            0,
            originalBitmap.width,
            originalBitmap.height,
            matrix,
            true
        )
        writeBitmap(rotatedBitmap, Bitmap.CompressFormat.JPEG, 100)
    }
}

/**
 * Creates an empty image file in private storage directory of the app
 * The function will create file under Android -> data -> package -> Pictures
 *
 * @return empty image file
 */
fun Context.getTempFile(): File {
    val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("tempImg", "", dir).also {
        it.deleteOnExit()
    }
}

/**
 * Copies input stream to file
 *
 * @param inputStream data source input stream, i.e. image uri
 */
fun File.copyInputStreamToFile(inputStream: InputStream?) {
    if (inputStream != null) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }
}
