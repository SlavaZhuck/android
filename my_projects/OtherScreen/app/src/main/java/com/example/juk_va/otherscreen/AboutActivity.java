package com.example.juk_va.otherscreen;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Alexander Klimov on 01.12.2014.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        String data1 = getIntent().getStringExtra("data1");
        String data2 = getIntent().getStringExtra("data2");
        TextView textData1 = (TextView)findViewById(R.id.textData1);
        TextView textData2 = (TextView)findViewById(R.id.textData2);
        textData1.setText(data1);
        textData2.setText(data2);
    }
}
