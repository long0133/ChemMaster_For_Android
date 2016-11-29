package com.gary.chemmaster.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gary on 16/11/28.
 */
public class CYLChemTool implements Parcelable{

    String title;
    String urlPath;
    /*指名属于哪个大类*/
    String belongTo;

    public static final Parcelable.Creator<CYLChemTool> CREATOR
            = new Parcelable.Creator<CYLChemTool>(){

        @Override
        public CYLChemTool createFromParcel(Parcel source) {

            CYLChemTool tool = new CYLChemTool(source.readString(),source.readString());

            return tool;
        }

        @Override
        public CYLChemTool[] newArray(int size) {
            return new CYLChemTool[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getTitle());
        dest.writeString(getUrlPath());

    }

    public CYLChemTool() {
    }



    public CYLChemTool(String title, String urlPath) {
        this.title = title;
        this.urlPath = urlPath;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
