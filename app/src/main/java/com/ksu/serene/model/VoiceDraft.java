package com.ksu.serene.model;

public class VoiceDraft extends Draft {

    private String audio;
    private String timestamp;

    public VoiceDraft(String id, String title, String date, String audio) {
        super(id, title, date);
        this.audio = audio;
        this.timestamp = date;
    }

    public String getAudio() {
        return audio;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
