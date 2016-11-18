package com.gary.chemmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gary.chemmaster.CallBack.CYLshowListCallBack;
import com.gary.chemmaster.CallBack.CommonCallBack;
import com.gary.chemmaster.R;
import com.gary.chemmaster.adapter.CYLDetailListAdapter;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLPicEntity;
import com.gary.chemmaster.entity.CYLReactionDetail;
import com.gary.chemmaster.model.CYLHttpManager;
import com.gary.chemmaster.util.CYLHttpUtils;
import com.gary.chemmaster.util.ImgaeLoader;
import java.util.ArrayList;
import java.util.List;


public class ShowPicListActivity extends AppCompatActivity {

    private innerBroadCastReceiver receiver;
    private ListView listView;
    private CYLDetailListAdapter adapter;
    private ArrayList<CYLReactionDetail> data;
    private LinearLayout detailLL;
    private ScrollView detailScroll;
    private ImgaeLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic_list);

        Log.d("cyl","进入显示界面");
        setUpBroadCastReceiver();

        initView();
    }

     private void initView(){
         listView = (ListView)findViewById(R.id.ReactionDeatilLv);
         detailLL = (LinearLayout) findViewById(R.id.ScrollContentLL);
         detailScroll = (ScrollView) findViewById(R.id.detailScrollV);

         imageLoader = new ImgaeLoader(this);
     }


    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            detailLL.removeAllViews();

            CYLHttpManager.setDtailContent(ShowPicListActivity.this, data.get(position).getUrlPath(), new CYLshowListCallBack<String>() {

                /*ignore*/
                @Override
                public void goToShowList(List<String> list) {

                }

                @Override
                public void showDetailContent(List<String> content) {

                    /*显示详情页面*/
                    detailScroll.setVisibility(View.VISIBLE);

                    boolean isMeetRecentLiterature = false;

                    for (int i = 0; i < content.size();i++)
                    {
                        String str = content.get(i);

                        if (str.contains("http"))
                        {
                            final ImageView imageView = new ImageView(ShowPicListActivity.this);

                            imageLoader.get(str, imageView, new CommonCallBack() {
                                @Override
                                public Object doSomeThing(final Object o) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            CYLPicEntity picEntity = (CYLPicEntity) o;
                                            ImageView iv = picEntity.getImageView();

                                            /*计算图片放大尺寸*/
                                            DisplayMetrics metrics = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                                            double scale = (metrics.widthPixels * 1.0)/picEntity.bitmap.getWidth();
                                            int height = (int)(picEntity.bitmap.getHeight()*scale);

                                            final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(metrics.widthPixels,height);

                                            if (iv != null)
                                            {
                                                iv.setLayoutParams(lp);

                                                iv.setImageBitmap(picEntity.bitmap);
                                            }
                                        }
                                    });

                                    return null;
                                }
                            });

                            detailLL.addView(imageView);
                        }
                        else
                        {
                            TextView textView = new TextView(ShowPicListActivity.this);

                            /*处理字符串*/
                            if (str.equals("Further Information Literature") || str.contains("Related Reactions")) continue;
                            if (str.equals("Recent Literature")) isMeetRecentLiterature = true;
                            if (isMeetRecentLiterature)
                            {
                                str = "     "+str;
                                textView.setTextSize(15);
                                textView.setTextColor(getResources().getColor(R.color.colorSub));
                            }
                            else
                            {

                                textView.setTextSize(18);
                                str = "     "+str;
                            }


                            textView.setText(str);
                            detailLL.addView(textView);
                            Log.i("cyl",str);
                        }

                    }

                }
            });

        }
    }

    @Override
    public void onBackPressed() {

        if (detailScroll.getVisibility() == View.VISIBLE)
        {
            detailScroll.setVisibility(View.INVISIBLE);
        }
        else
        {
            super.onBackPressed();
        }

    }

    /*设置广播接受者*/
    private void setUpBroadCastReceiver()
    {
        receiver = new innerBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CYLChemApplication.ACTION_PREPARE_TO_SHOW_LIST);
        registerReceiver(receiver,filter);
    }



    /*接受广播*/
    class innerBroadCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            /*接受广播显示传过来的数据*/
            if (intent.getAction().equals(CYLChemApplication.ACTION_PREPARE_TO_SHOW_LIST))
            {
                data = intent.getParcelableArrayListExtra("info");

                adapter = new CYLDetailListAdapter(ShowPicListActivity.this, data);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new InnerOnItemClickListener());
            }

        }
    }
}
