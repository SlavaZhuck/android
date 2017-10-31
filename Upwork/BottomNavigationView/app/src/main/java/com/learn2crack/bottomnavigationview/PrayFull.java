package com.learn2crack.bottomnavigationview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class PrayFull {
    //private static ArrayList<Pray> mPrayList = new ArrayList<Pray>();
    public static Pray mClosestPray;
    public static long timeToNext;
    public static long currentTimeMils;

    PrayFull() {
    }

    public static String getTimeToNext() {
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        if(mClosestPray!=null) {
            long minutes = ((mClosestPray.getDate().getTimeInMillis() - currentTimeMils) / (1000 * 60)) % 60;
            long hours = ((mClosestPray.getDate().getTimeInMillis() - currentTimeMils) / (1000 * 60 * 60)) % 24;
            // String formatted = String.format("H", hours);
            String elapsedTimeS = new String("(- " + String.format("%02d:%02d", hours, minutes) + ")");
            return elapsedTimeS;
        }else{
            return "99:99";
        }
    }

    public static ArrayList<Pray> getActualPrays(ArrayList<Pray> prayList) {
        // Calendar currCal = ProgressFragment.mCalendarCurrentTime;
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        ArrayList<Pray> actualPrays = new ArrayList<Pray>();
        for (int i = 0; i < prayList.size(); i++) {
            long time = prayList.get(i).getDate().getTimeInMillis();
            if (time > currentTimeMils) {
                actualPrays.add(prayList.get(i));
            }
        }
        if (actualPrays.size() > 0) {
            timeToNext = actualPrays.get(0).getDate().getTimeInMillis() - currentTimeMils;
            mClosestPray = actualPrays.get(0);
        }


        return actualPrays;
    }
}
