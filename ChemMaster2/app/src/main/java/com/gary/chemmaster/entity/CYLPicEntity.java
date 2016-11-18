package com.gary.chemmaster.entity;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by gary on 16/11/18.
 */
public class CYLPicEntity {

   public Bitmap bitmap;
   public String path;
    ImageView imageView;

    public CYLPicEntity(Bitmap bitmap, String path) {
        this.bitmap = bitmap;
        this.path = path;
    }

    public CYLPicEntity(Bitmap bitmap, String path, ImageView imageView) {
        this.bitmap = bitmap;
        this.path = path;
        this.imageView = imageView;
    }

    public CYLPicEntity() {
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPath() {
        return path;
    }
}
