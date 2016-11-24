package com.gary.chemmaster.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.adapter.CYLTotalSynAlertAdapter;
import com.gary.chemmaster.entity.CYLReactionDetail;

import java.util.ArrayList;


/**
 * Created by gary on 16/11/21.
 */
public class CYLShowDetailDialog extends Dialog{

    ArrayList<CYLReactionDetail> data;
    TextView dialogTv;
    ListView dialogLV;


    public CYLShowDetailDialog(Context context,ArrayList<CYLReactionDetail>data) {
        super(context);
        this.data = data;
    }

    public CYLShowDetailDialog(Context context, int themeResId, Context context1) {
        super(context, themeResId);
        context = context1;
    }

    public CYLShowDetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener, Context context1) {
        super(context, cancelable, cancelListener);
        context = context1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*除去dialog的title框*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.show_detail_dialog,null);

        /*计算合适的大小*/
        Window dialogW = getWindow();
        WindowManager.LayoutParams lp = dialogW.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics();
        lp.height = (int)(d.heightPixels*0.4);
        lp.width = (int)(d.widthPixels*0.4);
        lp.gravity = Gravity.CENTER;
        dialogW.setAttributes(lp);

        dialogTv = (TextView)dialogView.findViewById(R.id.dialog_nameTV);
        dialogLV = (ListView)dialogView.findViewById(R.id.dialog_show_lv);

        if (data.get(0).getTypeNum() == CYLReactionDetail.IS_FOR_TOTAL_SYN)
        {
            /*显示全合成时的标题设置*/
            dialogTv.setText(data.get(0).getName().charAt(0)+"");
        }
        else if (data.get(0).getTypeNum() == CYLReactionDetail.IS_FOR_HIGH_LIGHT)
        {
            String year = data.get(0).getHighLightYearUrl();
            String yearTitle = "Total Synthesis Of Year " + year.substring(year.indexOf("2"),year.lastIndexOf("/"));
            dialogTv.setTextSize(20);
            dialogTv.setText(yearTitle);
        }

        dialogLV.setAdapter(new CYLTotalSynAlertAdapter(data,getContext()));

        setContentView(dialogView);

    }

    public void setListItemClickListener(AdapterView.OnItemClickListener listener)
    {
        this.dialogLV.setOnItemClickListener(listener);
    }

    public ArrayList<CYLReactionDetail> getData() {
        return data;
    }
}
