package com.gary.chemmaster.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gary.chemmaster.R;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLDrawFragement extends Fragment {

    public CYLDrawFragement() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, null);

        return view;
    }
}
