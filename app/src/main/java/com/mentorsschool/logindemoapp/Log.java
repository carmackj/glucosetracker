package com.mentorsschool.logindemoapp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.*;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.*;

public class Log {
    public String level;
    public String email;
    public String time1;
    public String time2;
    public String timeStr;
    public String dateStr;

    public Log()
    {
        //Empty Constructor for getValue
    }

    public Log(String log, String email, String time1, String time2) //Main Constructor
    {
        this.level = log;
        this.email = email;
        this.time1 = time1;
        this.time2 = time2;

        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyy");
        DateFormat dfTime = new SimpleDateFormat("hh:mm:ss a");
        Date date = new Date();

        this.dateStr = dfDate.format(date);
        this.timeStr = dfTime.format(date);
    }

    public String getLevel(){ return level;}
}
