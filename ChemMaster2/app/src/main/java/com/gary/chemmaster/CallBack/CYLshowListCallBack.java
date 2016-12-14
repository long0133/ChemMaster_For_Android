package com.gary.chemmaster.CallBack;

import com.gary.chemmaster.CYLEnum.MouleFlag;

import java.util.List;

/**
 * Created by gary on 16/11/17.
 */
public interface CYLshowListCallBack<T> {

     void goToShowList(List<T> list);

     void showDetailContent(List<String> content, MouleFlag flag);
}
