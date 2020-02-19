package com.ksu.serene.Model;
import java.util.Date;
public class Draft {
    private String id;
    private String title;
    private Date date;

    public Draft(String id, String title, Date date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

}
