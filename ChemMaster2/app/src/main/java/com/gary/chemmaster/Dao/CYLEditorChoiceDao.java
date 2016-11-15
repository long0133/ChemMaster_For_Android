package com.gary.chemmaster.Dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLEditor;
import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sqlite.CYLDBOpenHelper;

/**
 * Created by gary on 2016/11/11.
 */
public class CYLEditorChoiceDao {

    private static CYLEditorChoiceDao EditorChoideDao;
    private ContentResolver contentResolver;
    private Context context;
    private String historyTable = CYLDBOpenHelper.EDITOR_CHOICE_HISTORY_TABLE;
    private String recentTable = CYLDBOpenHelper.EDITOR_CHOICE_TABLE;

    private CYLEditorChoiceDao(ContentResolver contentResolver, Context context) {
        this.contentResolver = contentResolver;
        this.context = context;
    }

    public static CYLEditorChoiceDao getInstance(ContentResolver contentResolver, Context context)
    {
        if (EditorChoideDao == null)
        {
            EditorChoideDao = new CYLEditorChoiceDao(contentResolver, context);
        }

        return EditorChoideDao;
    }


    /*获得所有的编辑推荐的历史信息*/
    public ArrayList<CYLEditor_Doi_Pub> getHistoryFromPrivider(String whereClouse, String[] whereArgs)
    {
        ArrayList<CYLEditor_Doi_Pub> pubs = new ArrayList<>();

        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_EditorChoice_History);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {"id","PubDate","doi"};
        String selection = whereClouse;
        String[] selectionArgs = whereArgs;
        Cursor cursor = db.query(historyTable,columns,selection,selectionArgs,null,null,"id ASC");

        if (cursor.moveToFirst())
        {
            for (;!cursor.isAfterLast(); cursor.moveToNext())
            {
                CYLEditor_Doi_Pub pub = new CYLEditor_Doi_Pub();
                pub.setId(cursor.getString(0));
                pub.setPubDate(cursor.getString(1));
                pub.setDoi(cursor.getString(2));

                pubs.add(pub);
            }
        }

        db.close();
        return pubs;
    }

    /*向数据库中添加编辑推荐的历史信息*/
    public long insertHistory(CYLEditor_Doi_Pub pub)
    {
        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_EditorChoice_History);

        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("id",pub.getId());
        values.put("pubDate", pub.getPubDate());
        values.put("doi", pub.getDoi());

        return db.insert(CYLDBOpenHelper.EDITOR_CHOICE_HISTORY_TABLE, null,values);
    }


    /******************************************************************************************************************/

    /*获得所有最近推荐的文章*/
    public List<CYLEditor> getRecentEditorChoice(String whereClouse, String[] whereArgs)
    {
        List<CYLEditor> datas = new ArrayList<>();
        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_EditorChoice);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(recentTable,
                            new String[]{"id","editorsChoicePubDate","journalTitle", "author","title","articalAbstract","doi","picpath"}
                            ,whereClouse,whereArgs,null,null,null);

        while (c.moveToNext())
        {
            CYLEditor editor = new CYLEditor();

            editor.setEditorsChoicePubDate(c.getString(1));
            editor.setJournalTitle(c.getString(2));
            editor.setAuthor(c.getString(3));
            editor.setTitle(c.getString(4));
            editor.setArticalAbstract(c.getString(5));
            editor.setDoi(c.getString(6));
            editor.setPicPath(c.getString(7));

            datas.add(editor);
        }

        if (c != null) c.close();

        return datas;
    }

    public long insertRencetEditorChoice(CYLEditor editor)
    {
        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_EditorChoice);
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("editorsChoicePubDate", editor.getEditorsChoicePubDate());
        values.put("journalTitle", editor.getJournalTitle());
        values.put("author", editor.getAuthor());
        values.put("title",editor.getTitle());
        values.put("articalAbstract", editor.getArticalAbstract());
        values.put("doi", editor.getDoi());
        values.put("picpath", editor.getPicPath());

        return db.insert(CYLDBOpenHelper.EDITOR_CHOICE_TABLE,null,values);
    }
}
