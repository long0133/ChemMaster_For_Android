package com.gary.chemmaster.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.activity.SavedListActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLMineFragement extends Fragment implements View.OnClickListener {

    Button myReactBtn;
    Button myTotalBtn;
    Button myHighBtn;


    public CYLMineFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine, null);

        initView(view);

        return view;
    }

    private void initView(View view) {
        myReactBtn = (Button) view.findViewById(R.id.myReactionBtn);
        myTotalBtn = (Button) view.findViewById(R.id.myTotalBtn);
        myHighBtn = (Button) view.findViewById(R.id.myHighLight);


        myReactBtn.setOnClickListener(this);
        myTotalBtn.setOnClickListener(this);
        myHighBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.myReactionBtn:
                SavedListActivity.titleName = "NameReaction";
                break;
            case R.id.myTotalBtn:
                SavedListActivity.titleName = "TotalSynthesis";
                break;
            case R.id.myHighLight:
                SavedListActivity.titleName = "HightLight";
                break;
        }

        Intent intent = new Intent(getActivity(),SavedListActivity.class);
        startActivity(intent);

    }



}
