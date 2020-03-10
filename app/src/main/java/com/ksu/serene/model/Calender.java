package com.ksu.serene.model;

public class Calender {
    //private Event [] events;
    private Reminder [] reminders;

    //Event [] events ,
    public Calender(Reminder [] reminders) {
        //this.events = events;
        this.reminders = reminders;
    }

    public Reminder[] getAllReminders () {
        return reminders;
    }
}
