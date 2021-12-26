package com.tdkankan.SearchBook;

import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.proxy.ProxyHost;
import com.tdkankan.utils.MultiThreadSpider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class xs59 {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("q", keyWords);
            alldoc = Jsoup.connect(searchURL).
                    data(postParam).
                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    post();
            Elements elements = alldoc.getElementsByTag("dl");

            list = MultiThreadSpider.spiderBookInfo(elements, webURL, "xs59");

        }catch (Exception e) {
//            e.printStackTrace();
        }
        return list;
    }

    public HashMap spiderBookInfo(Element listItem, String webURL) {
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
        HashMap<String, String> book = new HashMap<String, String>();
        try {
            Thread.sleep(new Random().nextInt(GlobalConfig.threadPoolRandomSleep));

            bookLink = listItem.getElementsByTag("dt").get(0).getElementsByTag("a").attr("href");
            bookLink = webURL + bookLink;
            bookName = listItem.getElementsByTag("dt").get(0).getElementsByTag("img").attr("alt");
            author = listItem.getElementsByAttributeValue("class", "book_other").get(0).getElementsByTag("span").get(0).text().trim();
            status = listItem.getElementsByAttributeValue("class", "book_other").get(0).getElementsByTag("span").get(1).text().trim();
            newChapter = listItem.getElementsByAttributeValue("class", "book_other").get(1).getElementsByTag("a").get(0).text().trim();
            newChapterLink = webURL + listItem.getElementsByAttributeValue("class", "book_other").get(1).getElementsByTag("a").attr("href");
            picLink = listItem.getElementsByTag("dt").get(0).getElementsByTag("img").attr("src");
            picLink = webURL + picLink;
            category = listItem.getElementsByAttributeValue("class", "book_other").get(0).getElementsByTag("a").text().trim();
            String infoLink = listItem.getElementsByAttributeValue("class", "book_des").get(0).getElementsByTag("a").attr("href");
            Document doc = Jsoup.connect(webURL + infoLink).
                    data("query", "Java").proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut)
                    .get();
            bookIntroduction = doc.getElementsByAttributeValue("class", "desc desc-short").get(0).text().trim();
        } catch (Exception e) {
//            e.printStackTrace();
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