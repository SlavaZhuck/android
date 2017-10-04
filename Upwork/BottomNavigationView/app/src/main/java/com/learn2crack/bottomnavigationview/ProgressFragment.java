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
    private SimpleAdapter adapter1;
    // List view
    private ListView lv;
    private TextView city;
    private TextView nextPrayer;
    private TextView nextPrayerTime;
    private TextView elapsedTime;
    Date datePray;
    Date currentDate;
    String contentText = null;
    WebView webView;
    ProgressTask progressTask;
    ArrayList<String> prayerNameS = new ArrayList<String>();
    ArrayList<String> prayerNameTimeS = new ArrayList<String>();
    ArrayList<String> prayerTimeS = new ArrayList<String>();
    Calendar calendarPrayTime = Calendar.getInstance();
    Calendar calendarCurrentTime = Calendar.getInstance();

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
     //   adapter1 = new SimpleAdapter(view.getContext(), R.layout.list_item , new String[]{"Name", "Time"},
     //           new int[]{R.id.pray_name, R.id.pray_time});
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
        MainActivity.Settings=this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, 0);
        int size= MainActivity.Settings.getInt("MyArraySize", 0); //читаем размер массива
        //if(size==0)
        //     return 0;
        String[] a=new String[size]; //аллоцируем массив
        for(int i=0; i < a.length; i++)
            a[i]=MainActivity.Settings.getString("MyArray"+i, null); //заполняем элементы массив
        titleList.clear();
        //saving data
        // и в цикле захватываем все данные какие есть на странице

        for (String titles : a) {
            // записываем в аррей лист
            titleList.add(titles);
        }
        String lastLocation = new String();
        lastLocation = MainActivity.Settings.getString("lastLocation", null);
        city = (TextView) this.getActivity().findViewById(R.id.city);
        nextPrayer = (TextView)this.getActivity().findViewById(R.id.nextPrayer);
        nextPrayerTime = (TextView)this.getActivity().findViewById(R.id.nextPrayTime);
        //elapsedTime = (TextView)this.getActivity().findViewById(R.id.elapsedTime);

        if(lastLocation!=null)
            city.setText(lastLocation);
        else
            city.setText("Unknown");
        lv.setAdapter(adapter);
        String arrPray[];
        String arrPrayTime[];
        String nextPrayHour = new String();
        String nextPrayminute = new String();

        DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
        currentDate = new Date();
        format.format(currentDate);
        calendarCurrentTime.setTime(currentDate);


        if(a.length>0){
            arrPray = a[0].split(" ", 2);
            arrPrayTime = arrPray[1].split(":", 2);
            nextPrayHour = arrPrayTime[0];
            nextPrayminute = arrPrayTime[1];
            calendarPrayTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(nextPrayHour));
            calendarPrayTime.set(Calendar.MINUTE, Integer.parseInt(nextPrayminute));

           // DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
            //currentDate = new Date();
            //format.format(currentDate);

            nextPrayer.setText(arrPray[0]);
            nextPrayerTime.setText(arrPray[1]);
           // long seconds = (calendarPrayTime.getTimeInMillis() - calendarCurrentTime.getTimeInMillis()) / 1000;
            //int diffMs = (int) (calendarPrayTime.getTime()-currentDate.getTime());
            // elapsedTime.setText(String.valueOf(seconds));

        }else{
            nextPrayer.setText("Unknown");
            nextPrayerTime.setText("Unknown");
            //elapsedTime.setText("Unknown");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        progressTask.cancel(true);
        MainActivity.Settings = this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = MainActivity.Settings.edit();
        if(prayerNameTimeS.size()>0) {
            editor.putInt("MyArraySize", prayerNameTimeS.size());
            for (int i = 0; i < prayerNameTimeS.size(); i++)
                editor.putString("MyArray" + i, prayerNameTimeS.get(i)); //складываем элементы массива
            editor.commit();
        }
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {

            // класс который захватывает страницу
            Document doc;
            String url = new String ();
            String currentYear = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.YEAR));
            String currentMonth = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.MONTH)+1);
            String currentDay = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.DAY_OF_MONTH));
            String currentPlace = "/415"; //for Male city
            url = "http://namaadhuvaguthu.com/en/prayertimes"+currentPlace+currentYear+currentMonth+currentDay;
            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect(url).get();
                // задаем с какого места, я выбрал заголовке статей/////////////////////////////////////////////////////////////////////////////////////////////////////////
                titleStr = doc.title();
                Elements prayerName = doc.getElementsByClass("prayer-name");
                Elements prayerTime= doc.getElementsByClass("prayer-time");

                Elements date= doc.getElementsByTag("h3");
                DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
                try {
                    datePray = format.parse(date.text());
                    calendarPrayTime.setTime(datePray);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                prayerNameS = new ArrayList<String>();
                prayerNameTimeS = new ArrayList<String>();
                prayerTimeS = new ArrayList<String>();

                int i = 0;
                for(Element titles: prayerName){
                    prayerNameS.add(prayerName.get(i).text());
                    prayerTimeS.add(prayerTime.get(i).text());
                    prayerNameTimeS.add(prayerName.get(i).text()+ " " + prayerTime.get(i).text());
                    i++;
                }

                // чистим наш аррей лист для того что бы заполнить
                titleList.clear();
                //saving data
                // и в цикле захватываем все данные какие есть на странице

                for (String titles : prayerNameTimeS) {
                    // записываем в аррей лист
                    titleList.add(titles);
                }


                // for (Element titles : prayerTime) {
                // записываем в аррей лист
                //     titleList.add(titles.text());
                // }


            } catch (IOException e) {
                e.printStackTrace();
            }
            //
//            String content;
//            try{
//                content = getContent(path[0]);
//            }
//            catch (IOException ex){
//                content = ex.getMessage();
//            }

//            return content;
            return null;
        }
        @Override
        protected void onPostExecute(String content) {

            contentText=content;
            //  contentView.setText(content);
            // webView.loadData(content, "text/html; charset=utf-8", "utf-8");
           // Toast.makeText(getActivity(), "Data loaded", Toast.LENGTH_SHORT).show();
            lv.setAdapter(adapter);
        }

        private String getContent(String path) throws IOException {
            BufferedReader reader=null;
            try {
                URL url=new URL(path);
                HttpsURLConnection c=(HttpsURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader= new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf=new StringBuilder();
                String line=null;
                while ((line=reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return(buf.toString());
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
}
