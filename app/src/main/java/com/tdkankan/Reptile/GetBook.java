package com.tdkankan.Reptile;

import android.content.Context;
import android.util.Log;

import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.SearchBook.biquge;
import com.tdkankan.contains.url;
import com.tdkankan.proxy.ProxyHost;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ZQZESS
 * @date 12/9/2020.
 * @file GetBook
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class GetBook {

    public static ArrayList<HashMap<String, String>> fengtui() {
        Document alldoc;
        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();
        try{
            alldoc = Jsoup.connect(url.biqugeURL).data("query", "Java").userAgent(ProxyHost.getProxyAgent()).get();
            Elements elements = alldoc.getElementsByAttributeValue("class", "item");
            for(Element listItem : elements) {
                HashMap<String,String> book = new HashMap<String,String>();
                String bookLink = listItem.getElementsByTag("a").attr("href");//获取书籍link
                if (GlobalConfig.bookmap.containsKey(bookLink)) {
                    book = getBookFromCache(bookLink);
                } else {
                    biquge biquge = new biquge();
                    book = biquge.spiderBookInfo(listItem, url.biqugeURL);
                }

                list.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList qiangtui()
    {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try{
            alldoc= Jsoup.connect(url.biqugeURL).data("query", "Java").userAgent(ProxyHost.getProxyAgent()).get();
            Elements elements = alldoc.select("div.r > ul").get(0).getElementsByTag("li");
            for(Element listItem : elements) {
                String bookLink = listItem.getElementsByTag("a").attr("href");//获取书籍link
                HashMap<String, String> book = new HashMap<String, String>();

                if (GlobalConfig.bookmap.containsKey(bookLink)) {
                    book = getBookFromCache(bookLink);
                } else {
                    biquge biquge = new biquge();
                    book = biquge.spiderBookInfo(listItem, url.biqugeURL);
                }

                list.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList ruku() {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try{
            alldoc = Jsoup.connect(url.biqugeURL).data("query", "Java").userAgent(ProxyHost.getProxyAgent()).get();
            Elements elements = alldoc.select("div.r > ul").get(1).getElementsByTag("li");
            for(Element listItem : elements) {
                HashMap<String,String> book = new HashMap<String,String>();
                String bookLink = listItem.getElementsByTag("a").attr("href");//获取书籍link
                if (GlobalConfig.bookmap.containsKey(bookLink)) {
                    book = getBookFromCache(bookLink);
                } else {
                    biquge biquge = new biquge();
                    book = biquge.spiderBookInfo(listItem, url.biqugeURL);
                }
                list.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static HashMap<String, String> getBookFromCache(String bookLink) {
        HashMap<String, String> book = new HashMap<String, String>();
        BookInfo bookInfo = GlobalConfig.bookmap.get(bookLink);
        book.put("bookName",bookInfo.getBookName());
        book.put("author",bookInfo.getAuthor());
        book.put("bookLink",bookLink);
        book.put("picLink",bookInfo.getPicLink());
        book.put("bookIntroduction",bookInfo.getBookIntroduction());
        book.put("lastTime",bookInfo.getLastTime());
        book.put("newChapter",bookInfo.getNewChapter());
        book.put("newChapterLink",bookInfo.getNewChapterLink());
        book.put("status",bookInfo.getStatus());
        book.put("category",bookInfo.getCategory());
        book.put("chapterNum","" + bookInfo.getChapterNum());
        book.put("linkFrom",bookInfo.getLinkFrom());
        return book;
    }

//    public static BookInfo GetBookInfo(String url) {
//        Document alldoc;
//        String bookName = "";    //书名
//        String author = "";  //作者
//        String bookLink = "";    //书链接
//        String picName = ""; //封面名字
//        String picLink = ""; //封面链接
//        String bookIntroduction = "";    //简介
//        String lastTime = "";    //最后更新时间
//        String newChapter = "";  //最新章节
//        String newChapterLink = "";  //最新章节链接
//        int chapterNum = 0; //总章节
//        String linkFrom = "";    //书源
//        String status = "";
//        String category = "";
//        try{
//            alldoc = Jsoup.connect("https://www.biqugeu.net/"+url+"/").data("query", "Java").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36").get();
//            bookName = alldoc.select("#info > h1").text().trim();
//            author = alldoc.select("#info > p:nth-child(2)").text().trim().replace("作 者：","");
//            lastTime = alldoc.select("#info > p:nth-child(4)").text().trim().replace("最后更新：","");
//            newChapter = alldoc.select("#info > p:nth-child(5)").text().trim().replace("最新章节：","");
//            newChapterLink=alldoc.select("#info > p:nth-child(5) > a").attr("href");
//            bookIntroduction = alldoc.select("#intro").text().trim();
//            picLink = alldoc.getElementsByTag("img").attr("src");
//
//            Elements listClass = alldoc.select("#list > dl > dd");
//            int i=0;
//            for(Element listitem:listClass) {
//                listitem.getElementsByTag("a").attr("href");//获取书籍link
//                i++;
//            }
//            if(i > 24) {
//                chapterNum = i - 12;
//            }else if(i<=24&&i>0) {
//                chapterNum = i / 2;
//            }else {
//                chapterNum = 0;
//            }
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        BookInfo bookInfo = new BookInfo(bookName, author, bookLink,
//                picLink, bookIntroduction,
//                lastTime, newChapter, newChapterLink,
//                chapterNum, status, category, linkFrom);
//        return bookInfo;
//    }
}
