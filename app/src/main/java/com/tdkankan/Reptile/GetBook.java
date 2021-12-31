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
//                    book = biquge.spiderBookInfo(listItem, url.biqugeURL);
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
//                    book = biquge.spiderBookInfo(listItem, url.biqugeURL);
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
//                    book = biquge.spiderBookInfo(listItem, url.biqugeURL);
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

}
