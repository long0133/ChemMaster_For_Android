package com.gary.chemmaster.util;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gary.chemmaster.Dao.CYLEditorChoiceDao;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLEditor;
import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 2016/11/10.
 */
public class JSONParse {

    private CYLChemApplication application = new CYLChemApplication();
    private static CYLEditorChoiceDao choiceDao = CYLChemApplication.editorChoiceDao;

    /*偏好设置文件名,存储上次更新时间*/
    private static final String HISTORY_UPDATE_PREFERENCES = "HISTORY_PREFERENCE_DATE";
    /*存储上次json字符串的长度*/
    private static final String JSON_LENGTH_PREFERENCES = "JSON_LENGTH_PREFERENCES";
    private static final String IS_FIRST_Time_UPDATE_PREFERENCES = "IS_FIRST_Time_UPDATE_PREFERENCES";
    private static final String IS_HISTORY_UPDATE_PREFERENCES = "IS_HISTORY_UPDATE_PREFERENCES";

    /*历史表单是否更新*/
    boolean isHistoryUpdated;
    private long ThreeDays = 3 * 24 * 60 * 60 * 1000;

    /*解析json获得历史所有编辑推荐文章的doi
    * 你应该在工作线程中调用此方法
    * 网络耗时过长 三天一更新
    * */
    public List<CYLEditor_Doi_Pub> getAllHistory() throws IOException, JSONException
    {
        Log.d("cyl","获取历史推荐");
        long lastUpadate = 0;
        boolean isFirstTime = true;
        int jsonStrLength = 0;
        List<CYLEditor_Doi_Pub> list = new ArrayList<>();

        /*判断是否三天前更新，以及是否第一次更新*/
        try
        {
            lastUpadate = (Long) CYLChemApplication.preference.getData(HISTORY_UPDATE_PREFERENCES,(long)0);
            isFirstTime = (Boolean) CYLChemApplication.preference.getData(IS_FIRST_Time_UPDATE_PREFERENCES, true);
            jsonStrLength =  (Integer) CYLChemApplication.preference.getData(JSON_LENGTH_PREFERENCES,0);
            isHistoryUpdated = (Boolean) CYLChemApplication.preference.getData(IS_HISTORY_UPDATE_PREFERENCES, true);
        }
        catch (NullPointerException e)
        {
            lastUpadate = 0l;
            isFirstTime = true;
            jsonStrLength = 0;
            isHistoryUpdated = true;
            CYLChemApplication.preference.saveData(HISTORY_UPDATE_PREFERENCES, 0l);
        }

        Log.d("cyl","isHistoryUpdated:" + isHistoryUpdated);
        Log.i("cyl","差值： "+(System.currentTimeMillis() - lastUpadate));
        /*若是跟新为首次更新或者3天前则更新*/
        if (System.currentTimeMillis() - lastUpadate >= ThreeDays)
        {
            Log.i("cyl",(System.currentTimeMillis() - lastUpadate)+" 进行更新 :" + lastUpadate);

            InputStream  is = CYLHttpUtils.get(CYLUrlFactory.getUrlOfAllEditorChoise());
            String jsonStr = CYLHttpUtils.getString(is);
            /*获取jsonstr备份*/
            String strAftertreatment = new String(jsonStr);

            if (!isFirstTime)
            {
                /*不是第一次， 处理获取最新的更新*/
                strAftertreatment = JSONStrTreatment(strAftertreatment,jsonStrLength);
            }
            else
            {
                /*是第一次,获取所有历史信息*/
                CYLChemApplication.preference.saveData(IS_FIRST_Time_UPDATE_PREFERENCES, false);
            }

            CYLChemApplication.preference.saveData(IS_HISTORY_UPDATE_PREFERENCES, true);
            CYLChemApplication.preference.saveData(HISTORY_UPDATE_PREFERENCES, System.currentTimeMillis());
            CYLChemApplication.preference.saveData(JSON_LENGTH_PREFERENCES, jsonStr.length());
            jsonStr = strAftertreatment;

            JSONArray arr = new JSONArray(jsonStr);

            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                CYLEditor_Doi_Pub pub = new CYLEditor_Doi_Pub(obj.getString("editorsChoicePubDate"),obj.getString("doi"));
                choiceDao.insertHistory(pub);
                list.add(pub);
            }

        }
        else
        {
        /*就在数据库中直接取*/
            list = choiceDao.getHistoryFromPrivider(null,null);
            Log.i("cyl","从数据库中存取出：" + list.size());
            CYLChemApplication.preference.saveData(IS_HISTORY_UPDATE_PREFERENCES, false);
        }

        return list;
    }


    /*获得最近文章信息*/
    public List<CYLEditor> getRecent(List<CYLEditor_Doi_Pub> pubs, int num) throws IOException,JSONException {

        List<CYLEditor> list = new ArrayList<>();

        isHistoryUpdated = (Boolean) CYLChemApplication.preference.getData(IS_HISTORY_UPDATE_PREFERENCES,true);

        if(isHistoryUpdated)
        {
            /*历史表单更新了， 应该更新最近推荐*/
            Log.d("cyl","更新最近推荐");

            String jsonStr = CYLHttpUtils.getString(CYLHttpUtils.get(CYLUrlFactory.getUrlOfRencetEditorChoice(pubs, num)));

            JSONArray arr = new JSONArray(jsonStr);

            for (int i = 0; i < arr.length(); i++) {
                CYLEditor editor = new CYLEditor();

                JSONObject object = arr.getJSONObject(i);

                editor.setDoi(object.getString("doi"));
                editor.setArticalAbstract(object.getString("articleAbstract"));
                editor.setAuthor(object.getString("authors"));
                editor.setEditorsChoicePubDate(object.getString("editorsChoicePubDate"));
                editor.setTitle(object.getString("title"));

            /*设置图片*/
                JSONArray picArr = object.getJSONArray("tocGraphics");
                for (int j = 0; j < picArr.length(); j++) {
                    JSONObject pic = picArr.getJSONObject(j);

                    if (pic.getInt("imageOrder") == 0) {
                        String picPath = pic.getString("imageHighResRelativeURL");

                        picPath = "http://pubs.acs.org/editorschoice/" + picPath;

                        editor.setPicPath(picPath);
                    }

                }

            /*设置杂志标题*/
                JSONObject journal = object.getJSONObject("journal");
                editor.setJournalTitle(journal.getString("abbrevJournalTitle"));


            /*去除概论中的标签*/
                String aa = editor.getArticalAbstract();
                editor.setArticalAbstract(aa.substring(3,(aa.length() - 4)));

                list.add(editor);

                /*数据库中添加最新推荐*/
                CYLChemApplication.editorChoiceDao.insertRencetEditorChoice(editor);
            }
        }
        else
        {
            /*数据库中取出最新推荐*/
            Log.d("cyl","数据库中取出最新推荐");
            list = CYLChemApplication.editorChoiceDao.getRecentEditorChoice(null,null);
        }

        return list;
    }

    //处理Json字符串获得最新的部分
    private String JSONStrTreatment(String jsonStr, long lastLength)
    {
        long nowLength = jsonStr.length();
        String updateStr = jsonStr.substring(0, (int)(nowLength - lastLength));
        Log.d("cyl", " 更新json "+ updateStr);
        return updateStr;
    }

}

