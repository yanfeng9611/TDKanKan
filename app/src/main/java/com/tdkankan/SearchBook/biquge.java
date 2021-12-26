package com.tdkankan.SearchBook;


import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
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

/**
 * @author ZQZESS
 * @date 1/6/2021.
 * @file SearchBook
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class biquge {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            keyWords = URLEncoder.encode(keyWords, "gbk");
            alldoc = Jsoup.connect(searchURL + keyWords).
                    data("query", "Java").
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut)
                    .get();
            Elements elements = alldoc.getElementsByAttributeValue("class", "novelslistss").get(0).getElementsByTag("li");

            list = MultiThreadSpider.spiderBookInfo(elements, webURL, "biquge");

        }catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }

    public HashMap<String, String> spiderBookInfo(Element listItem, String webURL) {
        String bookName = "";    //书名
        String author = "";  //作者
        String bookLink = "";    //书链接
        String picName = ""; //封面名字
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
            Thread.sleep(new Random().nextInt(GlobalConfig.threadPoolRandomSleep));
            bookLink = listItem.getElementsByTag("a").get(0).attr("href");  //获取书籍link
            bookName = listItem.getElementsByTag("a").get(0).text().trim();  //获取书名
            author = listItem.getElementsByTag("span").get(3).text().trim();  //获取作者名
            newChapter = listItem.getElementsByTag("span").get(2).text().trim();
            newChapterLink = listItem.getElementsByTag("a").get(0).attr("href");

            Document doc = Jsoup.connect(bookLink).
                    data("query", "Java").
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut)
                    .get();
            status = doc.getElementsByTag("meta").get(20).attr("content");
            category = doc.getElementsByTag("meta").get(15).attr("content");
            picLink = doc.getElementById("fmimg").getElementsByTag("img").attr("src");
            bookIntroduction = doc.getElementById("intro").text().trim();
//                    lastTime = doc.getElementById("info").getElementsByTag("p").get(2).text().trim();
//                    int chapterNum = 0; //总章节
        } catch (Exception e) {
//            e.printStackTrace();
        }
//        // 缓存图片
//        final String finalPicLink = picLink;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                BookInfoCache.loadImage(finalPicLink); //采用Haspmap缓存图片至内存
//            }
//        }).start();

        // 缓存图书
        BookInfo bookInfo = new BookInfo(bookName, author, bookLink,
                picLink, bookIntroduction,
                lastTime, newChapter, newChapterLink,
                chapterNum, status, category, linkFrom);
        GlobalConfig.bookmap.put(bookLink, bookInfo);

        book.put("bookName", bookName);
        book.put("author", author);
        book.put("bookLink", bookLink);
        book.put("picLink", picLink);
        book.put("bookIntroduction", bookIntroduction);
        book.put("status", status);
        book.put("category", category);
        book.put("newChapter", newChapter);
        book.put("newChapterLink", newChapterLink);
        book.put("linkFrom", webURL);
        return book;
    }

}