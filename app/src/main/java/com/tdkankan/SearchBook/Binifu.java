package com.tdkankan.SearchBook;


import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.Data.UrlConfig;
import com.tdkankan.proxy.ProxyHost;
import com.tdkankan.utils.MultiThreadSpider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Binifu {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        String bookName = "";    //书名
        String bookLink = "";    //书链接
        String picLink = ""; //封面链接
        String bookIntroduction = "";    //简介
        String lastTime = "";    //最后更新时间
        String newChapter = "";  //最新章节
        String newChapterLink = "";  //最新章节链接
        String status = "";
        String category = "";
        HashMap<String,String> book;
        try {

            keyWords = URLEncoder.encode(keyWords, "UTF-8");
            String url = webURL + "/e/search/?keyboard=" + keyWords + "&classid=0&searchget=1&show=title,author";
            alldoc = Jsoup.connect(url).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    post();
            Elements elements = alldoc.getElementsByTag("dl");
            for (Element item : elements) {
                book = new HashMap<String,String>();
                picLink = item.getElementsByTag("img").attr("src");
                bookLink = webURL + item.getElementsByTag("a").get(1).attr("href");
                bookName = item.getElementsByTag("a").get(1).text().trim();
                category = item.getElementsByTag("a").get(2).text().trim();
                status = item.getElementsByTag("span").get(1).text().trim();
                bookIntroduction = item.getElementsByAttributeValue("class", "book_des").text().trim();
                newChapter = item.getElementsByTag("a").get(3).text().trim();
                newChapterLink = webURL + item.getElementsByTag("a").get(3).attr("href");
                lastTime = item.getElementsByTag("span").get(3).text().trim();

                if (picLink.contains("nocover") || picLink.length() > UrlConfig.commonPicLink.length()) {
                    picLink = UrlConfig.commonPicLink;
                }

                book.put("bookLink", bookLink);
                book.put("bookName", bookName);
                book.put("status", status);
                book.put("category", category);
                book.put("picLink", picLink);
                book.put("bookIntroduction", bookIntroduction);
                book.put("lastTime", lastTime);
                book.put("newChapter", newChapter);
                book.put("newChapterLink", newChapterLink);
                book.put("linkFrom", UrlConfig.binifu);
                list.add(book);

                // 缓存图片
                final String finalPicLink = picLink;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BookInfoCache.loadImage(finalPicLink); //采用Haspmap缓存图片至内存
                    }
                }).start();
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookInfo spiderBookInfo(String bookURL) {
        String bookName = "";    //书名
        String author = "";  //作者
        String bookLink = "";    //书链接
        String picLink = ""; //封面链接
        String bookIntroduction = "";    //简介
        String lastTime = "";    //最后更新时间
        String newChapter = "";  //最新章节
        String newChapterLink = "";  //最新章节链接
        int chapterNum = 0; //总章节
        String linkFrom = "";    //书源
        String status = "";
        String category = "";
        HashMap<String,String> book = new HashMap<String,String>();
        try {
            Document doc = Jsoup.connect(bookURL).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            bookIntroduction = doc.getElementsByTag("meta").get(11).attr("content");
            picLink = doc.getElementsByTag("meta").get(12).attr("content");
            String link = "https://img.imiaobige.com/46008/183143.jpg";
            if (picLink.length() < link.length() - 3 || picLink.length() > link.length() + 3) {
                picLink = UrlConfig.binifuURL + "/images/nocover.jpg";
            }
            category = doc.getElementsByTag("meta").get(13).attr("content");
            author = doc.getElementsByTag("meta").get(14).attr("content");
            bookName = doc.getElementsByTag("meta").get(15).attr("content");
            bookLink = doc.getElementsByTag("meta").get(16).attr("content");
            status = doc.getElementsByTag("meta").get(18).attr("content");
            lastTime = doc.getElementsByTag("meta").get(20).attr("content");
            newChapter = doc.getElementsByTag("meta").get(21).attr("content");
            newChapterLink = doc.getElementsByTag("meta").get(22).attr("content");
            linkFrom = UrlConfig.binifu;
            doc = Jsoup.connect(bookLink).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            String docStr = doc.toString();
            String str = docStr.substring(docStr.indexOf("<div class=\"border-line\"></div>"));
            int offset = "<div class=\"border-line\"></div>".length();
            docStr = str.substring(offset);
            String subStr = docStr.substring(docStr.indexOf("<div class=\"border-line\"></div>"), docStr.indexOf("<div class=\"pages\"></div>"));
            doc = Jsoup.parse(subStr);
            chapterNum = doc.getElementsByTag("a").size();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 缓存图片
        final String finalPicLink = picLink;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BookInfoCache.loadImage(finalPicLink); //采用Haspmap缓存图片至内存
            }
        }).start();

        // 缓存图书
        BookInfo bookInfo = new BookInfo(bookName, author, bookLink,
                picLink, bookIntroduction, lastTime,
                newChapter, newChapterLink, chapterNum,
                linkFrom, status, category) ;
        GlobalConfig.bookmap.put(bookURL, bookInfo);

        return bookInfo;
    }
}
