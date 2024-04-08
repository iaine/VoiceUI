package uk.ac.warwick.cim.voiceui;

public class Sonification {

    private boolean rms = false;

    private boolean audio = false;

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
