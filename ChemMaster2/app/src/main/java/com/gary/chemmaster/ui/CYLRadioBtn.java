package com.gary.chemmaster.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import com.gary.chemmaster.R;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLRadioBtn extends RadioButton {

    public CYLRadioBtn(Context context) {
        super(context);
    }

    public CYLRadioBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CYLRadioBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);
        //drawableSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_rbDrawableTopSize, 50);
        Drawable drawableTop = a.getDrawable(R.styleable.MyRadioButton_rbDrawableTop);

        //释放资源
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(null,drawableTop,null,null);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        if(top != null){
            //这里只要改后面两个参数就好了，一个宽一个是高，如果想知道为什么可以查找源码
            top.setBounds(0,0,130,130);
        }
        setCompoundDrawables(left,top,right,bottom);
    }

}
