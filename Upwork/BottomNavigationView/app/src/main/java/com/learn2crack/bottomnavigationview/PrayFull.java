package com.learn2crack.bottomnavigationview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class PrayFull {
    //private static ArrayList<Pray> mPrayList = new ArrayList<Pray>();
    private static Pray mClosestPray = new Pray();
    private static long timeToNext;
    private static long currentTimeMils;
    private static int mPrayListSize = 0;

    PrayFull() {
    }

    public static int getmPrayListSize() {
        return mPrayListSize;
    }

    //public ArrayList<Pray> getPrayList() {
    //    return mPrayList;
    //}

    //public void setPrayList(ArrayList<Pray> prayList) {
    //    mPrayList = prayList;
   // }


//    PrayFull(ArrayList<Pray> prayList) {
//        mPrayList = prayList;
//
//    }

//    public static int findClosestPray(Calendar currentDate){
//        long currentDateinMills = currentDate.getTimeInMillis();
//        for (int i =0; i< mPrayList.size();i++) {
//            long time = mPrayList.get(i).getDate().getTimeInMillis();
//         //   mPrayList.add(titles);
//        }
//
//        return 0;
//    }


    public static String getTimeToNext() {
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        long seconds = (long) ((mClosestPray.getDate().getTimeInMillis() - currentTimeMils) / 1000) % 60;
        long minutes = (long) ((mClosestPray.getDate().getTimeInMillis() - currentTimeMils) / (1000 * 60)) % 60;
        long hours = (long) ((mClosestPray.getDate().getTimeInMillis() - currentTimeMils)/ (1000 * 60 * 60)) % 24;
        String formatted = String.format("H", hours);
        String elapsedTimeS = new String("(- " + String.format("%02d:%02d", hours, minutes) + ")");

        return elapsedTimeS;

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
            //actualPrays.add(titles);
        }
        if (prayList.size() > 0) {
            timeToNext = actualPrays.get(0).getDate().getTimeInMillis() - currentTimeMils;
        }
        mPrayListSize = prayList.size();
        mClosestPray = actualPrays.get(0);
        return actualPrays;
    }
}
