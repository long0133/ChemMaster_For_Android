package com.gary.chemmaster.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gary.chemmaster.R;

import java.util.List;

/**
 * Created by gary on 16/11/28.
 */
public class CYLLoadingDialog extends Dialog {

    private LayoutInflater inflater;
    private static CYLLoadingDialog dialog;

    private CYLLoadingDialog(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static private CYLLoadingDialog getInstance(Context context)
    {
        dialog = new CYLLoadingDialog(context);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        LinearLayout loadingDialog = (LinearLayout) inflater.inflate(R.layout.loading_dialog,null);

        ImageView imageView = (ImageView) loadingDialog.findViewById(R.id.loadingImageView);
        TextView textView = (TextView) loadingDialog.findViewById(R.id.loadingTextView);

        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.loading_dialog_rotation);

        imageView.setAnimation(animation);

        setContentView(loadingDialog);

        /*设置加载框的大小*/
        Window dialogW = getWindow();
        WindowManager.LayoutParams lp = dialogW.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics();
        lp.height = (int)(d.heightPixels*0.2);
        lp.width = (int)(d.widthPixels*0.8);
        lp.gravity = Gravity.CENTER;
        dialogW.setAttributes(lp);

        setCanceledOnTouchOutside(false);
    }

    static public void showLoading(Context context)
    {
        getInstance(context).show();
    }

    static public void loadingDismiss()
    {
        dialog.dismiss();
    }
}
