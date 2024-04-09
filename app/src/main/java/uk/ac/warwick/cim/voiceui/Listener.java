package uk.ac.warwick.cim.voiceui;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class Listener implements RecognitionListener {

    private static String TAG = "LISTENER";

    //String.valueOf(fileName)
    protected File fName;


    private Sonification sonification = new Sonification();

    protected Listener(File fName1) {
        fName = fName1;
    }

    public void onReadyForSpeech(Bundle params)
    {
        System.out.println ( params.describeContents() );
        Log.d(TAG, "onReadyForSpeech");
    }
    public void onBeginningOfSpeech()
    {
        Log.d(TAG, "onBeginningOfSpeech");
        Log.i(TAG, "onBeginningOfSpeech: " + System.currentTimeMillis());
    }
    public void onRmsChanged(float rmsdB)
    {
        Log.i(TAG, "onRmsChanged: " + System.currentTimeMillis() + " "  + String.valueOf(rmsdB) );
        FileWriter fileWriter = new FileWriter(fName);
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ","  + String.valueOf(rmsdB) + "\n");
        if (sonification.getAudioState()) {
            //@todo
        }

        if (sonification.getRmsState()) {
            //@todo
        }

    }
    public void onBufferReceived(byte[] buffer)
    {
        Log.i(TAG, "onBufferReceived" + new String(buffer, StandardCharsets.UTF_16BE) );
        Log.d(TAG, "onBufferReceived");
    }
    public void onEndOfSpeech()
    {
        Log.d(TAG, "onEndofSpeech");
        Log.i(TAG, "onEndOfSpeech: " + System.currentTimeMillis());

    }
    public void onError(int error)
    {
        Log.d(TAG,  "error " +  error);
        //mText.setText("error " + error);
    }
    public void onResults(Bundle results) {

        String obj = "";
        for(String key : results.keySet()){
            obj += results.get(key);   //later parse it as per your required type
        }

        String str = new String();
        Log.d(TAG, "onResults " + obj);
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++) {
            Log.d(TAG, "result " + data.get(i));
            str += data.get(i);
        }
        //let's get the confidence
        float[] confidences = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
        String c = "";
        for (float c1: confidences){
            c += String.valueOf(c1) + " ,";
        }
        Log.i(TAG, "Confidence: " + c);

        String language = results.getString(SpeechRecognizer.DETECTED_LANGUAGE);
        int confidence = results.getInt(SpeechRecognizer.LANGUAGE_DETECTION_CONFIDENCE_LEVEL);
        int lang_switch = results.getInt(SpeechRecognizer.LANGUAGE_SWITCH_RESULT);
        ArrayList res_alternatives = results.getStringArrayList(SpeechRecognizer.RESULTS_ALTERNATIVES);
        ArrayList alternative = results.getStringArrayList(SpeechRecognizer.TOP_LOCALE_ALTERNATIVES);

        String message = language + " " + String.valueOf(confidence) + " " + lang_switch + " "
                + res_alternatives + " " + alternative;
        Log.i(TAG, message);
        //mText.setText("results: "+String.valueOf(data.size()));
    }
    public void onPartialResults(Bundle partialResults)
    {
        String str = new String();
        System.out.println ( "onPartialResults" + partialResults.describeContents() );
        ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d(TAG, "onPartialResults");
        for (int i = 0; i < partialResults.size(); i++) {
            Log.d(TAG, "result " + data.get(i));
            str += data.get(i);
        }

        String language = partialResults.getString(SpeechRecognizer.DETECTED_LANGUAGE);
        int confidence = partialResults.getInt(SpeechRecognizer.LANGUAGE_DETECTION_CONFIDENCE_LEVEL);
        int lang_switch = partialResults.getInt(SpeechRecognizer.LANGUAGE_SWITCH_RESULT);
        ArrayList res_alternatives = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_ALTERNATIVES);
        ArrayList alternative = partialResults.getStringArrayList(SpeechRecognizer.TOP_LOCALE_ALTERNATIVES);

        String message = language + " " + String.valueOf(confidence) + " " + lang_switch + " "
                + res_alternatives + " " + alternative;
        Log.i(TAG, message);

        String obj = "";
        for(String key : partialResults.keySet()){
            obj += partialResults.getString(key);   //later parse it as per your required type
        }
        Log.d(TAG, "onPartialResults " + obj);
    }
    public void onEvent(int eventType, Bundle params)
    {
        String obj = "";
        for(String key : params.keySet()){
            obj += params.getString(key);   //later parse it as per your required type
        }
        Log.d(TAG, "onEvent " + obj);
        System.out.println ( "onEvent " + params.describeContents() );
        Log.d(TAG, "onEvent " + eventType);
    }
}
