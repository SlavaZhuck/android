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
import java.util.ArrayList;
import java.util.Iterator;

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
    TextView contentView;
    String contentText = null;
    WebView webView;
    ProgressTask progressTask;
    ArrayList<String> prayerNameS = new ArrayList<String>();
    ArrayList<String> prayerNameTimeS = new ArrayList<String>();
    ArrayList<String> prayerTimeS = new ArrayList<String>();

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
       adapter = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, R.id.product_name, titleList);
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
        city = (TextView) this.getActivity().findViewById(R.id.city);
        city.setText(MainActivity.addresses.get(0).getLocality());

        lv.setAdapter(adapter);

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
            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect("http://namaadhuvaguthu.com/en").get();
                // задаем с какого места, я выбрал заголовке статей/////////////////////////////////////////////////////////////////////////////////////////////////////////
                titleStr = doc.title();
                Elements prayerName = doc.getElementsByClass("prayer-name");
                Elements prayerTime= doc.getElementsByClass("prayer-time");

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
            Toast.makeText(getActivity(), "Data loaded", Toast.LENGTH_SHORT)
                    .show();
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
