package com.gary.chemmaster.entity;

import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.Dao.CYLNameReactionDao;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gary on 16/11/16.
 */
public class CYLReactionDetail implements Parcelable {

    private byte[] Picture;
    private String Desc;
    private String UrlPath;
    private String name;
    /*高亮反应的yearUrl导向这一年的所有高亮文章*/
    private String HighLightYearUrl;
    /* 全合成中文章的年限*/
    private String year;
    private String month;
    private String author;
    private int typeNum;
    public static int IS_FOR_NAME_REACTION = 0;
    public static  int IS_FOR_TOTAL_SYN = 1;
    public static  int IS_FOR_HIGH_LIGHT = 2;

    public static final Parcelable.Creator<CYLReactionDetail> CREATOR
            = new Parcelable.Creator<CYLReactionDetail>(){

        @Override
        public CYLReactionDetail createFromParcel(Parcel source) {
            CYLReactionDetail detail = new CYLReactionDetail();

            detail.setUrlPath(source.readString());
            detail.setName(source.readString());
            detail.setYear(source.readString());
            detail.setAuthor(source.readString());
            detail.setTypeNum(source.readInt());
            detail.setHighLightYearUrl(source.readString());
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
        dest.writeInt(typeNum);
        dest.writeString(getHighLightYearUrl());

    }


    public void setMonth(String month) {
        this.month = month;
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


    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public void setHighLightYearUrl(String highLightYearUrl) {
        HighLightYearUrl = highLightYearUrl;
    }

    public String getHighLightYearUrl() {
        return HighLightYearUrl;
    }

    public int getTypeNum() {
        return typeNum;
    }

    public String getMonth() {
        return month;
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

    @Override
    public String toString() {
        return "CYLReactionDetail{" +
                "Picture=" + Arrays.toString(Picture) +
                ", Desc='" + Desc + '\'' +
                ", UrlPath='" + UrlPath + '\'' +
                ", name='" + name + '\'' +
                ", HighLightYearUrl='" + HighLightYearUrl + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", author='" + author + '\'' +
                ", typeNum=" + typeNum +
                '}';
    }
}
