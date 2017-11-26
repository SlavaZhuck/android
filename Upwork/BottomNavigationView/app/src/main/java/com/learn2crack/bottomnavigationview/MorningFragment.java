package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Admin on 19.11.2017.
 */

public class MorningFragment extends Fragment {
    private static MorningFragment m_A = new MorningFragment();

    static MorningFragment getMorningFragment() {
        return m_A;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.morning_fragment, container, false);
        TextView yourTextView = (TextView) view.findViewById(R.id.textMorning);
        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/waheed.otf");
        yourTextView.setTypeface(myTypeface);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
