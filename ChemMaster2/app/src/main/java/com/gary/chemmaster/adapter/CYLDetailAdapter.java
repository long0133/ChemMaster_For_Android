//package com.gary.chemmaster.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Gallery;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.gary.chemmaster.util.CommonUtils;
//import com.gary.chemmaster.util.ImgaeLoader;
//import com.gary.chemmaster.R;
//import com.gary.chemmaster.entity.CYLReactionDetail;
//import com.gary.chemmaster.util.BitmapUtils;
//
//import java.util.List;
//
///**
// * Created by gary on 16/11/17.
// */
//public class CYLDetailAdapter extends BaseAdapter {
//
//    Context context;
//    List<String> data;
//    LayoutInflater inflater;
//    ImgaeLoader imgaeLoader;
//
//
//    public CYLDetailAdapter(Context context, List<String> data) {
//        this.context = context;
//        this.data = data;
//        imgaeLoader = new ImgaeLoader(context);
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        String str = data.get(position);
//
//        if (convertView == null)
//        {
//            convertView = inflater.inflate(R.layout.list_detail_item,null );
//        }
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.detialImage);
//        TextView textView = (TextView) convertView.findViewById(R.id.detialText);
//
//        if (str.contains("http"))
//        {
//            //// TODO: 16/11/17
//            imgaeLoader.get(str,imageView);
//
//
//
//        }
//        else
//        {
//            textView.setText(str);
//        }
//
//
//        return convertView;
//    }
//
//
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//}
