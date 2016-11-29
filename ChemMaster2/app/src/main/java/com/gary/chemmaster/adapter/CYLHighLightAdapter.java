package com.gary.chemmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.entity.CYLReactionDetail;

import java.util.List;

/**
 * Created by gary on 16/11/24.
 */
public class CYLHighLightAdapter extends BaseAdapter {

    Context context;
    List<CYLReactionDetail> data;
    LayoutInflater inflater;

    public CYLHighLightAdapter(Context context, List<CYLReactionDetail> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_highlight_item,null);
        }

        String year = data.get(position).getHighLightYearUrl();
        String yearTitle = "HighLights Of Year " + year.substring(year.indexOf("2"),year.lastIndexOf("/"));

        TextView tv = (TextView)convertView.findViewById(R.id.highLightYearTV);
        tv.setText(yearTitle);

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
