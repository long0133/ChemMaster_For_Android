package com.gary.chemmaster.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.chemmaster.CallBack.CYLEditorChoiceCallBack;
import com.gary.chemmaster.CallBack.CYLRecentCallBack;
import com.gary.chemmaster.CallBack.CommonCallBack;
import com.gary.chemmaster.R;
import com.gary.chemmaster.adapter.CYLBrifViewPagerAdapter;
import com.gary.chemmaster.entity.CYLEditor;
import com.gary.chemmaster.entity.CYLPicEntity;
import com.gary.chemmaster.ui.CYLViewPager;
import com.gary.chemmaster.util.CommonUtils;
import com.gary.chemmaster.util.ImgaeLoader;
import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;
import com.gary.chemmaster.model.CYLHttpManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.gary.chemmaster.util.ImgaeLoader;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLMainFragement extends Fragment {

    private CYLViewPager pager_editorsuggest;
    private InnerEditorPagerAdapter adapter;
    List<CYLEditor> RecentList;

    private LinearLayout detailLL;
    private ScrollView detailScrollV;
    private ImgaeLoader imageLoader;

    RadioGroup indicator;

    /*******************************最新内容简介的展示VIEWPAGER******************************************/
    List<LinearLayout> relativeLayouts;
    private ImgaeLoader imgaeLoader;
    private Timer picTimer;

    private static final int TIMER_CYCLE_PIC = 0;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case TIMER_CYCLE_PIC:
                    pager_editorsuggest.setCurrentItem(((pager_editorsuggest.getCurrentItem()+1)%relativeLayouts.size()),true);
                    break;
            }
        }
    };


    public CYLMainFragement() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("cyl","创建mianFragment");
        View view = inflater.inflate(R.layout.fragment_main, null);
        imgaeLoader = new ImgaeLoader(getContext());

       initView(view);
        initBrifView(view);

        getInfoToShow();


        return view;
    }

    private void initView(View view)
    {
        indicator = (RadioGroup) view.findViewById(R.id.indicator);
        indicator.setVisibility(View.INVISIBLE);
        pager_editorsuggest = (CYLViewPager) view.findViewById(R.id.Viewpager_main_editorsuggest);
        detailLL = (LinearLayout) view.findViewById(R.id.detailContentLLForMain);
        detailScrollV = (ScrollView) view.findViewById(R.id.detailScrollVForMain);

        imageLoader = new ImgaeLoader(getContext());

        detailScrollV.setVisibility(View.INVISIBLE);

        indicator.check(0);
    }


    /*设置Timer显示图片轮播器*/
    private void setPicTimer()
    {
        picTimer = new Timer();
        picTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message.obtain(handler,TIMER_CYCLE_PIC).sendToTarget();
            }
        }, 3000,3000);

    }

    private void stopTimer()
    {
        if(picTimer!=null)
        {
            picTimer.cancel();
            picTimer = null;
        }
    }

    /*获得图片轮播器的显示内容*/
    private void getInfoToShow()
    {
        /*回调获得历史内容*/
        CYLHttpManager.getHistory(new CYLEditorChoiceCallBack() {

            @Override
            public List<CYLEditor> getRecentEditorChoice(List<CYLEditor_Doi_Pub> history) {

                /*回调从历史内容中取出需要显示的文献模型*/
                /*在进行显示数目更新时，需要删除preference以及数据库*/
                CYLHttpManager.getRecent(history, 5, new CYLRecentCallBack() {
                    @Override
                    public void setRecentCallBack(List<CYLEditor> list) {

                        RecentList = list;

                        setUpImageViews();

                        adapter = new InnerEditorPagerAdapter(relativeLayouts);
                        pager_editorsuggest.setAdapter(adapter);
                        pager_editorsuggest.setOffscreenPageLimit(3);
                        pager_editorsuggest.setOnPageChangeListener(new OnEditorPagerChangerListener());
                        setPicTimer();
                    }
                });

                return null;
            }
        });
    }


    /*设置显示的RelativeLayout*/
    private void setUpImageViews()
    {
        relativeLayouts = new ArrayList<>();

        for (int i = 0; i < RecentList.size(); i ++) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout rl = (LinearLayout) inflater.inflate(R.layout.layout_editor_choice, null);
            final ImageView imageView = (ImageView) rl.findViewById(R.id.EC_image);
            TextView abs = (TextView) rl.findViewById(R.id.EC_abstract);

            /*加载图片*/
            String picPath = RecentList.get(i).getPicPath();
            imgaeLoader.get(picPath, imageView, new CommonCallBack() {
                @Override
                public Object doSomeThing(Object o) {

                    final CYLPicEntity picEntity = (CYLPicEntity) o;

                    final Bitmap bitmap = picEntity.bitmap;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            picEntity.getImageView().setImageBitmap(bitmap);
                        }
                    });
                    return null;
                }
            });


            /*加载文字*/
            String lable = RecentList.get(i).getArticalAbstract();
            lable = "   " + Html.fromHtml(lable).toString();
            abs.setText(lable);

            /*显示indicator*/
            indicator.setVisibility(View.VISIBLE);

            relativeLayouts.add(rl);
        }
    }

    /*编辑推荐pager的adapter*/
    class InnerEditorPagerAdapter extends PagerAdapter
    {

        List<LinearLayout> imageViews;

        public InnerEditorPagerAdapter(List<LinearLayout> imageViews) {
            this.imageViews = imageViews;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LinearLayout imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(imageViews.get(position));
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    /*监听器 : 实现定时器图片循环轮播*/
    class OnEditorPagerChangerListener implements ViewPager.OnPageChangeListener
    {
        /*是否为用户拖拽导致的pager滑动*/
        boolean onUserScrolling;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

            /*仅有用户拖拽才会触发dragging， 而其他两种状态系统用户的操作都会触发*/
            switch (state)
            {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    stopTimer();
                    onUserScrolling = true;
                    break;

                case ViewPager.SCROLL_STATE_IDLE:

                    if (onUserScrolling)
                    {
                        setPicTimer();
                    }
                    onUserScrolling = false;

                    /*修正indicator*/
                    indicator.check(pager_editorsuggest.getCurrentItem()+1);

                    break;
            }

        }
    }


    /*******************************最新内容简介的展示VIEWPAGER******************************************/
    private CYLViewPager Brif_ViewPager;
    private  RadioGroup Brif_RadioGroup;

    private void initBrifView(View view)
    {
        Brif_ViewPager = (CYLViewPager) view.findViewById(R.id.brifViewPager);
        Brif_RadioGroup = (RadioGroup) view.findViewById(R.id.brif_RadioGroup);

        Brif_RadioGroup.setOnCheckedChangeListener(new InnerBrifRadioGroupOnCheckedListener());
        Brif_ViewPager.setOnPageChangeListener(new InnerBrifViewPagerChangeListener());
        Brif_ViewPager.setOffscreenPageLimit(2);

        setAdapterForBrif();
    }

    private void setAdapterForBrif()
    {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new Brif_HighLightFragment());
        fragments.add(new Brif_TotalSynFrangment());
        fragments.add(new Brif_NameReacFragment());

        Brif_ViewPager.setAdapter(new CYLBrifViewPagerAdapter(getFragmentManager(),fragments));
    }

    class InnerBrifViewPagerChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position)
            {
                case 0:
                    Brif_RadioGroup.check(R.id.brif_highlight);
                    break;

                case 1:
                    Brif_RadioGroup.check(R.id.brif_totalSynthesis);
                    break;

                case 2:
                    Brif_RadioGroup.check(R.id.brif_reaction);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class InnerBrifRadioGroupOnCheckedListener implements RadioGroup.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId)
            {
                case R.id.brif_highlight:
                    Brif_ViewPager.setCurrentItem(0);
                    break;

                case R.id.brif_totalSynthesis:
                    Brif_ViewPager.setCurrentItem(1);
                    break;

                case R.id.brif_reaction:
                    Brif_ViewPager.setCurrentItem(2);
                    break;
            }

        }
    }

}
