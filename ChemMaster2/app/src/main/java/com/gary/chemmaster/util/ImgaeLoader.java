package com.gary.chemmaster.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by gary on 2016/11/9.
 */
public class ImgaeLoader {

    Context context;
    ListView listView;
    boolean isLoading = true;

    /*加载任务队列*/
    LinkedList<AvatarLoadTask> taskList = new LinkedList<>();
    /*任务加载线程*/
    MusicAvatarLoadThread loadThread;
    /*用软引用指向bitmap,在内存警告时会释放部分软引用的对象*/
    HashMap<String, SoftReference<Bitmap>> avatarCache = new HashMap<>();

    static final int MESSAGE_IMAGE_LOAD = 1;
    android.os.Handler handler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what)
            {
                case MESSAGE_IMAGE_LOAD:
                    AvatarLoadTask task = (AvatarLoadTask) msg.obj;
                    if (task.bitmap != null)
                    {
                        ImageView imageView = null;
                        if (listView != null)
                        {
                            imageView = (ImageView)listView.findViewWithTag(task.path);
                        }

                        if(imageView != null) imageView.setImageBitmap(task.bitmap);

                       /*软引用,存入缓存: 当内存趋于阈值时,GC会释放部分软引用对象*/
                        SoftReference<Bitmap> ref = new SoftReference<Bitmap>(task.bitmap);
                        avatarCache.put(task.path,ref);

                       /*存入文件缓存*/
                        try {

                            BitmapUtils.bitmapSave(ref.get(),new File(context.getCacheDir(),"image"
                                    + task.path.substring(task.path.lastIndexOf("/"))));

                        }
                        catch (IOException e)
                        {

                        }
                    }
                    else
                    {

                    }
                    break;
            }
        }
    };

    public ImgaeLoader(Context context) {
        this.context = context;
        loadThread = new MusicAvatarLoadThread();
        loadThread.start();
    }

    public ImgaeLoader(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        loadThread = new MusicAvatarLoadThread();
        loadThread.start();
    }

    /*异步加载图片,设置到对用view上*/
    public void get(String picPath, ImageView imageView)
    {
         /*从缓存中取出图片*/
        if(avatarCache.containsKey(picPath))
        {
            Log.d("cyl", "从缓存中取出图片");
            SoftReference<Bitmap> ref = avatarCache.get(picPath);
            if (ref != null)
            {
                Bitmap bitmap = ref.get();
                if (bitmap != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
                else
                {

                /*缓存图片已经被GC清除, 则向任务队列中加入新的下载任务*/
                    taskList.add(new AvatarLoadTask(picPath, null));

                    synchronized (loadThread)
                    {
                        loadThread.notify();
                    }
                }
            }
        }
        else
        {
            Log.d("cyl", "从文件缓存中取出图片");
            /*从文件缓存中取出图片*/
            Bitmap bitmap = BitmapUtils.bitmapLoad(new File(context.getCacheDir(),"image"
                    + picPath.substring(picPath.lastIndexOf("/"))));

            if (bitmap != null)
            {
                /*取出了图片,在界面上显示并且加载入缓存*/
                avatarCache.put(picPath, new SoftReference<Bitmap>(bitmap));
                imageView.setImageBitmap(bitmap);

            }else
            {
                /*文件缓存中没有图片, 则向任务队列中加入新的下载任务*/
                taskList.add(new AvatarLoadTask(picPath, null));

                synchronized (loadThread)
                {
                    loadThread.notify();
                }
            }

        }
    }

    public void stopThread()
    {
        isLoading = false;
        synchronized (loadThread)
        {
            loadThread.notify();
        }
    }

    /*子线程进行图片加载任务队列*/
    class MusicAvatarLoadThread extends Thread
    {
        @Override
        public void run() {

            try
            {
                while (isLoading)
                {
                    if(!taskList.isEmpty())
                    {
                        Log.d("cyl", "下载图片");
                        AvatarLoadTask task = taskList.removeFirst();

                        task.bitmap = BitmapFactory.decodeStream(CYLHttpUtils.get(task.path));

                        Message.obtain(handler,MESSAGE_IMAGE_LOAD,task).sendToTarget();
                    }
                    else {
                        synchronized (loadThread)
                        {
                            loadThread.wait();
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }


        }
    }

    class AvatarLoadTask
    {
        String path;
        Bitmap bitmap;

        public AvatarLoadTask(String path, Bitmap bitmap) {
            this.path = path;
            this.bitmap = bitmap;
        }
    }


}
