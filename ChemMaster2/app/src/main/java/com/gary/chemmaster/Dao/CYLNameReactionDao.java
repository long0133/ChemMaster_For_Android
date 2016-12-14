package com.gary.chemmaster.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLReactionDetail;

import java.util.ArrayList;
import java.util.List;

import sqlite.CYLDBOpenHelper;

/**
 * Created by gary on 16/11/16.
 */
public class CYLNameReactionDao {

    private Context context;
    private String nameReactionTable = CYLDBOpenHelper.NAME_REACTION_LIST_TABLE;
    private static CYLNameReactionDao nameReactionDao;

    private CYLNameReactionDao(Context context) {
        this.context = context;
    }

    public static CYLNameReactionDao getInstance(Context context)
    {
        nameReactionDao = new CYLNameReactionDao(context);
        return nameReactionDao;
    }

    public List<CYLReactionDetail> getAllNameReaction()
    {
        List<CYLReactionDetail> list = new ArrayList<>();

        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_COMMON,nameReactionTable);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(nameReactionTable,new String[]{"id",
                                                                 "name ",
                                                                 "urlpath" },null,null,null,null,null);

        while (cursor.moveToNext())
        {
            CYLReactionDetail reaction = new CYLReactionDetail();

//            reaction.setDesc(cursor.getString(1));
            reaction.setName(cursor.getString(1));
            reaction.setUrlPath(cursor.getString(2));
//            reaction.setPicture(cursor.getBlob(4));

            list.add(reaction);
        }

        cursor.close();

        db.close();

        return list;
    }


    public CYLReactionDetail getNameReaction(String selection, String[] selectionArgs )
    {

        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_COMMON,nameReactionTable);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(nameReactionTable,new String[]{"id",
                "name",
                "urlpath",},selection,selectionArgs,null,null,null);


        CYLReactionDetail reaction = new CYLReactionDetail();

        while (cursor.moveToNext())
        {
//            reaction.setDesc(cursor.getString(1));
            reaction.setName(cursor.getString(1));
            reaction.setUrlPath(cursor.getString(2));
//            reaction.setPicture(cursor.getBlob(4));

        }

        cursor.close();

        db.close();

        return reaction;
    }


    public long insertNameReaction(CYLReactionDetail reaction)
    {
        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_COMMON,nameReactionTable);
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues values = new ContentValues();
//        values.put("desc",reaction.getDesc());
        values.put("name", reaction.getName());
        values.put("urlpath", reaction.getUrlPath());
//        values.put("bitmap", reaction.getPicture());

        long id = db.insert(nameReactionTable,null, values);

        return id;
    }
}
