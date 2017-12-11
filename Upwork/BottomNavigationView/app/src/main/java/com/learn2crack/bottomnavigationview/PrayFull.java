package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class PrayFull {
    //private static ArrayList<Pray> mPrayList = new ArrayList<Pray>();
    public static long mClosestPrayTime;
    public static long timeToNext;
    public static long currentTimeMils;

    PrayFull() {
    }

    public static String getTimeToNext() {
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        if(mClosestPrayTime>1) {
            long seconds = ((mClosestPrayTime - currentTimeMils) / 1000) % 60;
            long minutes = ((mClosestPrayTime - currentTimeMils) / (1000 * 60)) % 60;
            long hours = ((mClosestPrayTime - currentTimeMils) / (1000 * 60 * 60)) % 24;
            // String formatted = String.format("H", hours);
            String elapsedTimeS = new String("- " + String.format("%02d:%02d:%02d", hours, minutes,seconds));
            return elapsedTimeS;
        }else{
            return "99:99:99";
        }
    }

    public static long getTimeToNextInMillis() {
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        if(mClosestPrayTime>1) {
             return mClosestPrayTime-currentTimeMils;
        }else{
            return -1;
        }
    }

    public static ArrayList<Pray> getActualPrays(ArrayList<Pray> prayList) {
        // Calendar currCal = ProgressFragment.mCalendarCurrentTime;
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        ArrayList<Pray> actualPrays = new ArrayList<Pray>();
        ArrayList<Pray> actualPraysShort = new ArrayList<Pray>();
        for (int i = 0; i < prayList.size(); i++) {
            long time = prayList.get(i).getDate().getTimeInMillis();
            if (time > currentTimeMils) {
                actualPrays.add(prayList.get(i));
            }
        }
        if (actualPrays.size() > 0) {
            timeToNext = actualPrays.get(0).getDate().getTimeInMillis() - currentTimeMils;
            mClosestPrayTime = actualPrays.get(0).getTimeInMillis();
        }
//        if (prayList.size() > 5) {
//            for(int i =0; i<=5; i++) {
//                actualPraysShort.add(prayList.get(i));
//            }
//        }

        return actualPrays;
    }
}
