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


public class binifu {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {

            keyWords = URLEncoder.encode(keyWords, "UTF-8");
            String url = webURL + "/e/search/?keyboard=" + keyWords + "&classid=0&searchget=1&show=title,author";
            alldoc = Jsoup.connect(url).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            Elements elements = alldoc.getElementsByTag("dl");

            list = MultiThreadSpider.spiderBookInfo(elements, webURL, "binifu");

        }catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }

    public HashMap spiderBookInfo(Element listItem, String webURL) {
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
            Thread.sleep(new Random().nextInt(GlobalConfig.threadPoolRandomSleep));
            bookLink = webURL + listItem.getElementsByTag("dt").get(0).getElementsByTag("a").attr("href");
            bookName = listItem.getElementsByTag("dd").get(0).getElementsByTag("a").text().trim();
            status = listItem.getElementsByAttributeValue("class", "book_other").get(0).getElementsByTag("span").get(0).text().trim();
            category = listItem.getElementsByAttributeValue("class", "book_other").get(0).getElementsByTag("a").text().trim();
            newChapter = listItem.getElementsByAttributeValue("class", "book_other").get(1).getElementsByTag("a").text();
            newChapterLink = webURL + listItem.getElementsByAttributeValue("class", "book_other").get(1).getElementsByTag("a").attr("href");
            bookIntroduction = listItem.getElementsByAttributeValue("class", "book_des").text().trim();
            picLink = webURL + listItem.getElementsByTag("dt").get(0).getElementsByTag("img").attr("src");

            Document doc = Jsoup.connect(bookLink).
                    data("query", "Java").
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            bookLink = doc.getElementsByAttributeValue("class", "motion").get(0).getElementsByTag("a").get(0).attr("href");
            author = doc.getElementsByAttributeValue("class", "booktitle").get(0).getElementsByTag("a").text().trim();

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
        book.put("newChapter", newChapter);
        book.put("newChapterLink", newChapterLink);
        book.put("linkFrom", webURL);
        book.put("category", category);
        return book;
    }

}
