package com.ksu.serene.Model;
import java.util.Date;
public class TextDraft extends Draft {
    private String message;
    private String timestap;

    public TextDraft(String id, String title, Date date , String message , String timestap) {
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
