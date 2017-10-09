package com.learn2crack.bottomnavigationview;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class Pray {
    private String mName;
    private Calendar mDate;

    @Override
    public String toString() {
        return mName;
    }

    public Pray(String name, Calendar date) {
        mName   = name;
        mDate   = date;

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(Calendar date) {
        mDate = date;
    }
}
