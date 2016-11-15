package com.gary.chemmaster.entity;

/**
 * Created by gary on 2016/11/10.
 */

/*EditorChoice的历史信息实体, 通过doi可以找到对用的文献*/
public class CYLEditor_Doi_Pub {

    String id;
    String PubDate;
    String doi;

    public CYLEditor_Doi_Pub() {
    }

    public CYLEditor_Doi_Pub(String pubDate, String doi) {
        PubDate = pubDate;
        this.doi = doi;
    }

    public String getPubDate() {
        return PubDate;
    }

    public String getDoi() {
        return doi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPubDate(String pubDate) {
        PubDate = pubDate;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    @Override
    public String toString() {
        return "CYLEditor_Doi_Pub{" +
                "PubDate='" + PubDate + '\'' +
                ", doi='" + doi + '\'' +
                '}';
    }
}
