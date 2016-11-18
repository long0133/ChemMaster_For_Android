package com.gary.chemmaster.util;

import android.util.Log;

import com.gary.chemmaster.entity.CYLEditor_Doi_Pub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLUrlFactory {

    public static String getUrlOfAllEditorChoise()
    {
        return "http://pubs.acs.org/editorschoice/manuscripts.json?format=lite";
    }

    public static String getUrlOfRencetEditorChoice(List<CYLEditor_Doi_Pub> pubs, int nums)
    {

        StringBuilder builder = new StringBuilder("http://pubs.acs.org/editorschoice/manuscripts.json?find=ByDoiEquals&");

        for (int i = 0; i < nums; i ++)
        {
            CYLEditor_Doi_Pub pub = pubs.get(i);

            builder.append("doi%5B%5D="+pub.getDoi().replace("/","%2F")+"&");
        }


        String s = builder.toString();
        s = s.substring(0,s.length()-1);

        return s;
    }

    public static String getUrlOfAllNameReactionList()
    {
        return "http://www.organic-chemistry.org/namedreactions/";
    }

}
