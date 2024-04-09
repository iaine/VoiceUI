package uk.ac.warwick.cim.voiceui;

import android.content.Context;
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

    protected File fName;

    private FileWriter fileWriter;

    private Sonification sonification;

    protected Listener(File fName1, String audio, Context ctx) {
        fName = fName1;
        fileWriter = new FileWriter(fName);
        sonification = new Sonification(ctx, audio);
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
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", onBeginningOfSpeech \n");

    }
    public void onRmsChanged(float rmsdB)
    {
        Log.i(TAG, "onRmsChanged: " + System.currentTimeMillis() + " "  + String.valueOf(rmsdB) );

        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ","  + String.valueOf(rmsdB) + "\n");
        if (sonification.getAudioState()) {
            sonification.playAudioGraph(rmsdB);
        }

        if (sonification.getRmsState()) {
            sonification.playRmsAudio();
        }

    }
    public void onBufferReceived(byte[] buffer)
    {
        Log.i(TAG, "onBufferReceived" + new String(buffer, StandardCharsets.UTF_16BE) );
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", onBufferReceived \n");
    }
    public void onEndOfSpeech()
    {
        Log.i(TAG, "onEndOfSpeech: " + System.currentTimeMillis());
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", onEndOfSpeech \n");

    }
    public void onError(int error)
    {
        Log.d(TAG,  "error " +  error);
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + "," + error + " onError \n");
    }
    public void onResults(Bundle results) {

        String obj = "";
        for(String key : results.keySet()){
            obj += results.get(key);   //later parse it as per your required type
        }

        String str = new String();
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++) {
            str += data.get(i);
        }
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", Text: " + str +" \n");
        //let's get the confidence
        float[] confidences = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
        String c = "";
        for (float c1: confidences){
            c += String.valueOf(c1) + " ,";
        }

        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", Confidence: " + c +" \n");

        String language = results.getString(SpeechRecognizer.DETECTED_LANGUAGE);
        int confidence = results.getInt(SpeechRecognizer.LANGUAGE_DETECTION_CONFIDENCE_LEVEL);
        int lang_switch = results.getInt(SpeechRecognizer.LANGUAGE_SWITCH_RESULT);
        ArrayList res_alternatives = results.getStringArrayList(SpeechRecognizer.RESULTS_ALTERNATIVES);
        ArrayList alternative = results.getStringArrayList(SpeechRecognizer.TOP_LOCALE_ALTERNATIVES);

        String message = language + " " + String.valueOf(confidence) + " " + lang_switch + " "
                + res_alternatives + " " + alternative;

        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", Message: "+ message +"\n");
    }
    public void onPartialResults(Bundle partialResults)
    {
        String str = new String();

        ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < partialResults.size(); i++) {
            str += data.get(i);
        }

        String language = partialResults.getString(SpeechRecognizer.DETECTED_LANGUAGE);
        int confidence = partialResults.getInt(SpeechRecognizer.LANGUAGE_DETECTION_CONFIDENCE_LEVEL);
        int lang_switch = partialResults.getInt(SpeechRecognizer.LANGUAGE_SWITCH_RESULT);
        ArrayList res_alternatives = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_ALTERNATIVES);
        ArrayList alternative = partialResults.getStringArrayList(SpeechRecognizer.TOP_LOCALE_ALTERNATIVES);

        String message = language + " " + String.valueOf(confidence) + " " + lang_switch + " "
                + res_alternatives + " " + alternative;

        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ","+ message +"\n");
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", Partial: " + str +" \n");
    }
    public void onEvent(int eventType, Bundle params)
    {
        String obj = "";
        for(String key : params.keySet()){
            obj += params.getString(key);   //later parse it as per your required type
        }
        fileWriter.writeFile(fName.toString(), System.currentTimeMillis() + ", Event: "+ obj +"\n");
    }
}
