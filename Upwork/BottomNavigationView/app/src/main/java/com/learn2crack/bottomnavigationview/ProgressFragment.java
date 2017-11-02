package com.learn2crack.bottomnavigationview;

/**
 * Created by Juk_VA on 27.09.2017.
 */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class ProgressFragment extends Fragment {
    // благодоря этому классу мы будет разбирать данные на куски
    public Elements title;
    //public String mTitleStr;
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> mStringListActualToSave = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView lv;
    private TextView city;
    private TextView nextPrayer;
    private TextView nextPrayerTime;
    private TextView elapsedTime;
    private ProgressTask progressTask;
    private PrayAdapter prayAdapter;
    private static Calendar mCalendarCurrentTime = Calendar.getInstance();
    private static Calendar mCalendarNextTime = Calendar.getInstance();
    private Date mCurrentDate;
    private ArrayList<String> mPrayerNameS = new ArrayList<>();
    private ArrayList<String> mPrayerNameTimeS = new ArrayList<>();
    private ArrayList<String> mPrayerNameSNext = new ArrayList<>();
    private ArrayList<String> mPrayerNameTimeSNext = new ArrayList<>();
    private ArrayList<Pray> mPrayList = new ArrayList<>();
    private ArrayList<Pray> mPrayListActual = new ArrayList<>();
    private ArrayList<Pray> mPrayListShow = new ArrayList<>();
    private String[] mLoadedArrayActualPrayString;
    private String[] mLoadedArrayShowPrayString;
    private String mLastLocation;
    private ArrayList<Pray> mPraylistUrl = new ArrayList<>();
    private ArrayList<Pray> mPraylistUrlNext = new ArrayList<>();
    private ArrayList<Pray> mPraylistUrlFull = new ArrayList<>();
    private long mClosestPrayTime;
    //  private ArrayList<Pray> mPraylistToShow = new ArrayList<>();
  //  private PrayFull mPrayFullOb = new PrayFull();


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

        progressTask = (ProgressTask) new ProgressTask().execute();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        progressTask.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreData();

        city = (TextView) this.getActivity().findViewById(R.id.city);
        nextPrayer = (TextView) this.getActivity().findViewById(R.id.nextPrayer);
        nextPrayerTime = (TextView) this.getActivity().findViewById(R.id.nextPrayTime);
        elapsedTime = (TextView) this.getActivity().findViewById(R.id.elapsedTime);

        DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
         //mCurrentDate = new Date();
        //format.format(mCurrentDate);
        //mCalendarCurrentTime.setTime(mCurrentDate);

        lv.setAdapter(prayAdapter);
        updateDataFields();

    }

    public void updateDataFields() {
        if (mLastLocation != null) {
            city.setText(mLastLocation);
        }else {
            city.setText("Unknown");
        }

        //scheduleNotification(getNotification("20 second delay"), 20000);
        if (mPrayList.size() > 0) {
            mPrayList = PrayFull.getActualPrays(mPrayList);

            nextPrayer.setText(mPrayList.get(0).getName());
            nextPrayerTime.setText(mPrayList.get(0).getTime());
            String elapsedTimeS = PrayFull.getTimeToNext();
            elapsedTime.setText(elapsedTimeS);
            if((int)PrayFull.getTimeToNextInMillis()>0) {
                scheduleNotification(getNotification("Pray is now"), (int) PrayFull.getTimeToNextInMillis());
            }
        } else {
            nextPrayer.setText("Unknown");
            nextPrayerTime.setText("Unknown");
            elapsedTime.setText("Unknown");
        }
    }

    public void restoreData() {

        MainActivity.Settings = this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, 0);

        int size = MainActivity.Settings.getInt("prayActualSize", 0); //читаем размер массива
        mLoadedArrayActualPrayString = new String[size]; //аллоцируем массив
        for (int i = 0; i < mLoadedArrayActualPrayString.length; i++)
            mLoadedArrayActualPrayString[i] = MainActivity.Settings.getString("prayArrayActual" + i, null); //заполняем элементы массив

        mLastLocation = new String();
        mLastLocation = MainActivity.Settings.getString("lastLocation", null);
        String arrPray[] ;
        String arrPrayName[] = new String[size];
        String arrPrayTime[] = new String[size];
        String arrPrayTimeImMillis[] = new String[size];

        if (mLoadedArrayActualPrayString.length > 0) {
            mPrayList.clear();
            for (int i = 0; i < mLoadedArrayActualPrayString.length; i++) {
                arrPray = mLoadedArrayActualPrayString[i].split(" ", 3);
                arrPrayName[i] = arrPray[0];
                arrPrayTime[i] = arrPray[1];
                arrPrayTimeImMillis[i] = arrPray[2];
                mCalendarCurrentTime = Calendar.getInstance();
                mCalendarCurrentTime.setTimeInMillis(Long.parseLong(arrPrayTimeImMillis[i], 10)   );
                mPrayList.add(new Pray(arrPrayName[i], mCalendarCurrentTime));
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        progressTask.cancel(true);
    }


    private class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {

            // класс который захватывает страницу
            Document doc, docNext;
            String url ;
            String urlNext ;
            mCalendarCurrentTime = Calendar.getInstance();
            mCalendarNextTime = Calendar.getInstance();
            mCalendarNextTime.setTimeInMillis(mCalendarCurrentTime.getTimeInMillis()+ 86400000l);
            String currentYear = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.YEAR));
            String currentMonth = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.MONTH) + 1);
            String currentDay = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.DAY_OF_MONTH));
            String nextDay = "/" + String.valueOf(mCalendarNextTime.get(mCalendarNextTime.DAY_OF_MONTH) );
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
                Elements prayerTimeNext = docNext.getElementsByClass("prayer-time");

                mPraylistUrl = new ArrayList<>();
                mPraylistUrlNext = new ArrayList<>();
                mPraylistUrlFull = new ArrayList<>();

                mPrayerNameS = new ArrayList<>();
                mPrayerNameTimeS = new ArrayList<>();
                mPrayerNameSNext  = new ArrayList<>();
                mPrayerNameTimeSNext = new ArrayList<>();

                for (int i = 0; i < prayerName.size(); i++) {
                    mPrayerNameS.add(prayerName.get(i).text());
                    mPrayerNameTimeS.add(prayerName.get(i).text() + " " + prayerTime.get(i).text());
                    mPraylistUrl.add(new Pray(prayerName.get(i).text(), prayerTime.get(i).text(), true));
                }
                for (int i = 0; i < prayerNameNext.size(); i++) {
                    mPrayerNameSNext.add(prayerNameNext.get(i).text());
                    mPrayerNameTimeSNext.add(prayerNameNext.get(i).text() + " " + prayerTimeNext.get(i).text());
                    mPraylistUrlNext.add(new Pray(prayerName.get(i).text(), prayerTime.get(i).text(), false));
                }
                mPraylistUrlFull.addAll(mPraylistUrl);
                mPraylistUrlFull.addAll(mPraylistUrlNext);

               // mPrayListActual = PrayFull.getActualPrays(mPraylistUrlFull);

                if (mPraylistUrlFull.size() > 0) {
                    mStringListActualToSave.clear();
                    for (int i = 0; i < mPraylistUrlFull.size(); i++) {
                        mStringListActualToSave.add(mPraylistUrlFull.get(i).getName() + " " + String.format("%02d:%02d", mPraylistUrlFull.get(i).getDate().get(Calendar.HOUR_OF_DAY), mPraylistUrlFull.get(i).getDate().get(Calendar.MINUTE)) + " " +mPraylistUrlFull.get(i).getTimeInMillis());
                    }
                }

                MainActivity.Settings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = MainActivity.Settings.edit();
               // editor.putLong("prayActualTime", PrayFull.mClosestPrayTime);
                if (mStringListActualToSave.size() > 0) {
                    editor.putInt("prayActualSize", mStringListActualToSave.size());
                    for (int i = 0; i < mStringListActualToSave.size(); i++)
                        editor.putString("prayArrayActual" + i, mStringListActualToSave.get(i).toString()); //складываем элементы массива
                    editor.commit();
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

        private String getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return (buf.toString());
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
    public void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(getActivity(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setContentTitle("Scheduled Notification for Pray");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.background_home);
        return builder.build();
    }
}
