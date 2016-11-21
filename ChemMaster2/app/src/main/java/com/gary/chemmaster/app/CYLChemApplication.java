package com.gary.chemmaster.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.ParcelUuid;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gary.chemmaster.Dao.CYLEditorChoiceDao;
import com.gary.chemmaster.Dao.CYLNameReactionDao;
import com.gary.chemmaster.Dao.CYLTotalSynDao;
import com.gary.chemmaster.util.CYLPreference;

/**
 * Created by gary on 2016/11/11.
 */
public class CYLChemApplication extends Application {

    /*数据库名*/
    public static final String SQlite_DataBase_EditorChoice = "EditorChoice.db";
    public static final String SQlite_DataBase_EditorChoice_History = "EditorChoiceHistory.db";
    public static final String SQlite_DataBase_COMMON = "COMMON.db";

    /*EditorChoice数据管理*/
    public static CYLEditorChoiceDao editorChoiceDao;
    public static CYLNameReactionDao nameReactionDao;
    public static CYLTotalSynDao totalSynDao;

    /*广播action*/
    /*设置显示人名反应*/
    public static String ACTION_PREPARE_TO_SHOW_NAME_REACTIONLIST = "ACTION_PREPARE_TO_SHOW_NAME_REACTION_LIST";
    /*设置显示全合成*/
    public static String ACTION_PREPARE_TO_SHOW_TOTAL_SYNTHESIS= "ACTION_PREPARE_TO_SHOW_TOTAL_SYNTHESIS";

    public static CYLPreference preference;

    @Override
    public void onCreate() {
        super.onCreate();

        editorChoiceDao = CYLEditorChoiceDao.getInstance(getContentResolver(), this);
        nameReactionDao = CYLNameReactionDao.getInstance(this);
        totalSynDao = CYLTotalSynDao.getInstance(this);

        CYLPreference.init(getApplicationContext());
        preference = CYLPreference.getInstance();
    }



}
