package com.ksu.serene.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class TextDraft extends Draft {
    private String message;
    private String timestap;

    public static final Comparator<TextDraft> BY_NAME_ALPHABETICAL = new Comparator<TextDraft>() {
        @Override
        public int compare(TextDraft textDraft, TextDraft t1) {
            Date c1 = textDraft.setDate(textDraft.getDate());
            Date tD1 = t1.setDate(t1.getDate());
            //check if c1 is end but d1 if not end first
            //equal call the time stamp
            if (c1.compareTo(tD1) == 0) {
                //call time stamp
                if ( textDraft.setTime(textDraft.getTimestap()).compareTo(t1.setTime(t1.getTimestap())) == 0){
                    //by title
                    textDraft.getTitle().compareTo(t1.getTitle());
                }
                return textDraft.setTime(textDraft.getTimestap()).compareTo(t1.setTime(t1.getTimestap()));
            }
            return c1.compareTo(tD1);
        }
    };

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

    public  Date setDate(String startDate1) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = simpleDateFormat.parse(startDate1);
            return date;
        }catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public  Date setTime(String startDate1) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm", Locale.UK);
        try {
            date = simpleDateFormat.parse(startDate1);
            return date;
        }catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


}
