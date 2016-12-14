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
 * Created by gary on 16/11/21.
 */
public class CYLTotalSynDao {

    private Context context;
    private String totalSynthesisListTable = CYLDBOpenHelper.TOTAL_SYNTHESIS_LIST_TABLE;
    private static CYLTotalSynDao totalSynDao;

    private CYLTotalSynDao(Context context) {
        this.context = context;
    }

    public static CYLTotalSynDao getInstance(Context context)
    {
         totalSynDao = new CYLTotalSynDao(context);
        return totalSynDao;
    }

    public List<CYLReactionDetail> getAllTotalSynReaction()
    {
        List<CYLReactionDetail> list = new ArrayList<>();

        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_COMMON,totalSynthesisListTable);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(totalSynthesisListTable,new String[]{"id",
                "desc",
                "name ",
                "urlpath" ,
                "year",
                "author",
                "bitmap",
                "typenum"},null,null,null,null,null);

        while (cursor.moveToNext())
        {
            CYLReactionDetail reaction = new CYLReactionDetail();

            reaction.setDesc(cursor.getString(1));
            reaction.setName(cursor.getString(2));
            reaction.setUrlPath(cursor.getString(3));
            reaction.setYear(cursor.getString(4));
            reaction.setAuthor(cursor.getString(5));
            reaction.setPicture(cursor.getBlob(6));

            list.add(reaction);
        }

        cursor.close();

        db.close();

        return list;
    }


    public CYLReactionDetail getTotalSynReaction(String selection, String[] selectionArgs )
    {

        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_COMMON,totalSynthesisListTable);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(totalSynthesisListTable,new String[]{"id",
                "desc",
                "name",
                "urlpath",
                "year",
                "author",
                "bitmap",
                "typenum"},selection,selectionArgs,null,null,null);

        CYLReactionDetail reaction = null;

        while (cursor.moveToNext())
        {
            reaction.setDesc(cursor.getString(1));
            reaction.setName(cursor.getString(2));
            reaction.setUrlPath(cursor.getString(3));
            reaction.setYear(cursor.getString(4));
            reaction.setAuthor(cursor.getString(5));
            reaction.setPicture(cursor.getBlob(6));
            reaction.setTypeNum(CYLReactionDetail.IS_FOR_TOTAL_SYN);
        }

        cursor.close();

        db.close();

        return reaction;
    }


    public long insertNameReaction(CYLReactionDetail reaction)
    {
        CYLDBOpenHelper helper = new CYLDBOpenHelper(context, CYLChemApplication.SQlite_DataBase_COMMON,totalSynthesisListTable);
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("desc",reaction.getDesc());
        values.put("name", reaction.getName());
        values.put("urlpath", reaction.getUrlPath());
        values.put("year", reaction.getYear());
        values.put("author",reaction.getAuthor());
        values.put("bitmap", reaction.getPicture());
        values.put("typenum",reaction.getTypeNum());

        long id = db.insert(totalSynthesisListTable,null, values);

        db.close();

        return id;
    }

}
