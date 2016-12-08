package com.gary.chemmaster.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gary on 16/11/29.
 */
public class Brif_HighLightFragment extends Fragment {

    private CYLListView highlight_lv;
    private List<CYLReactionDetail> datas;
    private InnerBrifHighLightAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View ll = inflater.inflate(R.layout.fragment_brif_highlight,null);

        highlight_lv = (CYLListView) ll.findViewById(R.id.brif_highlight_lv);

        setListView();

        return ll;
    }

    private void setListView()
    {
        getInfo();
    }

    private void getInfo()
    {
        AsyncTask<String, String, List<CYLReactionDetail>> task = new AsyncTask<String, String, List<CYLReactionDetail>>() {
            @Override
            protected List<CYLReactionDetail> doInBackground(String... params) {

                List<CYLReactionDetail> list = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String year = sdf.format(System.currentTimeMillis());
                String urlPath = "http://www.organic-chemistry.org/Highlights/"+year+"/index.shtm";

               try
               {
                   list = new CYLHtmlParse().getHighLighOfYear(list,urlPath);
               }catch (IOException e)
               {
                   e.printStackTrace();
               }


                return list;
            }


            @Override
            protected void onPostExecute(List<CYLReactionDetail> list) {

                /*获的最新的高亮*/
                try {
                    datas = list.subList(list.size() - 10,list.size());
                }
                catch (IndexOutOfBoundsException e)
                {
                    Toast.makeText(getContext(),"没有网络连接", Toast.LENGTH_LONG).show();
                }

                if (datas.size() != 0)
                {
                    adapter = new InnerBrifHighLightAdapter();
                    highlight_lv.setAdapter(adapter);
                    highlight_lv.setOnItemClickListener(new InnerBrifHighLightOnItemClickListener());
                }
            }
        };

        task.execute();
    }

    /*adapter*/
    class InnerBrifHighLightAdapter extends BaseAdapter
    {

        LayoutInflater inflater;

        public InnerBrifHighLightAdapter() {;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CYLReactionDetail detail = datas.get(position);

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.list_brif_highlight_item,null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.brif_highlight_textV);
            tv.setText(detail.getName());

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

    class InnerBrifHighLightOnItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(getContext(), ShowPicListActivity.class);
            startActivity(intent);

            String urlPath = datas.get(position).getUrlPath();
            CYLHttpManager.setDtailContent(getContext(), urlPath, MouleFlag.moduleHighLightOfYear, new CYLshowListCallBack<String>() {

                /*ignore*/
                @Override
                public void goToShowList(List<String> list) {

                }

                @Override
                public void showDetailContent(List<String> content) {

                    Intent broadcast = new Intent(CYLChemApplication.ACTION_DIRECTLY_TO_SHOW_HIGHLIGHT_WITH_CONTENT);
                    broadcast.putStringArrayListExtra("content",new ArrayList<String>(content));
                    getContext().sendBroadcast(broadcast);
                }
            });


        }
    }
}