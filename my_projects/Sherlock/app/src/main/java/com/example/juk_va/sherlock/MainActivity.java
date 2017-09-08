package com.example.juk_va.sherlock;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    static final private int CHOOSE_THIEF = 0;
    public void onClick(View view) {
        Intent questionIntent = new Intent(MainActivity.this, InvestigateActivity.class);
        startActivityForResult(questionIntent,CHOOSE_THIEF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView textViewInfo = (TextView) findViewById(R.id.textViewInfo);

        if(requestCode == CHOOSE_THIEF){
                if(resultCode == RESULT_OK){
                    String thiefName = data.getStringExtra(InvestigateActivity.THIEF);
                    textViewInfo.setText(thiefName);
                } else {
                    textViewInfo.setText("");
                }
        }
    }
}
