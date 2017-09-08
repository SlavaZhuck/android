package com.example.juk_va.sherlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class InvestigateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigate);
    }

    public final static String THIEF = "com.example.juk_va.sherlock.THIEF";
    public void onRadioClick(View view) {
        Intent answewIntent = new Intent();

        switch (view.getId()){
            case R.id.radioDog:
                answewIntent.putExtra(THIEF,"пёсик");
                break;
            case R.id.radioCrow:
                answewIntent.putExtra(THIEF,"Ворона");
                break;
            case R.id.radioCat:
                answewIntent.putExtra(THIEF,"лошадь");
                break;
            default:
                break;
        }

        setResult(RESULT_OK, answewIntent);
        finish();
    }
}
