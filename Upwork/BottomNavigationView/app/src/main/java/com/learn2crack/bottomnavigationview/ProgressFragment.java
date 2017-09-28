package com.learn2crack.bottomnavigationview;

/**
 * Created by Juk_VA on 27.09.2017.
 */

import android.os.Bundle;
import android.app.Fragment;
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

    TextView contentView;
    String contentText = null;
    WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
       // contentView = (TextView) view.findViewById(R.id.content);
       // webView = (WebView) view.findViewById(R.id.webView);
        lv = (ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, R.id.product_name, titleList);
        // если данные ранее были загружены
        if(contentText!=null){
      //      contentView.setText(contentText);
      //      webView.loadData(contentText, "text/html; charset=utf-8", "utf-8");
        }

        Button btnFetch = (Button)view.findViewById(R.id.downloadBtn);
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contentText==null){
                //    contentView.setText("Загрузка...");
                    new ProgressTask().execute("https://developer.android.com/index.html");
                }
            }
        });
        return view;
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

                ArrayList<String> prayerNameS = new ArrayList<String>();
                ArrayList<String> prayerNameTimeS = new ArrayList<String>();
                ArrayList<String> prayerTimeS = new ArrayList<String>();
                String temp ;
                int i = 0;
                for(Element titles: prayerName){
                    prayerNameS.add(prayerName.get(i).text());
                    i++;
                }

                i = 0;
                for(Element titles: prayerName){
                    prayerTimeS.add(prayerTime.get(i).text());
                    i++;
                }

                i = 0;
                for(Element titles: prayerName){
                    prayerNameTimeS.add(prayerNameS.get(i)+ " " + prayerTimeS.get(i));
                    i++;
                }
                // чистим наш аррей лист для того что бы заполнить
                titleList.clear();


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
            String content;
            try{
                content = getContent(path[0]);
            }
            catch (IOException ex){
                content = ex.getMessage();
            }

            return content;
        }
        @Override
        protected void onPostExecute(String content) {

            contentText=content;
          //  contentView.setText(content);
           // webView.loadData(content, "text/html; charset=utf-8", "utf-8");
            Toast.makeText(getActivity(), "Данные загружены", Toast.LENGTH_SHORT)
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
