package com.gary.chemmaster.entity;

import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.Dao.CYLNameReactionDao;

/**
 * Created by gary on 16/11/16.
 */
public class CYLReactionDetail implements Parcelable {

    private byte[] Picture;
    private String Desc;
    private String UrlPath;
    private String name;
    private String year;
    private String author;

    public static final Parcelable.Creator<CYLReactionDetail> CREATOR
            = new Parcelable.Creator<CYLReactionDetail>(){

        @Override
        public CYLReactionDetail createFromParcel(Parcel source) {
            CYLReactionDetail detail = new CYLReactionDetail();

            detail.setUrlPath(source.readString());
            detail.setName(source.readString());
            detail.setYear(source.readString());
            detail.setAuthor(source.readString());


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
        dest.writeString(year);
        dest.writeString(author);

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

    public void setYear(String year) {
        this.year = year;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
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
