package uk.ac.warwick.cim.voiceui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;
import java.lang.reflect.*;

public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer sr;
    private static final int SPEECH_REQUEST_CODE = 0;

    private Sonification sonification = new Sonification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String requiredPermission = Manifest.permission.RECORD_AUDIO;

        if (checkCallingOrSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{requiredPermission}, 101);
        }
        File lFile = new File(this.getExternalFilesDir(null), "listener" + System.currentTimeMillis() + ".txt");

        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new Listener(lFile));
    }

    public void speechText (View v) {
        displaySpeechRecognizer(v);
    }

    public void speechRecognise (View v) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
        sr.startListening(intent);
    }

    public void displayLanguages(View v) {
        Intent detailsIntent =  new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        sendOrderedBroadcast(
                detailsIntent, null, new LanguageDetailsChecker(), null,
                Activity.RESULT_OK, null, null);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //use reflection to look at the class
        Object inten = intent;
        String code = inten.getClass().toString() + "\n";
        Method[] m = inten.getClass().getDeclaredMethods();
        for (Method method : m) code += method;
        Log.i("VOICE", code);
        File codeFile = new File(this.getExternalFilesDir(null), "code.txt");
        FileWriter fWrite = new FileWriter(codeFile);
        fWrite.writeFile(codeFile.toString(), code);
        // This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.i("VOICE", results.toString());
            float[] confidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
            Log.i("VOICE", confidences.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void rmsGraph (View v) {
        sonification.setRmsState();
    }

    public void audioGraph (View v) {
        sonification.setAudioState();
    }

}