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
    //Date datePray;
    //Date datePrayNext;
    Date currentDate;
    //Date currentDateNext;
    String contentText = null;
    ProgressTask progressTask;
    ArrayList<String> prayerNameS = new ArrayList<String>();
    ArrayList<String> prayerNameTimeS = new ArrayList<String>();
    ArrayList<String> prayerTimeS = new ArrayList<String>();
    ArrayList<String> prayerNameSNext = new ArrayList<String>();
    ArrayList<String> prayerNameTimeSNext = new ArrayList<String>();
    ArrayList<String> prayerTimeSNext = new ArrayList<String>();
    ArrayList<Calendar> calendarPrayTime = new ArrayList<Calendar>();
    ArrayList<Pray> prayList= new ArrayList<Pray>();
    ArrayList<Pray> prayListActual= new ArrayList<Pray>();
    PrayFull prayFullOb= new PrayFull();
    static Calendar calendarCurrentTime = Calendar.getInstance();
    Calendar calendarCurrentTimeNext = Calendar.getInstance();
    String[] loadedArrayPray;
    String[] loadedArrayPrayNext;
    String lastLocation;
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
        nextPrayer = (TextView)this.getActivity().findViewById(R.id.nextPrayer);
        nextPrayerTime = (TextView)this.getActivity().findViewById(R.id.nextPrayTime);
        //elapsedTime = (TextView)this.getActivity().findViewById(R.id.elapsedTime);

        lv.setAdapter(adapter);
        calendarCurrentTime.setTime(currentDate);
        if(lastLocation!=null)
            city.setText(lastLocation);
        else
            city.setText("Unknown");

        if(prayListActual.size()>0) {
              nextPrayer.setText(prayListActual.get(0).getName());
            //  nextPrayerTime.setText(prayListActual.get(0).getHour()+":"+ prayListActual.get(0).getMinute());
        } else {
            nextPrayer.setText("Unknown");
            nextPrayerTime.setText("Unknown");
            //elapsedTime.setText("Unknown");
        }

        String nextPrayHour = new String();
        String nextPrayminute = new String();

    }

    public void restoreData(){
        MainActivity.Settings=this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, 0);
        int size= MainActivity.Settings.getInt("prayArraySize", 0); //читаем размер массива
        int sizeNext= MainActivity.Settings.getInt("prayArraySizeNext", 0); //читаем размер массива
        //if(size==0)
        //     return 0;
        loadedArrayPray=new String[size]; //аллоцируем массив
        loadedArrayPrayNext=new String[sizeNext]; //аллоцируем массив
        for(int i=0; i < loadedArrayPray.length; i++)
            loadedArrayPray[i]=MainActivity.Settings.getString("prayArray"+i, null); //заполняем элементы массив
        for(int i=0; i < loadedArrayPrayNext.length; i++)
            loadedArrayPrayNext[i]=MainActivity.Settings.getString("prayArrayNext"+i, null); //заполняем элементы массив
//        titleList.clear();
        //saving data
        // и в цикле захватываем все данные какие есть на странице

//        for (String titles : loadedArrayPray) {
            // записываем в аррей лист
