package com.mentorsschool.logindemoapp;

/**
 * Created by joshu on 4/20/2018.
 */

public class User {

    public String name;
    public String type;
    public int lowLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(int lowLevel) {
        this.lowLevel = lowLevel;
    }

    public int getHighLevel() {
        return highLevel;
    }

    public void setHighLevel(int highLevel) {
        this.highLevel = highLevel;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

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
