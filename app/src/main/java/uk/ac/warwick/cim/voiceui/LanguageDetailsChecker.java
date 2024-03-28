package uk.ac.warwick.cim.voiceui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LanguageDetailsChecker extends BroadcastReceiver {
    private List<String> supportedLanguages;

    private String languagePreference;

    private TextView tv_display;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //tv_display = (TextView) findViewById(R.id.display);
        Bundle results = getResultExtras(true);
        if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE))
        {
            languagePreference =
                    results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
        }
        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES))
        {
            supportedLanguages =
                    results.getStringArrayList(
                            RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
        }
        System.out.println(results.toString());
        //tv_display.append(results.toString());
        Log.i("VOICE L",results.toString());

    }
}
