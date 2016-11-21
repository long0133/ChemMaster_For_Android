package com.gary.chemmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import com.gary.chemmaster.adapter.CYLTotalSynAdapter;
import com.gary.chemmaster.adapter.CYLTotalSynAlertAdapter;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLPicEntity;
import com.gary.chemmaster.entity.CYLReactionDetail;
import com.gary.chemmaster.model.CYLHttpManager;
import com.gary.chemmaster.ui.CYLShowDetailDialog;
import com.gary.chemmaster.util.CYLHttpUtils;
import com.gary.chemmaster.util.ImgaeLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ShowPicListActivity extends AppCompatActivity {

    private innerBroadCastReceiver receiver;
    private ListView listView;
    private BaseAdapter adapter;
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

    /*人名反应点击监听器*/
    private class InnerReactiongListOnItemClickListener implements AdapterView.OnItemClickListener
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

    /*全合成点击监听器*/
    private class InnerTotalSynthesisOnItemClickListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            View item = listView.getChildAt(position);
            TextView title = (TextView)item.findViewById(R.id.detailTitle);

            /*从集合中获取显示的数据*/
            ArrayList<CYLReactionDetail> subData = new ArrayList<>();

            Iterator<CYLReactionDetail> iterator = data.iterator();

            while (iterator.hasNext())
            {
                CYLReactionDetail detail = iterator.next();
                if (detail.getName().substring(0,1).contains(title.getText().toString()))
                {
                    subData.add(detail);
                }
            }

            /*单击后跳出alertview显示具体全合成名称*/
            CYLShowDetailDialog detailDialog = new CYLShowDetailDialog(ShowPicListActivity.this, subData);
            detailDialog.show();
        }
    }

    class InnerAlertDialogOnClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {

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
        filter.addAction(CYLChemApplication.ACTION_PREPARE_TO_SHOW_NAME_REACTIONLIST);
        filter.addAction(CYLChemApplication.ACTION_PREPARE_TO_SHOW_TOTAL_SYNTHESIS);
        registerReceiver(receiver,filter);
    }



    /*接受广播*/
    class innerBroadCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            /*接受广播显示传过来的数据*/
            if (intent.getAction().equals(CYLChemApplication.ACTION_PREPARE_TO_SHOW_NAME_REACTIONLIST))
            {
                data = intent.getParcelableArrayListExtra("info");

                adapter = new CYLDetailListAdapter(ShowPicListActivity.this, data);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new InnerReactiongListOnItemClickListener());
            }
            else if (intent.getAction().equals(CYLChemApplication.ACTION_PREPARE_TO_SHOW_TOTAL_SYNTHESIS))
            {
                data = intent.getParcelableArrayListExtra("info");
                String[] alphaB = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T",
                        "U","V","W","X","Y","Z"};
                adapter = new CYLTotalSynAdapter(ShowPicListActivity.this,alphaB);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new InnerTotalSynthesisOnItemClickListener());
            }

        }
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(receiver);

        super.onDestroy();

    }
}
