package uk.ac.warwick.cim.voiceui

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import java.io.File
import java.nio.charset.StandardCharsets

internal class Listener(var fName: File) :
    RecognitionListener {
    private val fileWriter: FileWriter = FileWriter()

    override fun onReadyForSpeech(params: Bundle) {
        println(params.describeContents())
        Log.d(TAG, "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech")
        Log.i(TAG, "onBeginningOfSpeech: " + System.currentTimeMillis())
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", onBeginningOfSpeech \n"
        )
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.i(TAG, "onRmsChanged: " + System.currentTimeMillis() + " " + rmsdB.toString())

        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + "," + rmsdB.toString() + "\n"
        )
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.i(TAG, "onBufferReceived" + String(buffer!!, StandardCharsets.UTF_16BE))
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", onBufferReceived \n"
        )
    }

    override fun onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech: " + System.currentTimeMillis())
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", onEndOfSpeech \n"
        )
    }

    override fun onError(error: Int) {
        Log.d(TAG, "error $error")
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + "," + error + " onError \n"
        )
    }

    override fun onResults(results: Bundle) {
        var obj: String? = ""
        for (key in results.keySet()) {
            obj += results[key] //later parse it as per your required type
        }

        var str: String? = ""
        val data: ArrayList<*>? = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        for (i in data!!.indices) {
            str += data[i]
        }
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", Text: " + str + " \n"
        )
        //let's get the confidence
        val confidences = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
        var c = ""
        for (c1 in confidences!!) {
            c += "$c1 ,"
        }

        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", Confidence: " + c + " \n"
        )

        val language = results.getString(SpeechRecognizer.DETECTED_LANGUAGE)
        val confidence = results.getInt(SpeechRecognizer.LANGUAGE_DETECTION_CONFIDENCE_LEVEL)
        val langSwitch = results.getInt(SpeechRecognizer.LANGUAGE_SWITCH_RESULT)
        val resAlternatives: ArrayList<*>? =
            results.getStringArrayList(SpeechRecognizer.RESULTS_ALTERNATIVES)
        val alternative: ArrayList<*>? =
            results.getStringArrayList(SpeechRecognizer.TOP_LOCALE_ALTERNATIVES)

        val message = (language + " " + confidence.toString() + " " + langSwitch + " "
                + resAlternatives + " " + alternative)

        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", Message: " + message + "\n"
        )
    }

    override fun onPartialResults(partialResults: Bundle) {
        var str: String? = ""

        val data: ArrayList<*>? =
            partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        for (i in 0..<partialResults.size()) {
            str += data!![i]
        }

        val language = partialResults.getString(SpeechRecognizer.DETECTED_LANGUAGE)
        val confidence = partialResults.getInt(SpeechRecognizer.LANGUAGE_DETECTION_CONFIDENCE_LEVEL)
        val langSwitch = partialResults.getInt(SpeechRecognizer.LANGUAGE_SWITCH_RESULT)
        val resAlternatives: ArrayList<*>? =
            partialResults.getStringArrayList(SpeechRecognizer.RESULTS_ALTERNATIVES)
        val alternative: ArrayList<*>? =
            partialResults.getStringArrayList(SpeechRecognizer.TOP_LOCALE_ALTERNATIVES)

        val message = (language + " " + confidence.toString() + " " + langSwitch + " "
                + resAlternatives + " " + alternative)

        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + "," + message + "\n"
        )
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", Partial: " + str + " \n"
        )
    }

    override fun onEvent(eventType: Int, params: Bundle) {
        var obj: String? = ""
        for (key in params.keySet()) {
            obj += params.getString(key) //later parse it as per your required type
        }
        fileWriter.writeFile(
            fName.toString(),
            System.currentTimeMillis().toString() + ", Event: " + obj + "\n"
        )
    }

    companion object {
        private const val TAG = "LISTENER"
    }
}
