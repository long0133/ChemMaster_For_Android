package com.gary.chemmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.entity.CYLChemTool;

import java.util.ArrayList;

/**
 * Created by gary on 16/11/28.
 */
public class CYLChemToolAdapter extends BaseAdapter {


    ArrayList<CYLChemTool> tools;
    Context context;
    LayoutInflater inflate;

    public CYLChemToolAdapter(Context context, ArrayList<CYLChemTool> tools) {
        this.tools = tools;
        this.context = context;
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tools.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            convertView = inflate.inflate(R.layout.list_reaction_detail_item,null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.detailTitle);

        textView.setText(tools.get(position).getTitle());

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
