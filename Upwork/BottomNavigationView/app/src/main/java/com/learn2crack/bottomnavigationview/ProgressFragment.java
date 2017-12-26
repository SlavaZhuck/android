package com.learn2crack.bottomnavigationview;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class ProgressFragment extends Fragment {
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> mStringListActualToSave = new ArrayList<>();
    private ListView lv;
    private TextView city;
    private TextView nextPrayer;
    private TextView nextPrayerTime;
    private TextView elapsedTime;
    private ProgressTask progressTask;
    private PrayAdapter prayAdapter;
    private static Calendar mCalendarCurrentTime = Calendar.getInstance();
    private ArrayList<Pray> mPrayList = new ArrayList<>();
    private String mLastLocation;
    public static Thread t;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        lv = (ListView) view.findViewById(R.id.listView);
        //adapter = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, R.id.pray_name, mStringListActualToSave);
        prayAdapter = new PrayAdapter(view.getContext(), mPrayList);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        progressTask.cancel(true);
        if(t.isAlive()) {
            t.interrupt();
        }
        Log.v(TAG, "onStopProgress");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressTask.cancel(true);
        if(t.isAlive()) {
            t.interrupt();
        }
        Log.v(TAG, "onDestroyProgress");
    }


    @Override
    public void onPause() {
        super.onPause();
        progressTask.cancel(true);
        if(t.isAlive()) {
            t.interrupt();
        }
        Log.v(TAG, "onPauseProgress");
    }

    @Override
    public void onResume() {
        super.onResume();
      //  if(!(progressTask.getStatus()== AsyncTask.Status.RUNNING)){
            progressTask = (ProgressTask) new ProgressTask().execute();
       // }
//        if(!t.isAlive()){
            t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // update TextView here!
                                    String elapsedTimeS = PrayFull.getTimeToNext();
                                    elapsedTime.setText(elapsedTimeS);
                                }
                            });
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
 //       }
        restoreData();

        //city = (TextView) this.getActivity().findViewById(R.id.city);
        nextPrayer = (TextView) this.getActivity().findViewById(R.id.nextPrayer);
        nextPrayerTime = (TextView) this.getActivity().findViewById(R.id.nextPrayTime);
        elapsedTime = (TextView) this.getActivity().findViewById(R.id.elapsedTime);

        t.start();
        updateDataFields();
        lv.setAdapter(prayAdapter);
        Log.v(TAG, "onResumeProgress");
    }

    public void updateDataFields() {
        Log.v(TAG, "UpdateFields");
        if (mPrayList.size() > 0) {
            mPrayList = PrayFull.getActualPrays(mPrayList);

            nextPrayer.setText(mPrayList.get(0).getName());
            nextPrayerTime.setText(mPrayList.get(0).getTime());

            if((int)PrayFull.getTimeToNextInMillis()>0) {
                scheduleNotification(getNotification(getResources().getString(R.string.Pray) + " " + mPrayList.get(0).getName() + " " + getResources().getString(R.string.Isnow),"Notification for Pray"), (int) PrayFull.getTimeToNextInMillis());
            }
        } else {
            nextPrayer.setText(R.string.unknown);
            nextPrayerTime.setText(R.string.unknown);
            elapsedTime.setText(R.string.unknown);
        }
    }

    public void restoreData() {
        Log.v(TAG, "RestoreData");

        MainActivity.Settings = this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, 0);

        int size = MainActivity.Settings.getInt("prayActualSize", 0); //читаем размер массива
        String[] mLoadedArrayActualPrayString = new String[size];
        for (int i = 0; i < mLoadedArrayActualPrayString.length; i++)
            mLoadedArrayActualPrayString[i] = MainActivity.Settings.getString("prayArrayActual" + i, null); //заполняем элементы массив

       // mLastLocation = new String();
        if(MainActivity.Settings.getString("lastLocation", null)!= null){
            mLastLocation = MainActivity.Settings.getString("lastLocation", null);
        }else{
            mLastLocation = "Unknown";
        }
        String arrPray[];
        String arrPrayName[] = new String[size];
         String arrPrayTimeImMillis[] = new String[size];

        if (mLoadedArrayActualPrayString.length > 0) {
            mPrayList.clear();
            for (int i = 0; i < mLoadedArrayActualPrayString.length; i++) {
                arrPray = mLoadedArrayActualPrayString[i].split(" ", 3);
                arrPrayName[i] = arrPray[0];
                //arrPrayTime[i] = arrPray[1];
                arrPrayTimeImMillis[i] = arrPray[2];
                mCalendarCurrentTime = Calendar.getInstance();
                mCalendarCurrentTime.setTimeInMillis(Long.parseLong(arrPrayTimeImMillis[i], 10)   );
                mPrayList.add(new Pray(arrPrayName[i], mCalendarCurrentTime));
            }
        }

    }


   // @SuppressLint("StaticFieldLeak")
    private class ProgressTask extends AsyncTask<String, Void, String> {
    //    @SuppressLint("DefaultLocale")
        @Override
        protected String doInBackground(String... path) {

            // класс который захватывает страницу
            Document doc, docNext;
            String url ;
            String urlNext;
            //mCalendarCurrentTime = Calendar.getInstance();
            Calendar mCalendarNextTime = Calendar.getInstance();
            mCalendarNextTime.setTimeInMillis(Calendar.getInstance().getTimeInMillis()+ 86400000L);
            String currentYear = "/" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            String currentMonth = "/" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
            String currentDay = "/" + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            String nextDay = "/" + String.valueOf(mCalendarNextTime.get(Calendar.DAY_OF_MONTH) );
            String currentPlace = "/415"; //for Male city
            url = "http://namaadhuvaguthu.com/en/prayertimes" + currentPlace + currentYear + currentMonth + currentDay;
            urlNext = "http://namaadhuvaguthu.com/en/prayertimes" + currentPlace + currentYear + currentMonth + nextDay;
            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect(url).get();
                docNext = Jsoup.connect(urlNext).get();
                // задаем с какого места, я выбрал заголовке статей/////////////////////////////////////////////////////////////////////////////////////////////////////////
                //mTitleStr = doc.title();
                Elements prayerName = doc.getElementsByClass("prayer-name");
                Elements prayerTime = doc.getElementsByClass("prayer-time");
                Elements prayerNameNext = docNext.getElementsByClass("prayer-name");

                ArrayList<Pray> mPraylistUrl = new ArrayList<>();
                ArrayList<Pray> mPraylistUrlNext = new ArrayList<>();
                ArrayList<Pray> mPraylistUrlFull = new ArrayList<>();

                for (int i = 0; i < prayerName.size(); i++) {
                    mPraylistUrl.add(new Pray(prayerName.get(i).text(), prayerTime.get(i).text(), true));
                }
                for (int i = 0; i < prayerNameNext.size(); i++) {
                    mPraylistUrlNext.add(new Pray(prayerName.get(i).text(), prayerTime.get(i).text(), false));
                }
                mPraylistUrlFull.addAll(mPraylistUrl);
                mPraylistUrlFull.addAll(mPraylistUrlNext);

                if (mPraylistUrlFull.size() > 0) {
                    mStringListActualToSave.clear();
                    for (int i = 0; i < mPraylistUrlFull.size(); i++) {
                        mStringListActualToSave.add(mPraylistUrlFull.get(i).getName() + " " + String.format("%02d:%02d", mPraylistUrlFull.get(i).getDate().get(Calendar.HOUR_OF_DAY), mPraylistUrlFull.get(i).getDate().get(Calendar.MINUTE)) + " " + mPraylistUrlFull.get(i).getTimeInMillis());
                    }
                }

                MainActivity.Settings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = MainActivity.Settings.edit();
               // editor.putLong("prayActualTime", PrayFull.mClosestPrayTime);
                if (mStringListActualToSave.size() > 0) {
                    editor.putInt("prayActualSize", mStringListActualToSave.size());
                    for (int i = 0; i < mStringListActualToSave.size(); i++)
                        editor.putString("prayArrayActual" + i, mStringListActualToSave.get(i)); //складываем элементы массива
                    editor.apply();
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String content) {

            updateDataFields();
            lv.setAdapter(prayAdapter);
        }

//        private String getContent(String path) throws IOException {
//            BufferedReader reader = null;
//            try {
//                URL url = new URL(path);
//                HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
//                c.setRequestMethod("GET");
//                c.setReadTimeout(10000);
//                c.connect();
//                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
//                StringBuilder buf = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    buf.append(line + "\n");
//                }
//                return (buf.toString());
//            } finally {
//                if (reader != null) {
//                    reader.close();
//                }
//            }
//        }
    }
    public void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }
    }

    public Notification getNotification(String content, String title) {
        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setContentTitle(title);//"Scheduled Notification for Pray");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_notification);
        return builder.build();
    }


}
