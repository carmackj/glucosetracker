package com.mentorsschool.logindemoapp;

/**
 * Created by joshu on 4/20/2018.
 */

public class User {

    public String name;
    public String type;
    public int lowLevel;
    public int highLevel;
    public String doctor;

    public User ()
    {

    }

    public User (String name, String type, int lowLevel, int highLevel, String doctor)
    {
        this.name = name;
        this.type = type;
        this.lowLevel = lowLevel;
        this.highLevel = highLevel;
        this.doctor = doctor;
    }
}
