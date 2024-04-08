package uk.ac.warwick.cim.voiceui;

import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Record {

    private static final String LOG_TAG = "VOICe";
    private MediaRecorder mediaRecorder;



    public Record(MediaRecorder mr) {
        this.mediaRecorder = mr;
    }


    public void startRecordAudio (File fileName) {
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.mediaRecorder.setOutputFile(fileName);
        }


        try {
            this.mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        this.mediaRecorder.start();
    }

    public void stopRecordAudio() {

        if (this.mediaRecorder != null) {
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
            this.mediaRecorder = null;
        }
    }
}
