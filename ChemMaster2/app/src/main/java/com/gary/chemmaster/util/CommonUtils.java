package com.gary.chemmaster.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 16/11/17.
 */
public class CommonUtils<T> {


    /* 图片放大的method */
    public static Bitmap big(Bitmap bmp, double scale)
    {
        int bmpWidth=bmp.getWidth();
        int bmpHeight=bmp.getHeight();

        /* 计算这次要放大的比例 */
       float scaleWidth=(float)(bmpWidth*scale);
        float scaleHeight=(float)(bmpHeight*scale);

    /* 产生reSize后的Bitmap对象 */
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp,0,0,bmpWidth,
                bmpHeight,matrix,true);

        return resizeBmp;
    }

}
