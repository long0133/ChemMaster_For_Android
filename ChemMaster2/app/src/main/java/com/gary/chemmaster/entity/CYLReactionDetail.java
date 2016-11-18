package com.gary.chemmaster.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.gary.chemmaster.Dao.CYLNameReactionDao;

/**
 * Created by gary on 16/11/16.
 */
public class CYLReactionDetail implements Parcelable {

    private byte[] Picture;
    private String Desc;
    private String UrlPath;
    private String name;
    public static final Parcelable.Creator<CYLReactionDetail> CREATOR
            = new Parcelable.Creator<CYLReactionDetail>(){

        @Override
        public CYLReactionDetail createFromParcel(Parcel source) {
            CYLReactionDetail detail = new CYLReactionDetail();

            detail.setUrlPath(source.readString());
            detail.setName(source.readString());

            return detail;
        }

        @Override
        public CYLReactionDetail[] newArray(int size) {
            return new CYLReactionDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

//        dest.writeString(getDesc());
        dest.writeString(getUrlPath());
        dest.writeString(name);

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(byte[] picture) {
        Picture = picture;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setUrlPath(String urlPath) {
        UrlPath = urlPath;
    }


    public String getName() {
        return name;
    }

    public byte[] getPicture() {
        return Picture;
    }

    public String getDesc() {
        return Desc;
    }

    public String getUrlPath() {
        return UrlPath;
    }
}
