package com.tdkankan.Reptile;

import android.content.Context;
import android.util.Log;

import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;

import com.tdkankan.Data.UrlConfig;
import com.tdkankan.SearchBook.Binifu;
import com.tdkankan.SearchBook.Biquge;
import com.tdkankan.SearchBook.Xixi;
import com.tdkankan.SearchBook.Xs5200;
import com.tdkankan.SearchBook.Xs59;
import com.tdkankan.SearchBook.Xs69shuba;
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
    static String bookName = "";    //书名
    static String author = "";  //作者
    static String bookLink = "";    //书链接
    static String picLink = "";    //书链接
    static String newChapter = "";  //最新章节
    static String newChapterLink = "";  //最新章节链接
    static String bookIntroduction = "";    //简介
    static String category = "";


    public static ArrayList<HashMap<String, String>> fengtui() {
        Document alldoc;
        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();
        HashMap<String,String> book = null;
        try{
            alldoc = Jsoup.connect(UrlConfig.biqugeURL).data("query", "Java").userAgent(ProxyHost.getProxyAgent()).get();
            Elements elements = alldoc.getElementsByAttributeValue("class", "item");
            for(Element item : elements) {
                book = new HashMap<String,String>();
                bookLink = item.getElementsByTag("a").attr("href");//获取书籍link
                if (GlobalConfig.bookmap.containsKey(bookLink)) {
                    book = getBookFromCache(bookLink);
                } else {
                    bookName = item.getElementsByTag("a").get(1).text();
                    bookLink = item.getElementsByTag("a").get(1).attr("href");
                    picLink = item.getElementsByTag("img").get(0).attr("src");
                    author = item.getElementsByTag("span").get(0).text().replace("作者：", "");
                    bookIntroduction = item.getElementsByTag("dd").get(0).text();
                    newChapter = item.getElementsByTag("a").get(2).text();
                    newChapterLink = item.getElementsByTag("a").get(2).attr("href");

                    book.put("bookLink", bookLink);
                    book.put("bookName", bookName);
                    book.put("author", author);
                    book.put("newChapter", newChapter);
                    book.put("newChapterLink", newChapterLink);
                    book.put("picLink", picLink);
                    book.put("bookIntroduction", bookIntroduction);
                    book.put("linkFrom", UrlConfig.biqugeURL);
                }
                list.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<HashMap<String, String>> qiangtui() {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> book = null;
        try {
            alldoc= Jsoup.connect(UrlConfig.biqugeURL).userAgent(ProxyHost.getProxyAgent()).get();
            Elements elements = alldoc.select("div.r > ul").get(0).getElementsByTag("li");
            for (Element item : elements) {
                bookLink = item.getElementsByTag("a").attr("href");//获取书籍link
                book = new HashMap<String, String>();

                if (GlobalConfig.bookmap.containsKey(bookLink)) {
                    book = getBookFromCache(bookLink);
                } else {
                    bookName = item.getElementsByTag("a").get(0).text();
                    bookLink = item.getElementsByTag("a").get(0).attr("href");
                    author = item.getElementsByTag("span").get(2).text();
                    category = item.getElementsByTag("span").get(0).text().replace("[", "").replace("]", "");
                    book.put("bookName", bookName);
                    book.put("bookLink", bookLink);
                    book.put("author", author);
                    book.put("category", category);
                    book.put("picLink", UrlConfig.commonPicLink);
                }
                list.add(book);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<HashMap<String,String>> ruku() {
        Document alldoc;
        HashMap<String,String> book = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try{
            alldoc = Jsoup.connect(UrlConfig.biqugeURL).data("query", "Java").userAgent(ProxyHost.getProxyAgent()).get();
            Elements elements = alldoc.select("div.r > ul").get(1).getElementsByTag("li");
            for(Element item : elements) {
                book = new HashMap<String,String>();
                bookLink = item.getElementsByTag("a").attr("href");//获取书籍link
                if (GlobalConfig.bookmap.containsKey(bookLink)) {
                    book = getBookFromCache(bookLink);
                } else {
                    bookName = item.getElementsByTag("a").get(0).text();
                    bookLink = item.getElementsByTag("a").get(0).attr("href");
                    author = item.getElementsByTag("span").get(2).text();
                    category = item.getElementsByTag("span").get(0).text().replace("[", "").replace("]", "");
                    book.put("bookName", bookName);
                    book.put("bookLink", bookLink);
                    book.put("author", author);
                    book.put("category", category);
                    book.put("picLink", UrlConfig.commonPicLink);
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

    public static BookInfo getBookInfo(String bookURL) {
        BookInfo book = null;
        if (bookURL.contains(UrlConfig.binifuURL)) {
            Binifu binifu = new Binifu();
            book = binifu.spiderBookInfo(bookURL);
        } else if (bookURL.contains(UrlConfig.biqugeURL)) {
            Biquge biquge = new Biquge();
            book = biquge.spiderBookInfo(bookURL);
        } else if (bookURL.contains(UrlConfig.xixiURL)) {
            Xixi xixi = new Xixi();
            book = xixi.spiderBookInfo(bookURL);
        } else if (bookURL.contains(UrlConfig.xs59URL)) {
            Xs59 xs59 = new Xs59();
            book = xs59.spiderBookInfo(bookURL);
        } else if (bookURL.contains(UrlConfig.xs69shubaURL)) {
            Xs69shuba xs69shuba = new Xs69shuba();
            book = xs69shuba.spiderBookInfo(bookURL);
        } else if (bookURL.contains(UrlConfig.xs5200URL)) {
            Xs5200 xs5200 = new Xs5200();
            book = xs5200.spiderBookInfo(bookURL);
        }
        return book;
    }
}
