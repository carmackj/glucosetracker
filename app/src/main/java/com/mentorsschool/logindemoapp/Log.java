package com.mentorsschool.logindemoapp;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Log {
    public String uid;
    public String level;
    public String strDate;
    public String strTime;

    public Log(String uid, String level)
    {
        this.uid = uid;
        this.level = level;

        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("hh:mm a");

        LocalDateTime now = LocalDateTime.now();

        strDate = dateformat.format(now);
        strTime = timeformat.format(now);
    }
}
