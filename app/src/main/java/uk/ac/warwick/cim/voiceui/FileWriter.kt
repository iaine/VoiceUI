package uk.ac.warwick.cim.voiceui

import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * Class to write data to file asynchronously
 */
class FileWriter(//public class FileConnection {
    //var fileName: File?
) {
    fun writeFile(fileName: String, params: String) {
        val outputStream: FileOutputStream?

        try {
            val file = File(fileName)
            if (!file.exists()) {
                val file = file.createNewFile()
                if (!file) {
                    throw Exception("Could not create $fileName")
                }
            }
            outputStream = FileOutputStream(file, true)
            outputStream.write(params.toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            Log.i("FILE", e.toString())
        }
    }
}

