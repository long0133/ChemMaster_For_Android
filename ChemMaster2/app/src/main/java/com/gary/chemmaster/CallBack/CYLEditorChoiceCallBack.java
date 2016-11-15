package com.gary.chemmaster.CallBack;

import com.gary.chemmaster.entity.CYLEditor;
import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;

import java.util.List;

/**
 * Created by gary on 2016/11/11.
 */
public interface CYLEditorChoiceCallBack {

    List<CYLEditor> getRecentEditorChoice(List<CYLEditor_Doi_Pub> history);

}
