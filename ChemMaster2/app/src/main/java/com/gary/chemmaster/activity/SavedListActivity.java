package com.gary.chemmaster.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gary.chemmaster.R;
import com.gary.chemmaster.adapter.CYLSavedListAdapter;
import com.gary.chemmaster.app.CYLChemApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavedListActivity extends AppCompatActivity {

    public static String titleName;
    private LinearLayout saveLL;
    private ListView saved_LiastV;
    private TextView saved_Title;
    private File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list);

        initView();

    }


    private void initView()
    {
        saveLL = (LinearLayout) findViewById(R.id.savedLinearLayout);
        saved_LiastV = (ListView) findViewById(R.id.saved_list);
        saved_Title = (TextView) findViewById(R.id.title_saved);

        saved_LiastV.setAdapter(new CYLSavedListAdapter(getFilesName(), SavedListActivity.this));

        saved_LiastV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent intent = new Intent(SavedListActivity.this, ShowPicListActivity.class);
                startActivity(intent);

                new Thread(){

                    @Override
                    public void run() {

                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {

                        }

                        Intent broadCast = new Intent(CYLChemApplication.ACTION_DIRECTLY_TO_SHOW_HIGHLIGHT_WITH_CONTENT);
                        broadCast.putStringArrayListExtra("content", getContentFromFile(files[position]));
                        sendBroadcast(broadCast);

                    }
                }.start();

            }
        });
    }


    private ArrayList<String> getContentFromFile(File file)
    {
        ArrayList<String> content = new ArrayList<>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String a;
            StringBuilder str = new StringBuilder();

            while ( (a = reader.readLine()) != null)
            {
                str.append(a);
            }

            String[] strs = str.toString().split("&");

            for (String subStr : strs)
            {
                content.add(subStr);
            }

        }
        catch (IOException e)
        {

        }

        return content;
    }

    private String[] getFilesName()
    {
        files = filesWithType(titleName);

        String[] names = new String[files.length];

        if (files != null && files.length != 0)
        {
            for (int i = 0; i < files.length; i ++)
            {
                names[i] = files[i].getName();
            }
        }

        return names;
    }

    private File[] filesWithType(String type)
    {
        File cache = getCacheDir();

        if (type.equals("NameReaction"))
        {
            File[] files = cache.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    if (pathname.getName().contains("NameReaction"))
                        return true;

                    return false;
                }
            });

            return files;
        }
        else if (type.equals("TotalSynthesis"))
        {
            File[] files = cache.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    if (pathname.getName().contains("TotalSynthesis"))
                        return true;

                    return false;
                }
            });

            return files;
        }
        else
        {
            File[] files = cache.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {

                    if (pathname.getName().contains("HighLight"))
                        return true;

                    return false;
                }
            });

            return files;
        }
    }
}
