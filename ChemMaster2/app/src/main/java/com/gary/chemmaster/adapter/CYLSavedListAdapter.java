package com.gary.chemmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gary.chemmaster.R;

/**
 * Created by gary on 16/12/12.
 */
public class CYLSavedListAdapter extends BaseAdapter {

    Context context;
    String[] names;
    LayoutInflater inflater;

    public CYLSavedListAdapter(String[] names, Context context) {
        this.names = names;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.simple_list,null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.simple_textV);

        tv.setText(names[position]);

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
