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
 * Created by gary on 16/11/17.
 */
public class CYLDetailListAdapter extends BaseAdapter {

    Context context;
    List<CYLReactionDetail> data;
    LayoutInflater inflater;

    public CYLDetailListAdapter(Context context, List<CYLReactionDetail> data) {
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

        CYLReactionDetail detail = data.get(position);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_reaction_detail_item,null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.detailTitle);

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
