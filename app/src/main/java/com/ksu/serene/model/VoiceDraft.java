package com.ksu.serene.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class VoiceDraft extends Draft {

    private String audio;
    private String timestamp;

    public static final Comparator<VoiceDraft> BY_NAME_ALPHABETICAL = new Comparator<VoiceDraft>() {
        @Override
        public int compare(VoiceDraft voicDraft, VoiceDraft t1) {
            Date c1 = voicDraft.setDate(voicDraft.getDate());
            Date tD1 = t1.setDate(t1.getDate());
            //check if c1 is end but d1 if not end first
            //equal call the time stamp
            if (c1.compareTo(tD1) == 0) {
                //call time stamp
                return voicDraft.setTime(voicDraft.getTimestamp()).compareTo(t1.setTime(t1.getTimestamp()));
            }
            return c1.compareTo(tD1);
        }
    };

    public VoiceDraft(String id, String title, String date, String audio,String timestamp) {
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

    public Date setDate(String startDate1) {
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
