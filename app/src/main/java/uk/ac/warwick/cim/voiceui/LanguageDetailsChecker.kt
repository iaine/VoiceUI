package uk.ac.warwick.cim.voiceui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log

class LanguageDetailsChecker : BroadcastReceiver() {
    private var supportedLanguages: MutableList<String?>? = null

    private var languagePreference: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        //tv_display = (TextView) findViewById(R.id.display);
        val results = getResultExtras(true)
        if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
            languagePreference =
                results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)
        }
        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            supportedLanguages =
                results.getStringArrayList(
                    RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES
                )
        }
        println(results.toString())
        //tv_display.append(results.toString());
        Log.i("VOICE L", results.toString())
    }
}
