package com.learn2crack.bottomnavigationview;

/**
 * Created by Juk_VA on 27.09.2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
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
    public String titleStr;
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> titleList = new ArrayList<String>();
    // Listview Adapter для вывода данных
    private ArrayAdapter<String> adapter;
    // List view
    private ListView lv;
    private TextView city;
    private TextView nextPrayer;
    private TextView nextPrayerTime;
    private TextView elapsedTime;
    private ProgressTask progressTask;
    //private String mContentText = null;
    private static Calendar mCalendarCurrentTime = Calendar.getInstance();
    private Date mCurrentDate;
    private ArrayList<String> mPrayerNameS = new ArrayList<String>();
    private ArrayList<String> mPrayerNameTimeS = new ArrayList<String>();
    private ArrayList<String> mPrayerTimeS = new ArrayList<String>();
    private ArrayList<String> mPrayerNameSNext = new ArrayList<String>();
    private ArrayList<String> prayerNameTimeSNext = new ArrayList<String>();
    private ArrayList<String> prayerTimeSNext = new ArrayList<String>();
    private ArrayList<Pray> mPrayList = new ArrayList<Pray>();
    private ArrayList<Pray> mPrayListActual = new ArrayList<Pray>();
    private PrayFull mPrayFullOb = new PrayFull(mPrayList);
    private String[] mLoadedArrayPray;
    private String mLastLocation;
    PrayAdapter prayAdapter;
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
        adapter = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, R.id.pray_name, titleList);
        prayAdapter = new PrayAdapter(this., mPrayListActual);

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

        //lv.setAdapter(adapter);
        lv.setAdapter(prayAdapter);
        mCalendarCurrentTime.setTime(mCurrentDate);

        mLastLocation = new String();
        mLastLocation = MainActivity.Settings.getString("lastLocation", null);
        if (mLastLocation != null)
            city.setText(mLastLocation);
        else
            city.setText("Unknown");

        if (mPrayList.size() > 0) {
            nextPrayer.setText(mPrayList.get(0).getName());
            nextPrayerTime.setText(mPrayList.get(0).getDate().get(Calendar.HOUR_OF_DAY) + ":" + mPrayList.get(0).getDate().get(Calendar.MINUTE));
//            if(mPrayFullOb.getmPrayListSize()>0) {
//                long seconds = (long) (mPrayFullOb.getTimeToNext() / 1000) % 60;
//                long minutes = (long) ((mPrayFullOb.getTimeToNext() / (1000 * 60)) % 60);
//                long hours = (long) ((mPrayFullOb.getTimeToNext() / (1000 * 60 * 60)) % 24);
//                String formatted = String.format("H", hours);
//                elapsedTime.setText("(- " + String.format("%02d:%02d", hours, minutes) + ")");
//            }
            MainActivity.Settings = this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, 0);
            String elapsedTimeS = MainActivity.Settings.getString("elapsedTime", null); //читаем размер массива
            elapsedTime.setText(elapsedTimeS);
        } else {
            nextPrayer.setText("Unknown");
            nextPrayerTime.setText("Unknown");
            elapsedTime.setText("Unknown");
        }
    }

    public void restoreData() {
        MainActivity.Settings = this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, 0);
        int size = MainActivity.Settings.getInt("prayArraySize", 0); //читаем размер массива

        mLoadedArrayPray = new String[size]; //аллоцируем массив
        for (int i = 0; i < mLoadedArrayPray.length; i++)
            mLoadedArrayPray[i] = MainActivity.Settings.getString("prayArray" + i, null); //заполняем элементы массив

        DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
        mCurrentDate = new Date();
        format.format(mCurrentDate);


        String arrPray[] = new String[size];
        String arrPrayName[] = new String[size];
        String arrPrayTime[] = new String[size];
        long arrPrayTimeH[] = new long[size];
        long arrPrayTimeM[] = new long[size];
        Calendar tempcalendar = Calendar.getInstance();
        long currentDayTImeStamp = Calendar.getInstance().getTimeInMillis();
        currentDayTImeStamp = currentDayTImeStamp - tempcalendar.get(Calendar.HOUR_OF_DAY) * 1000l * 60l * 60l
                - tempcalendar.get(Calendar.MINUTE) * 1000l * 60l - tempcalendar.get(Calendar.SECOND) * 1000l - tempcalendar.get(Calendar.MILLISECOND);
        if (mLoadedArrayPray.length > 0) {
            mPrayList.clear();
            for (int i = 0; i < mLoadedArrayPray.length; i++) {
                arrPray = mLoadedArrayPray[i].split(" ", 2);
                arrPrayName[i] = arrPray[0];
                arrPrayTime[i] = arrPray[1];
                arrPrayTimeH[i] = Integer.parseInt(arrPrayTime[i].substring(0, 2));
                arrPrayTimeM[i] = Integer.parseInt(arrPrayTime[i].substring(3, 5));
                tempcalendar = Calendar.getInstance();
                tempcalendar.set(Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                        (int) arrPrayTimeH[i],
                        (int) arrPrayTimeM[i],
                        0);
                tempcalendar.setTimeInMillis(currentDayTImeStamp + arrPrayTimeH[i] * 1000l * 60l * 60l + arrPrayTimeM[i] * 1000l * 60l);
                mPrayList.add(new Pray(arrPrayName[i], tempcalendar));
            }
        }

        if (mPrayList.size() > 0) {
            titleList.clear();
            for (int i = 0; i < mPrayList.size(); i++) {
                titleList.add(mPrayList.get(i).getName() + " " + String.format("%02d:%02d", mPrayList.get(i).getDate().get(Calendar.HOUR_OF_DAY), mPrayList.get(i).getDate().get(Calendar.MINUTE)));
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
            String url = new String();
            String urlNext = new String();
            String currentYear = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.YEAR));
            String currentMonth = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.MONTH) + 1);
            String currentDay = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.DAY_OF_MONTH));
            String nextDay = "/" + String.valueOf(mCalendarCurrentTime.getInstance().get(mCalendarCurrentTime.DAY_OF_MONTH) + 1);
            String currentPlace = "/415"; //for Male city
            url = "http://namaadhuvaguthu.com/en/prayertimes" + currentPlace + currentYear + currentMonth + currentDay;
            urlNext = "http://namaadhuvaguthu.com/en/prayertimes" + currentPlace + currentYear + currentMonth + nextDay;
            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect(url).get();
                docNext = Jsoup.connect(urlNext).get();
                // задаем с какого места, я выбрал заголовке статей/////////////////////////////////////////////////////////////////////////////////////////////////////////
                titleStr = doc.title();
                Elements prayerName = doc.getElementsByClass("prayer-name");
                Elements prayerTime = doc.getElementsByClass("prayer-time");
                Elements prayerNameNext = docNext.getElementsByClass("prayer-name");
                Elements prayerTimeNext = docNext.getElementsByClass("prayer-time");

                mPrayerNameS = new ArrayList<>();
                mPrayerNameTimeS = new ArrayList<>();
                mPrayerTimeS = new ArrayList<>();
                mPrayerNameSNext = new ArrayList<>();
                prayerNameTimeSNext = new ArrayList<>();
                prayerTimeSNext = new ArrayList<>();


                for (int i = 0; i < prayerName.size(); i++) {
                    mPrayerNameS.add(prayerName.get(i).text());
                    mPrayerTimeS.add(prayerTime.get(i).text());
                    mPrayerNameTimeS.add(prayerName.get(i).text() + " " + prayerTime.get(i).text());

                }


                for (int i = 0; i < prayerNameNext.size(); i++) {
                    mPrayerNameSNext.add(prayerNameNext.get(i).text());
                    prayerTimeSNext.add(prayerTimeNext.get(i).text());
                    prayerNameTimeSNext.add(prayerNameNext.get(i).text() + " " + prayerTimeNext.get(i).text());

                }

                mLastLocation = new String();
                mLastLocation = MainActivity.Settings.getString("lastLocation", null);

                String arrPray[] = new String[prayerName.size()];
                String arrPrayName[] = new String[prayerName.size()];
                String arrPrayTime[] = new String[prayerName.size()];
                long arrPrayTimeH[] = new long[prayerName.size()];
                long arrPrayTimeM[] = new long[prayerName.size()];
                Calendar tempcalendar = Calendar.getInstance();
                long currentDayTImeStamp = Calendar.getInstance().getTimeInMillis();
                currentDayTImeStamp = currentDayTImeStamp - tempcalendar.get(Calendar.HOUR_OF_DAY) * 1000l * 60l * 60l
                        - tempcalendar.get(Calendar.MINUTE) * 1000l * 60l - tempcalendar.get(Calendar.SECOND) * 1000l - tempcalendar.get(Calendar.MILLISECOND);
                if (mPrayerNameTimeS.size() > 0) {
                    mPrayList.clear();
                    for (int i = 0; i < mPrayerNameTimeS.size(); i++) {
                        arrPray = mPrayerNameTimeS.get(i).split(" ", 2);
                        arrPrayName[i] = arrPray[0];
                        arrPrayTime[i] = arrPray[1];
                        arrPrayTimeH[i] = Integer.parseInt(arrPrayTime[i].substring(0, 2));
                        arrPrayTimeM[i] = Integer.parseInt(arrPrayTime[i].substring(3, 5));
                        tempcalendar = Calendar.getInstance();
                        tempcalendar.set(Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                (int) arrPrayTimeH[i],
                                (int) arrPrayTimeM[i],
                                0);
                        tempcalendar.setTimeInMillis(currentDayTImeStamp + arrPrayTimeH[i] * 1000l * 60l * 60l + arrPrayTimeM[i] * 1000l * 60l);
                        mPrayList.add(new Pray(arrPrayName[i], tempcalendar));
                    }
                }
                if (prayerNameTimeSNext.size() > 0) {
                    for (int i = 0; i < prayerNameTimeSNext.size(); i++) {
                        arrPray = prayerNameTimeSNext.get(i).split(" ", 2);
                        arrPrayName[i] = arrPray[0];
                        arrPrayTime[i] = arrPray[1];
                        arrPrayTimeH[i] = Integer.parseInt(arrPrayTime[i].substring(0, 2));
                        arrPrayTimeM[i] = Integer.parseInt(arrPrayTime[i].substring(3, 5));
                        tempcalendar = Calendar.getInstance();
                        tempcalendar.set(Calendar.getInstance().get(Calendar.YEAR),
                                Calendar.getInstance().get(Calendar.MONTH),
                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1,
                                (int) arrPrayTimeH[i],
                                (int) arrPrayTimeM[i],
                                0);
                        tempcalendar.setTimeInMillis(currentDayTImeStamp + arrPrayTimeH[i] * 1000l * 60l * 60l + arrPrayTimeM[i] * 1000l * 60l + 86400000l);
                        mPrayList.add(new Pray(arrPrayName[i], tempcalendar));
                    }
                }

                mPrayFullOb.setPrayList(mPrayList);
                mPrayListActual = mPrayFullOb.getActualPrays(mPrayList);

                if (mPrayListActual.size() > 0) {
                    titleList.clear();
                    for (int i = 0; i < mPrayListActual.size(); i++) {
                        titleList.add(mPrayListActual.get(i).getName() + " " + String.format("%02d:%02d", mPrayListActual.get(i).getDate().get(Calendar.HOUR_OF_DAY), mPrayListActual.get(i).getDate().get(Calendar.MINUTE)));
                    }
                }

                MainActivity.Settings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = MainActivity.Settings.edit();
                if (titleList.size() > 0) {
                    editor.putInt("prayArraySize", titleList.size());
                    for (int i = 0; i < titleList.size(); i++)
                        editor.putString("prayArray" + i, titleList.get(i).toString()); //складываем элементы массива
                    editor.commit();
                }

                if (mPrayFullOb.getmPrayListSize() > 0) {
                    long seconds = (long) (mPrayFullOb.getTimeToNext() / 1000) % 60;
                    long minutes = (long) ((mPrayFullOb.getTimeToNext() / (1000 * 60)) % 60);
                    long hours = (long) ((mPrayFullOb.getTimeToNext() / (1000 * 60 * 60)) % 24);
                    String formatted = String.format("H", hours);
                    String elapsedTimeS = new String("(- " + String.format("%02d:%02d", hours, minutes) + ")");
                    editor.putString("elapsedTime", elapsedTimeS);
                    editor.commit();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String content) {

            // mContentText=content;
            //  contentView.setText(content);
            // webView.loadData(content, "text/html; charset=utf-8", "utf-8");
            // Toast.makeText(getActivity(), "Data loaded", Toast.LENGTH_SHORT).show();
           // lv.setAdapter(adapter);
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
}
