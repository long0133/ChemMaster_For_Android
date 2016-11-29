package com.gary.chemmaster.util;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

import com.gary.chemmaster.CYLEnum.MouleFlag;
import com.gary.chemmaster.Dao.CYLNameReactionDao;
import com.gary.chemmaster.Dao.CYLTotalSynDao;
import com.gary.chemmaster.activity.ShowPicListActivity;
import com.gary.chemmaster.app.CYLChemApplication;
import com.gary.chemmaster.entity.CYLChemTool;
import com.gary.chemmaster.entity.CYLReactionDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

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
            /*人名反应*/
            list = getNameReactionList(list);
        }
        else if (flag.equals(MouleFlag.moduleTotalSynthesis))
        {
            /*全合成*/
            Log.d("cyl","数据库中加载数据");

            list = CYLChemApplication.totalSynDao.getAllTotalSynReaction();

            if (list.isEmpty())
            {
                list = getTotalSynthesisList(list);
            }
        }
        else if (flag.equals(MouleFlag.moduleHightLight))
        {
            /*高亮文章的year*/
            list = getHighLightYearUrlList();

        }
        else if (flag.equals(MouleFlag.moduleHighLightOfYear))
        {
            list = getHighLighOfYear(list);
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
                detail.setTypeNum(CYLReactionDetail.IS_FOR_TOTAL_SYN);

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
                nameReaction.setTypeNum(CYLReactionDetail.IS_FOR_NAME_REACTION);

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

    /*获得高亮文章的year表*/
    public List<CYLReactionDetail> getHighLightYearUrlList() throws IOException
    {
        List<CYLReactionDetail> years = new ArrayList<>();

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(CYLUrlFactory.getUrlOfHighLightYearList()));

        Document doc = Jsoup.parse(htmlStr);

        Elements links =  doc.select("a[href]");

        for(Element link : links)
        {
            String href = link.attr("href").toString();

            if (href.contains("shtm") && href.substring(0,4).matches("[0-9]+"))
            {
                CYLReactionDetail detail = new CYLReactionDetail();
                detail.setTypeNum(CYLReactionDetail.IS_FOR_HIGH_LIGHT);
                String year = href.split("/")[0];
                detail.setHighLightYearUrl("http://www.organic-chemistry.org/Highlights/"+year+"/index.shtm");
                years.add(detail);
            }
        }

        return years;
    }

    /*获得高亮反应所选年的所有内容list*/
    public List<CYLReactionDetail> getHighLighOfYear(List<CYLReactionDetail> list) throws IOException
    {
        /*获得所选一年的所有高亮文章*/
        String p = ShowPicListActivity.selecedYearUrl;
        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(p));

        Document doc = Jsoup.parse(htmlStr);
        Elements links = doc.select("a[href$=shtm],title");

        for(Element link:links) {

            CYLReactionDetail detail = new CYLReactionDetail();
            if (!link.attr("href").toString().contains("index"))
            {
                detail.setTypeNum(CYLReactionDetail.IS_FOR_HIGH_LIGHT);
                detail.setHighLightYearUrl(p);
                detail.setUrlPath(p.substring(0,(p.lastIndexOf("/")+1)) + link.attr("href").toString());
                detail.setName(link.getElementsByTag("a").text());

                if (detail.getUrlPath().contains("shtm")) list.add(detail);
            }

        }

        return list;
    }


    public List<CYLReactionDetail> getHighLighOfYear(List<CYLReactionDetail> list, String selectedYearUrl) throws IOException
    {
        /*获得所选一年的所有高亮文章*/
        String p = selectedYearUrl;
        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(p));

        Document doc = Jsoup.parse(htmlStr);
        Elements links = doc.select("a[href$=shtm],title");

        for(Element link:links) {

            CYLReactionDetail detail = new CYLReactionDetail();
            if (!link.attr("href").toString().contains("index"))
            {
                detail.setTypeNum(CYLReactionDetail.IS_FOR_HIGH_LIGHT);
                detail.setHighLightYearUrl(p);
                detail.setUrlPath(p.substring(0,(p.lastIndexOf("/")+1)) + link.attr("href").toString());
                detail.setName(link.getElementsByTag("a").text());

                if (detail.getUrlPath().contains("shtm")) list.add(detail);
            }

        }

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


    /*获得某一高亮文章的详情内容*/
    public List<String> getDetailContentForHighLight(Context context, String urlPath) throws IOException
    {
        List<String> list = new ArrayList<>();

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(urlPath));

        Document doc = Jsoup.parse(htmlStr);
        Elements elements = doc.select("img[src$=GIF],p");

        for (Element element : elements) {
            /*解析出显示内容*/
            String p = element.getElementsByTag("p").text();
            if (p.length() != 0) list.add(p);

            String url = urlPath.substring(0,(urlPath.lastIndexOf("/")+1)) + element.attr("src");
            if (url.length() != 0 && !url.contains("Logos") && url.contains("GIF")) list.add(url);
        }

        return list;
    }


    /*获得化学工具网页的大类列表*/
    public List<CYLChemTool> getChemToolList(Context context) throws IOException
    {
        List<CYLChemTool> list = new ArrayList<>();

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get("http://www.organic-chemistry.org/info/chemistry/topics.shtm"));

        Document doc = Jsoup.parse(htmlStr);

        Elements links = doc.select("a[href]");

        for (Element link : links)
        {
            CYLChemTool chemTool = new CYLChemTool();

            if (link.attr("href").contains("shtm") && !link.getElementsByTag("a").text().contains("Links"))
            {
                chemTool.setTitle(link.getElementsByTag("a").text());
                chemTool.setUrlPath("http://www.organic-chemistry.org/info/chemistry/"+link.attr("href"));
                list.add(chemTool);
            }

        }

        return list;
    }

    /*获得指定的化学大类的小类列表*/
    public List<CYLChemTool> getSpecifiedChemToolList(Context context,String urlPath) throws  IOException
    {
        List<CYLChemTool> tools = new ArrayList<>();

        String htmlStr = CYLHttpUtils.getString(CYLHttpUtils.get(urlPath));

        Document doc = Jsoup.parse(htmlStr);

        Elements links =  doc.select("a[target]");

        for (Element link : links)
        {
            CYLChemTool tool = new CYLChemTool();
            tool.setTitle(link.getElementsByTag("a").text());
            tool.setUrlPath(link.attr("href"));
            tool.setBelongTo(urlPath.substring((urlPath.lastIndexOf("/") + 1),(urlPath.length() - 5)).toUpperCase());
            tools.add(tool);
        }
        return tools;
    }
}
