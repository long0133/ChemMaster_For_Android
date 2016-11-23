package com.gary.chemmaster.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.entity.CYLReactionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 16/11/21.
 */
public class CYLTotalSynAlertAdapter extends BaseAdapter {

    ArrayList<CYLReactionDetail> data;
    Context context;
    LayoutInflater inflater;

    public CYLTotalSynAlertAdapter(ArrayList<CYLReactionDetail> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CYLReactionDetail detail = data.get(position);
        ViewHolder holder = new ViewHolder();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.dialog_list_item,null);
            holder.name = (TextView)convertView.findViewById(R.id.dialog_item_name);
            holder.year = (TextView)convertView.findViewById(R.id.dialog_item_year);
            holder.author = (TextView)convertView.findViewById(R.id.dialog_item_author);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.name.setText(detail.getName());
        holder.year.setText(detail.getYear());
        holder.author.setText(detail.getAuthor());

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
        TextView name;
        TextView year;
        TextView author;
    }

}
