package com.gary.chemmaster.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.R;
import com.gary.chemmaster.entity.CYLContent;
import com.gary.chemmaster.entity.CYLReactionDetail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gary on 16/12/12.
 */
public class CYLEditDialog extends Dialog {

    CYLContent content;
    String detailName;
    TextView title;
    EditText editText;
    Button confirmBtn;
    Button cancleBtn;

    public CYLEditDialog(Context context,CYLContent content,String name) {
        super(context);
        this.content = content;
        this.detailName = name;
    }

    public CYLEditDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CYLEditDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_edit_dialog,null);

        title = (TextView) view.findViewById(R.id.title_dialog);
        editText = (EditText) view.findViewById(R.id.edittext_dialog);
        confirmBtn = (Button) view.findViewById(R.id.confirmBtn_dialog);
        cancleBtn = (Button) view.findViewById(R.id.cancleBtn_dialog);

        editText.setHint(detailName);

        if (content.getFlag().equals(MouleFlag.moduleNameReaction)) title.setText("Name Reaction");
        if (content.getFlag().equals(MouleFlag.moduleTotalSynthesis)) title.setText("Total Synthesis");
        if (content.getFlag().equals(MouleFlag.moduleHightLight)) title.setText("HighLight");

        confirmBtn.setOnClickListener(new InnerConfirmBtnListener());
        cancleBtn.setOnClickListener(new InnerCancleBtnListener());

        setContentView(view);
    }

    class InnerConfirmBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            String name = editText.getText().toString();
            if (name.length() == 0)
            {
                name = detailName;
            }

            File cache = getContext().getCacheDir();
            String filePath = "";

            if (content.getFlag().equals(MouleFlag.moduleNameReaction)) filePath = cache+"/"+"NameReaction_"+name;
            if (content.getFlag().equals(MouleFlag.moduleTotalSynthesis))  filePath = cache+"/"+"TotalSynthesis_"+name;
            if (content.getFlag().equals(MouleFlag.moduleHightLight))  filePath = cache+"/"+"HighLight_"+name;

            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

                for (String str : content.getContent())
                {
                    writer.write(str + "&");
                    writer.flush();
                }

            }
            catch (IOException e)
            {

            }

            Toast.makeText(getContext(),"存储完成",Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    class InnerCancleBtnListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }


    public void setConfirmOnClickListener(View.OnClickListener listener)
    {
        confirmBtn.setOnClickListener(listener);
    }

    public void setCancleOnClickListener(View.OnClickListener listener)
    {
        cancleBtn.setOnClickListener(listener);
    }

    public void setContent(CYLContent content) {
        this.content = content;
    }

    public void setDetail(String name) {
        this.detailName = name;
    }

}
