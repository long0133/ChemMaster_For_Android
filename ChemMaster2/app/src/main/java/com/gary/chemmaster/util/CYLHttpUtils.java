package com.gary.chemmaster.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by gary on 2016/11/10.
 */
public class CYLHttpUtils {

    /*
* 获得GET请求输入流
* */
    public static InputStream get(String path) throws IOException
    {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream is = conn.getInputStream();
        return is;
    }

    public static String getJsonString(InputStream inputStream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder result = new StringBuilder();


        while ((line = reader.readLine()) != null)
        {
            result.append(line);

        }

        return result.toString();
    }
}
