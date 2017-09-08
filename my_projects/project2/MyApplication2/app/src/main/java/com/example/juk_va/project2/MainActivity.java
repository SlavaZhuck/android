package com.example.juk_va.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mCrowsCounterButton;
    private int mCount = 0;
    private TextView mInfoTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInfoTextView=(TextView)findViewById(R.id.textView);
        mCrowsCounterButton = (Button)findViewById(R.id.buttonOld);
        mCrowsCounterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCount+=1;
                mInfoTextView.setText("Here are " + mCount + " Crows");
            }
          });
        }

    public void onClick(View view) {
        TextView helloTextVeiw = (TextView)findViewById(R.id.textView);
        helloTextVeiw.setText("Hello Kitty!");
    }
}
