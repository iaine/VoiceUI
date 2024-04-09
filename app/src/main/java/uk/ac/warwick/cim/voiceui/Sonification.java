package uk.ac.warwick.cim.voiceui;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Sonification {

    private boolean rms = false;

    private boolean audio = false;

    private final String rate;
    private final double[] mSound = new double[4410];
    private short[] mBuffer;

    private final int toneduration = 44100;

    private File audioFile;

    HashMap<String, short[]> tones = new HashMap<>();
    private MediaPlayer mediaPlayer;
    public Sonification(Context ctx, String fName) {
        AudioManager audioManager;
        audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        this.rate = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        this.mBuffer = new short[toneduration];
        tones.put("A3", this.createTone(220.0, toneduration));
        tones.put("C4", this.createTone(261.63 , toneduration));
        audioFile = new File(ctx.getExternalFilesDir(null), fName);
    }

    /**
     * Method to create the tone and buffer on initialisation
     * @param frequency frequency of the note in Hertz, not MIDI
     * @param duration  the duration of the tone
     */
    private short[] createTone(double frequency, int duration) {
        mBuffer = new short[duration];
        // Sine wave
        for (int i = 0; i < this.mSound.length; i++) {
            this.mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            this.mBuffer[i] = (short) (this.mSound[i]*Short.MAX_VALUE);
        }
        return mBuffer;
    }


    public short[] getTone (String note) {
        return tones.get(note);
    }

    /**
     * Method to play the tone when a signal is found.
     * For now, limited to Geiger counter style. Really ought to make it
     * more aesthetic.
     */
    public void playAudioGraph(float rmsChange) {
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(Integer.parseInt(this.rate))
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build();

        AudioTrack mAudioTrack = new AudioTrack(audioAttributes,
                audioFormat,
                mBufferSize*2,
                AudioTrack.MODE_STREAM,
                0);

        mAudioTrack.play();

        short[] lBuffer = tones.get("A3");
        mAudioTrack.write(lBuffer, 0, this.mSound.length);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float dist = (rmsChange > 0.00f) ? rmsChange : 1.75f;
            mAudioTrack.setVolume(dist);
        }
        mAudioTrack.stop();
        mAudioTrack.release();
    }


    public void playRmsAudio() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFile.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ioe) {
            Log.d("AUDIO", ioe.toString());
        }
    }




    public boolean getRmsState () {
        return rms;
    }

    public boolean getAudioState () {
        return audio;
    }

    public void setRmsState () {
        rms = (rms)? false: true;
    }

    public void setAudioState () {
        audio = (audio)? false: true;
    }
}
