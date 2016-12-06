package com.gary.chemmaster.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.CallBack.CYLshowListCallBack;
import com.gary.chemmaster.R;

import com.gary.chemmaster.activity.ShowPicListActivity;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLReactionDetail;
import com.gary.chemmaster.model.CYLHttpManager;
import com.gary.chemmaster.ui.CYLListView;
import com.gary.chemmaster.util.CYLHtmlParse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by gary on 16/11/29.
 */
public class Brif_NameReacFragment extends Fragment {

    CYLListView nameListV;
    List<CYLReactionDetail> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_brif_name_reaction,null);

        initView(view);

        setListViewContent();

        return view;

    }

    private void initView(View view)
    {
        nameListV = (CYLListView)view.findViewById(R.id.brif_NameReac_lv);
    }

    private void setListViewContent(){

        datas = CYLChemApplication.nameReactionDao.getAllNameReaction();

        if (datas.size() == 0)
        {
            AsyncTask<String,String,List<CYLReactionDetail>> task = new AsyncTask<String, String, List<CYLReactionDetail>>() {
                @Override
                protected List<CYLReactionDetail> doInBackground(String... params) {

                    List<CYLReactionDetail> list = new ArrayList<>();
                    try {
                        CYLHtmlParse parse = new CYLHtmlParse();
                        list = parse.getNameReactionList(list);
                    }catch (IOException e)
                    {

                    }

                    return list;
                }

                @Override
                protected void onPostExecute(List<CYLReactionDetail> list) {

                    Collections.shuffle(list);
                    /*随机选取数据进行显示*/

                    Random random = new Random();
                    int beginIndex = random.nextInt(list.size());
                    int endIndex = beginIndex + 10;

                    while (endIndex >= list.size())
                    {
                        beginIndex = random.nextInt(list.size());
                        endIndex = beginIndex + 10;
                    }

                    datas = list.subList(beginIndex,endIndex);
                    nameListV.setAdapter(new InnerNameReacListAdapter());
                    nameListV.setOnItemClickListener(new InnerNameReacOnIntemClickListener());
                }
            };

            task.execute();
        }
    }

    /*监听器*/
    class InnerNameReacOnIntemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(getContext(), ShowPicListActivity.class);
            startActivity(intent);

            String urlPath = datas.get(position).getUrlPath();
            CYLHttpManager.setDtailContent(getContext(), urlPath, MouleFlag.moduleNameReaction, new CYLshowListCallBack<String>() {

                /*ignore*/
                @Override
                public void goToShowList(List<String> list) {

                }

                @Override
                public void showDetailContent(List<String> content) {

                    Intent broadcast = new Intent(CYLChemApplication.ACTION_DIRECTLY_TO_SHOW_NAME_REACTIONLIST_WITH_CONTENT);
                    broadcast.putStringArrayListExtra("content",new ArrayList<String>(content));
                    getContext().sendBroadcast(broadcast);
                }
            });

        }
    }

    /*adapter*/
    class InnerNameReacListAdapter extends BaseAdapter
    {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return datas.size();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CYLReactionDetail detail = datas.get(position);

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.list_brif_name_react_item,null);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.brif_nameReac_textV);
            textView.setText(detail.getName());

            return convertView;
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
