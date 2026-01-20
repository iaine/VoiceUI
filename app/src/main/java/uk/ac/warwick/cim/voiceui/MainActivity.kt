package uk.ac.warwick.cim.voiceui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity : AppCompatActivity() {
    private var sr: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requiredPermission = Manifest.permission.RECORD_AUDIO

        if (checkCallingOrSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(requiredPermission), 101)
        }
        val lFile =
            File(this.getExternalFilesDir(null), "listener" + System.currentTimeMillis() + ".txt")

        sr = SpeechRecognizer.createSpeechRecognizer(this)
        sr!!.setRecognitionListener(Listener(lFile))
    }

    fun speechText(v: View?) {
        displaySpeechRecognizer()
    }

    fun speechRecognise(v: View?) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test")
        sr!!.startListening(intent)
    }

    fun displayLanguages(v: View?) {
        val detailsIntent = Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS)
        sendOrderedBroadcast(
            detailsIntent, null, LanguageDetailsChecker(), null,
            RESULT_OK, null, null
        )
    }

    // Create an intent that can start the Speech Recognizer activity
    fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        //use reflection to look at the class
        val inten: Any = intent
        var code: String? = inten.javaClass.toString() + "\n"
        val m = inten.javaClass.declaredMethods
        for (method in m) code += method
        Log.i("VOICE", code!!)
        val codeFile = File(this.getExternalFilesDir(null), "code.txt")
        val fWrite = FileWriter()
        fWrite.writeFile(codeFile.toString(), code)
        // This starts the activity and populates the intent with the speech text.
        this.startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results: MutableList<String?>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            Log.i("VOICE", results.toString())
            val confidences = data?.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES)
            Log.i("VOICE", confidences.toString())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    companion object {
        private const val SPEECH_REQUEST_CODE = 0
    }
}