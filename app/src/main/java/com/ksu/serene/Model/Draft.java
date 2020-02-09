package com.ksu.serene.Model;
import java.util.Date;
public class Draft {
    private int id;
    private String title;
    private Date date;

    public Draft(int id, String title, Date date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

}
