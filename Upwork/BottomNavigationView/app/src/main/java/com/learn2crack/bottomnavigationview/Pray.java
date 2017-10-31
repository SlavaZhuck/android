package com.learn2crack.bottomnavigationview;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class Pray {
    private String mName;
    private Calendar mDate;

    public Pray() {

    }

    @Override
    public String toString() {
        return mName + " " + mDate.get(Calendar.HOUR_OF_DAY) + ":" + mDate.get(Calendar.MINUTE);
    }

    public Pray(String name, Calendar date) {
        mName = name;
        mDate = date;
    }

    public Pray(String name, String date, boolean today) {
        String arrPray[] = new String[2];
         String arrPrayTime;
        long arrPrayTimeH ;
        long arrPrayTimeM ;
        Calendar currentCalendarDate = Calendar.getInstance();
        long millisFromStartOfDay = Calendar.getInstance().getTimeInMillis() -
                (currentCalendarDate.get(Calendar.HOUR_OF_DAY) * 1000l * 60l * 60l + currentCalendarDate.get(Calendar.MINUTE) * 1000l * 60l + currentCalendarDate.get(Calendar.SECOND) * 1000l + currentCalendarDate.get(Calendar.MILLISECOND));

       // arrPray = date.split(" ", 2);
        arrPrayTime = date;
        arrPrayTimeH = Integer.parseInt(arrPrayTime.substring(0, 2));
        arrPrayTimeM = Integer.parseInt(arrPrayTime.substring(3, 5));
        currentCalendarDate = Calendar.getInstance();
        currentCalendarDate.set(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                (int) arrPrayTimeH,
                (int) arrPrayTimeM,
                0);
        if(today == true) {
            currentCalendarDate.setTimeInMillis(millisFromStartOfDay + arrPrayTimeH * 1000l * 60l * 60l + arrPrayTimeM * 1000l * 60l);
        } else {
            currentCalendarDate.setTimeInMillis(millisFromStartOfDay + arrPrayTimeH * 1000l * 60l * 60l + arrPrayTimeM * 1000l * 60l + 86400000l);
        }
        mName = name;
        mDate = currentCalendarDate;
    }

    public String getName() {
        return mName;
    }
    public String getTime() {
        
        return String.format("%02d:%02d",mDate.get(Calendar.HOUR_OF_DAY),mDate.get(Calendar.MINUTE));
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
