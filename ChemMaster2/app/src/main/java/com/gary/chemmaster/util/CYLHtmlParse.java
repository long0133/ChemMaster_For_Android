package com.gary.chemmaster.util;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.Dao.CYLNameReactionDao;
import com.gary.chemmaster.Dao.CYLTotalSynDao;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLReactionDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 16/11/16.
 */
public class CYLHtmlParse {


    CYLChemApplication application;
    private CYLNameReactionDao nameReactionDao = CYLChemApplication.nameReactionDao;

    public CYLHtmlParse() {
        application = new CYLChemApplication();
    }

    public List<CYLReactionDetail> getReactionList(Context context, MouleFlag flag) throws IOException
    {

        List<CYLReactionDetail> list = new ArrayList<>();

        if (flag.equals(MouleFlag.moduleNameReaction))
        {
            list = getNameReactionList(list);
        }
        else if (flag.equals(MouleFlag.moduleTotalSynthesis))
        {
            Log.d("cyl","数据库中加载数据");

            list = CYLChemApplication.totalSynDao.getAllTotalSynReaction();

            if (list.isEmpty())
            {
                list = getTotalSynthesisList(list);
            }

        }

        return list;
    }

    /*获得全合成list*/
    public List<CYLReactionDetail> getTotalSynthesisList(List<CYLReactionDetail> list) throws IOException
    {

        Log.d("cyl","数据库为空，网络中加载全合成数据");

        for (char alphaB = 'a'; alphaB <= 'z'; alphaB++)
        {
            String path = "http://www.organic-chemistry.org/totalsynthesis/navi/"+alphaB+".shtm";
            String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(path));

            Document doc = Jsoup.parse(htmlStr);
            Elements links = doc.select("tr");
            for (int i = 0; i < links.size(); i++)
            {
                if (i == 0) continue;

                Element link = links.get(i);

                CYLReactionDetail detail = new CYLReactionDetail();

                /*获取相关内容的url*/
                String urlPath = link.child(0).getElementsByTag("a").attr("href").toString();

                urlPath = urlPath.replace("../","");

                if (!urlPath.contains("Highlights"))
                {
                    urlPath = "http://www.organic-chemistry.org/totalsynthesis/"+urlPath;
                }
                else
                {
                    urlPath = "http://www.organic-chemistry.org/"+urlPath;
                }

                detail.setUrlPath(urlPath);

                /*获得title*/
                 String title = link.child(0).getElementsByTag("a").text();
                detail.setName(title);
                /*获得year*/
                String year = link.child(1).getElementsByTag("td").text();
                detail.setYear(year);

                /*获得作者名*/
                String author = link.child(2).getElementsByTag("td").text();
                detail.setAuthor(author);

                CYLChemApplication.totalSynDao.insertNameReaction(detail);

                list.add(detail);
            }
        }

        Log.d("cyl","加载完毕");
        return list;
    }


    /*获得人名反应list*/
    public List<CYLReactionDetail> getNameReactionList(List<CYLReactionDetail> list) throws IOException
    {
        list = CYLChemApplication.nameReactionDao.getAllNameReaction();
        Log.d("cyl","网络中加载人名反应");

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(CYLUrlFactory.getUrlOfAllNameReactionList()));

        Document doc = Jsoup.parse(htmlStr);

        Elements links = doc.select("a[href]");

        for (Element link : links)
        {
            String name = link.attr("href");

            if (name.contains("shtm") && !name.contains("/"))
            {

                CYLReactionDetail nameReaction = new CYLReactionDetail();

                /*设置人名反应详情页的url*/
                nameReaction.setUrlPath("http://www.organic-chemistry.org/namedreactions/"+name);

                /*获得name字符串*/
                nameReaction.setName(link.getElementsByTag("a").text());

                //TODO
                /*设置图片*/
                nameReaction.setPicture(new byte[0]);
                /*设置desc*/
                nameReaction.setDesc("");

                list.add(nameReaction);

                /*当有图片数据和描述数据时才存入 数据库存入数据库*/
                if (nameReaction.getPicture().length != 0 && nameReaction.getDesc().length() != 0)
                {
                    CYLReactionDetail reaction = CYLChemApplication.nameReactionDao.getNameReaction("name=?",new String[]{nameReaction.getName()});

                    if (reaction == null)
                    {
                        /*新内容入数据库*/
                        Log.d("cyl","添加新内容入数据库 :" + nameReaction.getName());
                        CYLChemApplication.nameReactionDao.insertNameReaction(nameReaction);
                    }
                }

            }

        }

        Log.d("cyl","人名反应 ："+list.size()+"个");
        return list;
    }

    /*获得全合成反应详情字符串*/
    public List<String> getDetailContentForTotalSynthesis(Context context, String urlPath) throws IOException
    {
        Log.d("cyl","加载全合成反应@"+urlPath);
        /*获取图片路径的前缀*/
        String preUrl = urlPath.substring(0,(urlPath.lastIndexOf("/")+1));

        List<String> list = new ArrayList<>();

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(urlPath));

        Document doc = Jsoup.parse(htmlStr);
        Elements elements = doc.select("img[src],p");

        for (Element element : elements) {

            Log.i("cyl",element.toString());

            String img = element.attr("src");
            String p = element.getElementsByTag("p").text();

//
//            Log.w("cyl","image :"+img);
//            Log.w("cyl","p :" +p);

            if (img.length() != 0)
            {
                if (!img.contains("../") && !img.contains("Logo"))
                {
                    list.add(preUrl+img);
                }

            }
            if (p.length() != 0) list.add(Html.fromHtml(p).toString());
        }
        Log.d("cyl",list.toString());

        return list;
    }

    /*获得详情人名反应页面的内容*/
    public List<String> getDetailContentForNameReacton(Context context, String urlPath) throws IOException
    {
        Log.d("cyl","加载人名反应");
        List<String> list = new ArrayList<>();

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(urlPath));

        Document doc = Jsoup.parse(htmlStr);
        Elements elements = doc.select("img[src],p");

        for (Element element : elements) {

            Log.i("cyl",element.toString());

//            if (element.children().size() == 0)
//            {
                String img = element.attr("src");
                String p = element.getElementsByTag("p").text();


//                Log.w("cyl","image :"+img);
//                Log.w("cyl","p :" +p);

                if (img.length() != 0)
                {
                    if (!img.contains("../") && !img.contains("Logos"))
                    {
                            list.add("http://www.organic-chemistry.org/namedreactions/"+img);
                    }

                }
                if (p.length() != 0) list.add(Html.fromHtml(p).toString());
            }
//        }

        Log.d("cyl",list.toString());

        return list;
    }

    public String treatName(String name)
    {
        name = name.toLowerCase().replace("-", "");
        name = name.replace(" ", "_");
        name = name.replace("'", "");
        name = name.replace(",", "");
        name = name.replace("(", "_");
        name = name.replace(")", "_");
        return name;
    }

}
