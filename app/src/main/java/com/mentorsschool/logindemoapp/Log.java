package com.mentorsschool.logindemoapp;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.*;

public class Log {
    //public String uid;
    public String log;
    public String email;
    //public String strTime;

    public Log()
    {
        //Empty Constructor for getValue
    }

    public Log(String log, String email) //Main Constructor
    {
        this.log = log;
        this.email = email;
    }

    /*
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("level", level);
        return result;
    }
    */

    public String getLog(){ return log;}

}
