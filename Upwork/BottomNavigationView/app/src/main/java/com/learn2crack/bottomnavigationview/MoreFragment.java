package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Juk_VA on 26.09.2017.
 */

public class MoreFragment extends Fragment {

//    public static MoreFragment newInstance() {
//
//        return new MoreFragment();
//    }
    private static MoreFragment m_A = new MoreFragment();

    static MoreFragment getMoreFragment() {
        return m_A;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }
}