package com.gary.chemmaster.model;

import android.content.Context;
import android.os.AsyncTask;
import com.gary.chemmaster.CallBack.CYLEditorChoiceCallBack;
import com.gary.chemmaster.CallBack.CYLRecentCallBack;
import com.gary.chemmaster.CallBack.CYLshowListCallBack;
import com.gary.chemmaster.entity.CYLEditor;
import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;

import com.gary.chemmaster.entity.CYLReactionDetail;
import com.gary.chemmaster.util.CYLHtmlParse;
import com.gary.chemmaster.util.JSONParse;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 2016/11/10.

 * 此类用于管理所有的网络处理
 */
public class CYLHttpManager {


    /*异步处理获得编辑推荐历史文章*/
    public static void getHistory(final CYLEditorChoiceCallBack callBack)
    {
        AsyncTask<String,String,List<CYLEditor_Doi_Pub>> task = new AsyncTask<String, String, List<CYLEditor_Doi_Pub>>() {
            @Override
            protected List<CYLEditor_Doi_Pub> doInBackground(String... params) {

                List<CYLEditor_Doi_Pub> history = new ArrayList<>();

                try {
                    JSONParse parse = new JSONParse();
                    history = parse.getAllHistory();
                }
                catch (IOException e)
                {

                }catch (JSONException e)
                {

                }

                return history;

            }

            @Override
            protected void onPostExecute(List<CYLEditor_Doi_Pub> cylEditor_doi_pubs) {

                callBack.getRecentEditorChoice(cylEditor_doi_pubs);

            }
        };

        task.execute();
    }

    /*处理获得最近参数个数的编辑推荐*/
    public static void getRecent(final List<CYLEditor_Doi_Pub>pubs , final int num, final CYLRecentCallBack callBack)
    {

        AsyncTask<String, String, List<CYLEditor>> task = new AsyncTask<String, String, List<CYLEditor>>() {
            @Override
            protected List<CYLEditor> doInBackground(String... params) {

                List<CYLEditor> list = new ArrayList<>();

                try {
                    JSONParse parse = new JSONParse();
                    list = parse.getRecent(pubs,num);
                }
                catch (IOException e)
                {

                }catch (JSONException e)
                {

                }

                return list;
            }

            @Override
            protected void onPostExecute(List<CYLEditor> cylEditors) {

                callBack.setRecentCallBack(cylEditors);

            }
        };

        task.execute();

    }

    /*获得list显示列表*/
    public static void setListOfReactionDetail(final Context context, final CYLshowListCallBack<CYLReactionDetail> callBack)
    {
        AsyncTask<String, String ,List<CYLReactionDetail>> task = new AsyncTask<String, String, List<CYLReactionDetail>>() {
            @Override
            protected List<CYLReactionDetail> doInBackground(String... params) {

                List<CYLReactionDetail> list = new ArrayList<>();

                try {

                    CYLHtmlParse parse = new CYLHtmlParse();
                     list = parse.getNameReactionList(context);
                }
                catch (IOException e)
                {

                }

                return list;
            }

            @Override
            protected void onPostExecute(List<CYLReactionDetail> cylNameReactions) {

                callBack.goToShowList(cylNameReactions);
            }
        };

        task.execute();
    }


    /*获得list显示列表*/
    public static void setDtailContent(final Context context, final String urlPath, final CYLshowListCallBack<String> callBack )
    {
        AsyncTask<String, String ,List<String>> task = new AsyncTask<String, String, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {

                List<String> list = new ArrayList<>();

                try {

                    CYLHtmlParse parse = new CYLHtmlParse();
                    list = parse.getDetailContentForNameReacton(context, urlPath);
                }
                catch (IOException e)
                {

                }

                return list;
            }

            @Override
            protected void onPostExecute(List<String> list) {

                callBack.showDetailContent(list);

            }
        };

        task.execute();
    }

}
