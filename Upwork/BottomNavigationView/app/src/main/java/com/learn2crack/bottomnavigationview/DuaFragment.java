package com.learn2crack.bottomnavigationview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DuaFragment extends Fragment {

//    public static DuaFragment newInstance() {
//
//        return new DuaFragment();
//    }
    private static DuaFragment m_A = new DuaFragment();

    static DuaFragment getDuaFragment() {
        return m_A;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dua, container, false);
    }
}
