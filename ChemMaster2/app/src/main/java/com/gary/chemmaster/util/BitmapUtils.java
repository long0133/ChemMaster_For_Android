package com.gary.chemmaster.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gary on 2016/11/8.
 */
public class BitmapUtils {

    /*保存bitmap到cache中*/
    public static void bitmapSave(Bitmap bitmap, File file) throws IOException
    {
        if(!file.getParentFile().exists())
        {
            file.getParentFile().mkdir();
        }

        FileOutputStream out = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
    }

    /*从cache中取出bitmap*/
    public static Bitmap bitmapLoad(File file)
    {
        if (!file.exists())
        {
            return  null;
        }
        else
        {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
    }


}
