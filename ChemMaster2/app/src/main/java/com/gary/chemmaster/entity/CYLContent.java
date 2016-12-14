package com.gary.chemmaster.entity;

import com.gary.chemmaster.CYLEnum.MouleFlag;

import java.util.List;

/**
 * Created by gary on 16/12/12.
 */
public class CYLContent {

    List<String> content;
    MouleFlag flag;

    public CYLContent() {
    }

    public CYLContent(List<String> content, MouleFlag flag) {
        this.content = content;
        this.flag = flag;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public void setFlag(MouleFlag flag) {
        this.flag = flag;
    }

    public List<String> getContent() {
        return content;
    }

    public MouleFlag getFlag() {
        return flag;
    }
}