//            titleList.add(titles);
//        }
 //       for (String titles : loadedArrayPrayNext) {
            // записываем в аррей лист
 //           titleList.add(titles);
 //       }
        DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
        currentDate = new Date();
        format.format(currentDate);
        calendarCurrentTimeNext.setTimeInMillis(calendarCurrentTime.getTimeInMillis()+86400000);//+1 day
        lastLocation = new String();
        lastLocation = MainActivity.Settings.getString("lastLocation", null);

        String arrPray[] = new String[size];
        String arrPrayName[] = new String[size];
        String arrPrayTime[] = new String[size];
        int arrPrayTimeH[] = new int[size];
        int arrPrayTimeM[] = new int[size];
        Calendar tempcalendar = Calendar.getInstance();

        if (loadedArrayPray.length > 0) {
            prayList.clear();
            for (int i =0;i<loadedArrayPray.length;i++) {
                arrPray = loadedArrayPray[i].split(" ", 2);
                arrPrayName[i] =arrPray[0];
                arrPrayTime[i] = arrPray[1];
                arrPrayTimeH[i] = Integer.parseInt(arrPrayTime[i].substring(0,2));
                arrPrayTimeM[i] = Integer.parseInt(arrPrayTime[i].substring(3,5));
                tempcalendar.set(   Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                    arrPrayTimeH[i],
                                    arrPrayTimeM[i],
                                    0 );
                prayList.add(new Pray(arrPrayName[i], tempcalendar));
            }
        }
        if (loadedArrayPrayNext.length > 0) {
            for (int i =0;i<loadedArrayPrayNext.length;i++) {
                arrPray = loadedArrayPrayNext[i].split(" ", 2);
                arrPrayName[i] =arrPray[0];
                arrPrayTime[i] = arrPray[1];
                arrPrayTimeH[i] = Integer.parseInt(arrPrayTime[i].substring(0,2));
                arrPrayTimeM[i] = Integer.parseInt(arrPrayTime[i].substring(3,5));
                tempcalendar.set(Calendar.YEAR, calendarCurrentTimeNext.get(Calendar.YEAR));
                tempcalendar.set(Calendar.MONTH, calendarCurrentTimeNext.get(Calendar.MONTH));
                tempcalendar.set(Calendar.DAY_OF_MONTH, calendarCurrentTimeNext.get(Calendar.DAY_OF_MONTH));
                tempcalendar.set(Calendar.HOUR_OF_DAY, arrPrayTimeH[i]);
                tempcalendar.set(Calendar.MINUTE, arrPrayTimeM[i]);

                prayList.add(new Pray(arrPrayName[i], tempcalendar));
               // tempcalendar.set()
            }
        }

        prayFullOb.setPrayList(prayList);
        prayListActual = prayFullOb.getActualPrays(prayList);

        titleList.clear();
        //saving data
        // и в цикле захватываем все данные какие есть на странице

        for(int i = 0; i<prayListActual.size(); i++){
            //titleList.add(prayListActual.get(i).getName()+"   "+prayListActual.get(i).getHour()+";"+prayListActual.get(i).getMinute());
       //     titleList.add(prayListActual.get(i).getName()+"   "+prayListActual.get(i).getHour()+";"+prayListActual.get(i).getMinute());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        progressTask.cancel(true);
        MainActivity.Settings = this.getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = MainActivity.Settings.edit();
        if(prayerNameTimeS.size()>0) {
            editor.putInt("prayArraySize", prayerNameTimeS.size());
            for (int i = 0; i < prayerNameTimeS.size(); i++)
                editor.putString("prayArray" + i, prayerNameTimeS.get(i)); //складываем элементы массива
            editor.commit();
        }
        if(prayerNameTimeSNext.size()>0) {
            editor.putInt("prayArraySizeNext", prayerNameTimeSNext.size());
            for (int i = 0; i < prayerNameTimeSNext.size(); i++)
                editor.putString("prayArrayNext" + i, prayerNameTimeSNext.get(i)); //складываем элементы массива
            editor.commit();
        }

    }


    private class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {

            // класс который захватывает страницу
            Document doc, docNext;
            String url = new String ();
            String urlNext = new String ();
            String currentYear = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.YEAR));
            String currentMonth = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.MONTH)+1);
            String currentDay = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.DAY_OF_MONTH));
            String nextDay = "/"+String.valueOf(calendarCurrentTime.getInstance().get(calendarCurrentTime.DAY_OF_MONTH)+1);
            String currentPlace = "/415"; //for Male city
            url = "http://namaadhuvaguthu.com/en/prayertimes"+currentPlace+currentYear+currentMonth+currentDay;
            urlNext = "http://namaadhuvaguthu.com/en/prayertimes"+currentPlace+currentYear+currentMonth+nextDay;
            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect(url).get();
                docNext = Jsoup.connect(urlNext).get();
                // задаем с какого места, я выбрал заголовке статей/////////////////////////////////////////////////////////////////////////////////////////////////////////
                titleStr = doc.title();
                Elements prayerName = doc.getElementsByClass("prayer-name");
                Elements prayerTime= doc.getElementsByClass("prayer-time");
                Elements prayerNameNext = docNext.getElementsByClass("prayer-name");
                Elements prayerTimeNext= docNext.getElementsByClass("prayer-time");
                Elements date= doc.getElementsByTag("h3");
                Elements dateNext= docNext.getElementsByTag("h3");
                DateFormat format = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);


                prayerNameS = new ArrayList<String>();
                prayerNameTimeS = new ArrayList<String>();
                prayerTimeS = new ArrayList<String>();
                prayerNameSNext = new ArrayList<String>();
                prayerNameTimeSNext = new ArrayList<String>();
                prayerTimeSNext = new ArrayList<String>();

                int i = 0;
                for(Element titles: prayerName){
                    prayerNameS.add(prayerName.get(i).text());
                    prayerTimeS.add(prayerTime.get(i).text());
                    prayerNameTimeS.add(prayerName.get(i).text()+ " " + prayerTime.get(i).text());
                    i++;
                }

                i = 0;
                for(Element titles: prayerNameNext){
                    prayerNameSNext.add(prayerNameNext.get(i).text());
                    prayerTimeSNext.add(prayerTimeNext.get(i).text());
                    prayerNameTimeSNext.add(prayerNameNext.get(i).text()+ " " + prayerTimeNext.get(i).text());
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
                for (String titles : prayerNameTimeSNext) {
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
