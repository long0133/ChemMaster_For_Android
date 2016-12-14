package com.gary.chemmaster.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.CallBack.CYLshowListCallBack;
import com.gary.chemmaster.R;
import com.gary.chemmaster.activity.ShowPicListActivity;
import com.gary.chemmaster.activity.WebViewActivity;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLChemTool;
import com.gary.chemmaster.entity.CYLReactionDetail;
import com.gary.chemmaster.model.CYLHttpManager;
import com.gary.chemmaster.ui.CYLLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLSearchFragement extends Fragment implements View.OnClickListener{

    private EditText et_compound;
    private ImageButton searchBtn;
    private Button showNameRBtn;
    private Button showTotalSBtn;
    private Button showHighLBtn;
    private Button showChemRBtn;


    public CYLSearchFragement() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,null);

        initView(view);

        return view;
    }

    private void initView(View view)
    {
        et_compound = (EditText)view.findViewById(R.id.edittext_Search);
        searchBtn = (ImageButton)view.findViewById(R.id.searchBtn);
        showNameRBtn = (Button)view.findViewById(R.id.ShowNameReactionBtn);
        showTotalSBtn = (Button)view.findViewById(R.id.ShowTotalSynthesis);
        showHighLBtn = (Button)view.findViewById(R.id.ShowHightLightBtn);
        showChemRBtn = (Button)view.findViewById(R.id.showToolsBtn);

        /*根据屏幕尺寸设置按钮位置*/
         ViewGroup.LayoutParams nameLp = showNameRBtn.getLayoutParams();
        ViewGroup.LayoutParams totalLp = showTotalSBtn.getLayoutParams();
        ViewGroup.LayoutParams highLp = showHighLBtn.getLayoutParams();
        ViewGroup.LayoutParams chemLp = showChemRBtn.getLayoutParams();

        showNameRBtn.setOnClickListener(this);
        showTotalSBtn.setOnClickListener(this);
        showHighLBtn.setOnClickListener(this);
        showChemRBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
    }

    /*查询化合物的按钮点击*/
    /*相应四个模块按钮的点击*/
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ShowNameReactionBtn:
                 /*跳转界面*/
                Intent intent = new Intent(getContext(), ShowPicListActivity.class);
                startActivity(intent);

                CYLHttpManager.setListOfReactionDetail(getContext(),MouleFlag.moduleNameReaction, new CYLshowListCallBack<CYLReactionDetail>() {

                    @Override
                    public void goToShowList(List<CYLReactionDetail> list) {

                        /*发送广播*/
                        Intent broadCast = new Intent(CYLChemApplication.ACTION_PREPARE_TO_SHOW_NAME_REACTIONLIST);
                        broadCast.putParcelableArrayListExtra("info",new ArrayList<CYLReactionDetail>(list));
                        getContext().sendBroadcast(broadCast);

                    }

                    /*ignore*/
                    @Override
                    public void showDetailContent(List<String> content,MouleFlag flag) {

                    }


                });

                break;

            case R.id.ShowTotalSynthesis:

                /*跳转界面*/
                final Intent Tintent = new Intent(getContext(), ShowPicListActivity.class);
                startActivity(Tintent);


                CYLHttpManager.setListOfReactionDetail(getContext(), MouleFlag.moduleTotalSynthesis, new CYLshowListCallBack<CYLReactionDetail>() {

                    @Override
                    public void goToShowList(List<CYLReactionDetail> list) {

                        /*发送广播*/
                        Intent broadCast = new Intent(CYLChemApplication.ACTION_PREPARE_TO_SHOW_TOTAL_SYNTHESIS);
                        broadCast.putParcelableArrayListExtra("info",new ArrayList<CYLReactionDetail>(list));
                        getContext().sendBroadcast(broadCast);

                    }

                    /*ignore*/
                    @Override
                    public void showDetailContent(List<String> content, MouleFlag flag) {

                    }
                });
                break;

            case R.id.ShowHightLightBtn:

                /*跳转界面*/
                Intent Hintent = new Intent(getContext(), ShowPicListActivity.class);
                startActivity(Hintent);

                CYLHttpManager.setListOfReactionDetail(getContext(), MouleFlag.moduleHightLight, new CYLshowListCallBack<CYLReactionDetail>() {

                    @Override
                    public void goToShowList(List<CYLReactionDetail> list) {

                        /*发送广播*/
                        Intent broadCast = new Intent(CYLChemApplication.ACTION_PREPARE_TO_SHOW_HIGHTLIGHT);
                        broadCast.putParcelableArrayListExtra("info",new ArrayList<CYLReactionDetail>(list));
                        getContext().sendBroadcast(broadCast);
                    }

                    /*ignore*/
                    @Override
                    public void showDetailContent(List<String> content, MouleFlag flag) {

                    }
                });
                break;

            case R.id.showToolsBtn:
                Intent Toolintent = new Intent(getContext(),ShowPicListActivity.class);
                startActivity(Toolintent);

                CYLHttpManager.setListOfChemTool(getContext(), new CYLshowListCallBack<CYLChemTool>() {
                    @Override
                    public void goToShowList(List<CYLChemTool> list) {

                        /*发送广播*/
                        Intent broadCast = new Intent(CYLChemApplication.ACTION_PREPARE_TO_SHOW_CHEMTOOL);
                        broadCast.putParcelableArrayListExtra("info",new ArrayList<CYLChemTool>(list));
                        getContext().sendBroadcast(broadCast);

                    }

                    /*ignore*/
                    @Override
                    public void showDetailContent(List<String> content, MouleFlag flag) {

                    }
                });
                break;

            case R.id.searchBtn:
                String et = et_compound.getText().toString();
                if (et.length() == 0){
                    return;
                }
                String searchUrl = "http://chanpin.molbase.cn/search/?search_keyword="+et;

                Intent intent1 = new Intent(getContext(), WebViewActivity.class);
                intent1.putExtra("url",searchUrl);
                startActivity(intent1);

                break;
        }

    }
}
