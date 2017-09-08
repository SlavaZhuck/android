package com.example.admin.svetofor;

import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout mConstraintLayout;
    private TextView mInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConstraintLayout = (ConstraintLayout)findViewById(R.id.Svetophor);
        mInfoTextView = (TextView)findViewById(R.id.textView);
        Button yellowButton = (Button)findViewById(R.id.buttonYellow);
        yellowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mInfoTextView.setText(R.string.yellow);
                mConstraintLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorYellow));
            }
       });
    }



    public void onRedButtonClick(View view) {
        mInfoTextView.setText(R.string.red);
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
    }

    public void onYellowButtonClick(View view) {
        mInfoTextView.setText(R.string.yellow);
    }

    public void onGreenButtonClick(View view) {
        mInfoTextView.setText(R.string.green);
        mConstraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
    }
}
