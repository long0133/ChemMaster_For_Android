package com.gary.chemmaster.entity;

import android.graphics.Bitmap;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLEditor {

    private String editorsChoicePubDate;
    private String journalTitle;
    private String author;
    private String title;
    private String articalAbstract;
    private String doi;
    private String picPath;


    public CYLEditor() {
    }

    public CYLEditor(String editorsChoicePubDate, String journalTitle, String author, String title, String articalAbstract, String doi) {
        this.editorsChoicePubDate = editorsChoicePubDate;
        this.journalTitle = journalTitle;
        this.author = author;
        this.title = title;
        this.articalAbstract = articalAbstract;
        this.doi = doi;
    }

//

    @Override
    public String toString() {
        return "CYLEditor{" +
                "editorsChoicePubDate='" + editorsChoicePubDate + '\'' +
                ", journalTitle='" + journalTitle + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", articalAbstract='" + articalAbstract + '\'' +
                ", doi='" + doi + '\'' +
                ", pic=" + picPath +
                '}';
    }


    /*setter*/

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public void setEditorsChoicePubDate(String editorsChoicePubDate) {
        this.editorsChoicePubDate = editorsChoicePubDate;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArticalAbstract(String articalAbstract) {
        this.articalAbstract = articalAbstract;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    /*getter*/

    public String getPicPath() {
        return picPath;
    }

    public String getEditorsChoicePubDate() {
        return editorsChoicePubDate;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getArticalAbstract() {
        return articalAbstract;
    }

    public String getDoi() {
        return doi;
    }
}
