package com.example.juk_va.otherscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText data1Text;
    private EditText data2Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data1Text = (EditText)findViewById(R.id.data1);
        data2Text = (EditText)findViewById(R.id.data2);
    }

    public void onButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);

        intent.putExtra("data1",data1Text.getText().toString());
        intent.putExtra("data2",data2Text.getText().toString());
        //intent.putExtra("data1","asfasf");
        //intent.putExtra("data2","xcbvjgfj");

        startActivity(intent);
    }
}
