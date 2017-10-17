package com.learn2crack.bottomnavigationview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class PrayFull {
    private static ArrayList<Pray> mPrayList = new ArrayList<Pray>();
    private static Pray mClosestPray = new Pray();
    private static long timeToNext;
    private static long currentTimeMils;
    private static int mPrayListSize = 0;

    PrayFull() {
    }

    public static int getmPrayListSize() {
        return mPrayListSize;
    }

    public ArrayList<Pray> getPrayList() {
        return mPrayList;
    }

    public void setPrayList(ArrayList<Pray> prayList) {
        mPrayList = prayList;
    }


    PrayFull(ArrayList<Pray> prayList) {
        mPrayList = prayList;

    }

//    public static int findClosestPray(Calendar currentDate){
//        long currentDateinMills = currentDate.getTimeInMillis();
//        for (int i =0; i< mPrayList.size();i++) {
//            long time = mPrayList.get(i).getDate().getTimeInMillis();
//         //   mPrayList.add(titles);
//        }
//
//        return 0;
//    }


    public static long getTimeToNext() {
        currentTimeMils = Calendar.getInstance().getTimeInMillis();

        return mClosestPray.getDate().getTimeInMillis() - currentTimeMils;

    }

    public static ArrayList<Pray> getActualPrays(ArrayList<Pray> prayList) {
        // Calendar currCal = ProgressFragment.mCalendarCurrentTime;
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        ArrayList<Pray> actualPrays = new ArrayList<Pray>();
        for (int i = 0; i < mPrayList.size(); i++) {
            long time = mPrayList.get(i).getDate().getTimeInMillis();
            if (time > currentTimeMils) {
                actualPrays.add(mPrayList.get(i));
            }
            //actualPrays.add(titles);
        }
        if (mPrayList.size() > 0) {
            timeToNext = actualPrays.get(0).getDate().getTimeInMillis() - currentTimeMils;
        }
        mPrayListSize = mPrayList.size();
        mClosestPray = actualPrays.get(0);
        return actualPrays;
    }
}
