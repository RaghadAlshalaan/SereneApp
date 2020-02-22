package com.ksu.serene.Model;

public class VoiceDraft extends Draft {

    private String audio;
    private String timestamp;

    public VoiceDraft(String id, String title, String date, String audio, String timestamp) {
        super(id, title, date);
        this.audio = audio;
        this.timestamp = timestamp;
    }

    public String getAudio() {
        return audio;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
