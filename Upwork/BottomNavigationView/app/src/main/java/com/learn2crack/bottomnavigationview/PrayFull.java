package com.learn2crack.bottomnavigationview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Juk_VA on 09.10.2017.
 */

public class PrayFull {
    private static ArrayList<Pray> mPrayList = new ArrayList<Pray>();
    private static int mClosestPrayNum;
    private static int timeToNext;
    private static long currentTimeMils;
    PrayFull(){
    }

    public ArrayList<Pray> getPrayList() {
        return mPrayList;
    }

    public void setPrayList(ArrayList<Pray> prayList) {
        mPrayList = prayList;
    }

    public int getClosestPrayNum() {
        return mClosestPrayNum;
    }

    public void setClosestPrayNum(int closestPrayNum) {
        mClosestPrayNum = closestPrayNum;
    }

    PrayFull(ArrayList<Pray> prayList, int closestPrayNum){
        mPrayList = prayList;
        mClosestPrayNum = closestPrayNum;
    }

    public static int findClosestPray(Calendar currentDate){
        long currentDateinMills = currentDate.getTimeInMillis();
        for (int i =0; i< mPrayList.size();i++) {
            long time = mPrayList.get(i).getDate().getTimeInMillis();
         //   mPrayList.add(titles);
        }

        return 0;
    }

    public static ArrayList<Pray> getActualPrays(ArrayList<Pray> prayList){
        Calendar currCal = ProgressFragment.calendarCurrentTime;
        currentTimeMils = Calendar.getInstance().getTimeInMillis();
        ArrayList<Pray> actualPrays = new ArrayList<Pray>();
       for (int i =0; i< mPrayList.size();i++) {
            long time = mPrayList.get(i).getDate().getTimeInMillis();
        //    if(mPrayList.get(i).compareTo(currCal)==1){
       //         actualPrays.add(mPrayList.get(i));
        //    }
            //   mPrayList.add(titles);
        }

        return actualPrays;
    }
}
