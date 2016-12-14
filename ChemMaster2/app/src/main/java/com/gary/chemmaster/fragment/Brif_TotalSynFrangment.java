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
import java.util.List;
import java.util.Random;

/**
 * Created by gary on 16/11/29.
 */
public class Brif_TotalSynFrangment extends Fragment{

    private CYLListView TotalListV;
    public boolean isLoading = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_brif_total_synthesis,null);

        initView(view);

        setListViewContent(getContext());

        return view;

    }

    private void initView(View view)
    {
        TotalListV = (CYLListView) view.findViewById(R.id.brif_totalSynthesis_listV);
    }

    private void setListViewContent(final Context context)
    {
        AsyncTask<String,String,List<CYLReactionDetail>> task = new AsyncTask<String, String, List<CYLReactionDetail>>() {
            @Override
            protected List<CYLReactionDetail> doInBackground(String... params) {

                List<CYLReactionDetail> datas = new ArrayList<>();

                /*获得一个随机首字母*/
                int random = new Random().nextInt(26)+97;
                char alphaBet = (char)random;

                try
                {
                    CYLHtmlParse parse = new CYLHtmlParse();
                    datas = parse.getRandomTotalSynthesisListOfAlphaBet(context,datas,alphaBet);

                }catch (IOException e)
                {

                }

                return datas;
            }

            @Override
            protected void onPostExecute(List<CYLReactionDetail> list) {

                TotalListV.setAdapter(new InnerTotalSynListAdapter(list));
                TotalListV.setOnItemClickListener(new InnerTotalSynOnItemClickListener(list));

                isLoading = false;

            }
        };

        task.execute();
    }

    /*监听器*/
    class InnerTotalSynOnItemClickListener implements AdapterView.OnItemClickListener
    {

        List<CYLReactionDetail> datas;

        public InnerTotalSynOnItemClickListener(List<CYLReactionDetail> datas) {
            this.datas = datas;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            Intent intent = new Intent(getContext(), ShowPicListActivity.class);
            startActivity(intent);

            CYLHttpManager.setDtailContent(getContext(), datas.get(position).getUrlPath(), MouleFlag.moduleTotalSynthesis, new CYLshowListCallBack<String>() {

                /*ignore*/
                @Override
                public void goToShowList(List<String> list) {

                }

                @Override
                public void showDetailContent(List<String> content,MouleFlag flag) {

                    Intent nameCast = new Intent(CYLChemApplication.ACTION_NAME);
                    nameCast.putExtra("name",datas.get(position).getName());
                    getContext().sendBroadcast(nameCast);

                    Intent BroadCast = new Intent(CYLChemApplication.ACTION_DIRECTLY_TO_SHOW_TOTAL_SYNTHESIS_WITH_CONTENT);
                    BroadCast.putStringArrayListExtra("content",new ArrayList<String>(content));
                    getContext().sendBroadcast(BroadCast);

                }
            });

        }
    }


    /*adapter*/
    class InnerTotalSynListAdapter extends BaseAdapter{

        List<CYLReactionDetail> datas;
        LayoutInflater inflater;

        public InnerTotalSynListAdapter(List<CYLReactionDetail> datas) {
            this.datas = datas;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CYLReactionDetail detail = datas.get(position);
            ViewHolder holder = new ViewHolder();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.list_brif_totalsyn_item,null);
                holder.nameTV = (TextView) convertView.findViewById(R.id.brif_TotalSyn_nametextV);
                holder.authorTV = (TextView) convertView.findViewById(R.id.brif_TotalSyn_authortextV);
                holder.yearTV = (TextView) convertView.findViewById(R.id.brif_TotalSyn_yeartextV);

                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.nameTV.setText(detail.getName());
            holder.authorTV.setText(detail.getAuthor());
            holder.yearTV.setText(detail.getYear());

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

        class ViewHolder{
            TextView nameTV;
            TextView authorTV;
            TextView yearTV;
        }
    }
}
