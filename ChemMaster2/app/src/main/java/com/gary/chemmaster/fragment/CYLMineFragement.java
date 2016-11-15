package com.gary.chemmaster.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gary.chemmaster.R;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLMineFragement extends Fragment {

    public CYLMineFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine,null);


        return view;
    }
}
