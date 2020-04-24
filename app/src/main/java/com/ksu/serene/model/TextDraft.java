package com.ksu.serene.model;

public class TextDraft extends Draft {
    private String message;
    private String timestap;

    public TextDraft(String id, String title, String date , String message,String timestap) {
        super(id, title, date);
        this.message = message;
        this.timestap = timestap;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestap() {
        return timestap;
    }


}
