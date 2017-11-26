package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Juk_VA on 23.11.2017.
 */

public class EveningFragment extends Fragment {
    private static EveningFragment m_A = new EveningFragment();

    static EveningFragment getEveningFragment() {
        return m_A;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.evening_fragment, container, false);
        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/waheed.otf");
        TextView tv = (TextView) view.findViewById(R.id.textEvening);
        tv.setTypeface(myTypeface);
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

