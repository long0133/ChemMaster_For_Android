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

    /*广播action*/
    public static String ACTION_PREPARE_TO_SHOW_LIST = "ACTION_PREPARE_TO_SHOW_LIST";

    public static CYLPreference preference;

    @Override
    public void onCreate() {
        super.onCreate();

        editorChoiceDao = CYLEditorChoiceDao.getInstance(getContentResolver(), this);
        nameReactionDao = CYLNameReactionDao.getInstance(this);

        CYLPreference.init(getApplicationContext());
        preference = CYLPreference.getInstance();
    }



}
