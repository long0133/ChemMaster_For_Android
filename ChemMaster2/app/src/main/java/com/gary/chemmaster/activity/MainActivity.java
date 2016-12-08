package com.gary.chemmaster.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gary.chemmaster.R;
import com.gary.chemmaster.adapter.CYLPagerAdapter;
import com.gary.chemmaster.fragment.CYLDrawFragement;
import com.gary.chemmaster.fragment.CYLMainFragement;
import com.gary.chemmaster.fragment.CYLMineFragement;
import com.gary.chemmaster.fragment.CYLSearchFragement;
import com.gary.chemmaster.ui.CYLMainViewPager;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
                                                                RadioGroup.OnCheckedChangeListener{

    RadioGroup radioGroup;
    RadioButton mainBtn;
    RadioButton searchBtn;
    RadioButton drawBtn;
    RadioButton mineBtn;

    CYLMainViewPager pager;
    CYLPagerAdapter adapter;

    CYLMainFragement mainFragement;
    CYLSearchFragement searchFragement;
    CYLDrawFragement drawFragement;
    CYLMineFragement mineFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        setViewPagerAdapter();

        setListener();
    }

    /*初始化控件*/
    private void initView()
    {
        radioGroup = (RadioGroup)findViewById(R.id.RadioGroup);
        mainBtn = (RadioButton)findViewById(R.id.MainRB);
        searchBtn = (RadioButton)findViewById(R.id.SearchRB);
        drawBtn = (RadioButton)findViewById(R.id.DrawRB);
        mineBtn = (RadioButton)findViewById(R.id.MineRB);
        pager = (CYLMainViewPager)findViewById(R.id.Viewpager);

        mainBtn.setChecked(true);
    }

    /*设置Viewpager的adapter*/
    private void setViewPagerAdapter()
    {
        LinkedList<Fragment> fragments = new LinkedList<>();

        mainFragement = new CYLMainFragement();
        searchFragement = new CYLSearchFragement();
        drawFragement = new CYLDrawFragement();
        mineFragement = new CYLMineFragement();

        fragments.add(mainFragement);
        fragments.add(searchFragement);
        fragments.add(drawFragement);
        fragments.add(mineFragement);

        adapter = new CYLPagerAdapter(getSupportFragmentManager(),fragments);

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);

    }

    /*设置监听器*/
    private void setListener()
    {
        radioGroup.setOnCheckedChangeListener(this);
        pager.setOnPageChangeListener(this);
    }


    /*ViewPager的监听*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        switch (position)
        {
            case 0:
                radioGroup.check(R.id.MainRB);
                break;

            case 1:
                radioGroup.check(R.id.SearchRB);
                break;

            case 2:
                radioGroup.check(R.id.DrawRB);
                break;

            case 3:
                radioGroup.check(R.id.MineRB);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*RadioGroup的监听*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId)
        {
            case R.id.MainRB:
                pager.setCurrentItem(0,true);
                drawFragement.dismissPopWindow();
                break;

            case R.id.SearchRB:
                pager.setCurrentItem(1,true);
                drawFragement.dismissPopWindow();
                break;

            case R.id.DrawRB:
                pager.setCurrentItem(2,true);
                break;

            case R.id.MineRB:
                pager.setCurrentItem(3,true);
                drawFragement.dismissPopWindow();
                break;
        }

    }
}
