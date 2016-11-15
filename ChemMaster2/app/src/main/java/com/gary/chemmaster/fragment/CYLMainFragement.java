package com.gary.chemmaster.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gary.chemmaster.CallBack.CYLEditorChoiceCallBack;
import com.gary.chemmaster.CallBack.CYLRecentCallBack;
import com.gary.chemmaster.R;
import com.gary.chemmaster.entity.CYLEditor;
import com.gary.chemmaster.util.ImgaeLoader;
import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;
import com.gary.chemmaster.model.CYLHttpManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLMainFragement extends Fragment {

    private ViewPager pager_editorsuggest;
    private InnerEditorPagerAdapter adapter;
    List<CYLEditor> RecentList;
    List<RelativeLayout> relativeLayouts;
    private ImgaeLoader imgaeLoader;


    public CYLMainFragement() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("cyl","创建mianFragment");
        View view = inflater.inflate(R.layout.fragment_main, null);
        imgaeLoader = new ImgaeLoader(getContext());
        pager_editorsuggest = (ViewPager) view.findViewById(R.id.Viewpager_main_editorsuggest);

        /*回调获得历史内容*/
        CYLHttpManager.getHistory(new CYLEditorChoiceCallBack() {

            @Override
            public List<CYLEditor> getRecentEditorChoice(List<CYLEditor_Doi_Pub> history) {

                /*回调从历史内容中取出需要显示的文献模型*/
                CYLHttpManager.getRecent(history, 2, new CYLRecentCallBack() {
                    @Override
                    public void setRecentCallBack(List<CYLEditor> list) {

                        RecentList = list;

                        setUpImageViews();

                        adapter = new InnerEditorPagerAdapter(relativeLayouts);
                        pager_editorsuggest.setAdapter(adapter);
                        pager_editorsuggest.setOffscreenPageLimit(3);

                    }
                });

                return null;
            }
        });

        return view;
    }


    /*设置显示的RelativeLayout*/
    private void setUpImageViews()
    {
        relativeLayouts = new ArrayList<>();

        for (int i = 0; i < RecentList.size(); i ++)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout rl = (RelativeLayout)inflater.inflate(R.layout.layout_editor_choice,null);
            ImageView imageView = (ImageView) rl.findViewById(R.id.EC_image);
            TextView abs = (TextView) rl.findViewById(R.id.EC_abstract);

            String picPath = RecentList.get(i).getPicPath();
            imgaeLoader.get(picPath, imageView);
//            abs.setText(RecentList.get(i).getArticalAbstract());

            relativeLayouts.add(rl);
        }
    }


    /*编辑推荐pager的adapter*/
    class InnerEditorPagerAdapter extends PagerAdapter
    {

        List<RelativeLayout> imageViews;

        public InnerEditorPagerAdapter(List<RelativeLayout> imageViews) {
            this.imageViews = imageViews;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            RelativeLayout imageView = imageViews.get(position);
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

}
