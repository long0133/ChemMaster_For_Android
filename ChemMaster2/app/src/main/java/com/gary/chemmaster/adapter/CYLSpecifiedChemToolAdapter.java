package com.gary.chemmaster.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.entity.CYLChemTool;
import com.gary.chemmaster.entity.CYLReactionDetail;

import java.util.List;

/**
 * Created by gary on 16/11/28.
 */
public class CYLSpecifiedChemToolAdapter extends BaseAdapter {

    Context context;
    List<CYLChemTool> data;
    LayoutInflater inflater;

    public CYLSpecifiedChemToolAdapter(Context context, List<CYLChemTool> data) {
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

            CYLChemTool detail = data.get(position);
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
            holder.name.setText(detail.getTitle());
            holder.name.setTextSize(18);
            holder.year.setText("");
            holder.author.setText("");

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
