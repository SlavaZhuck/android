package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.horizontal_scroll_view, container, false);
    }
}
