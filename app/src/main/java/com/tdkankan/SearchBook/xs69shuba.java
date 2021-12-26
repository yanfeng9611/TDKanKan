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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class xs69shuba {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            keyWords = URLEncoder.encode(keyWords, "gbk");
            String submit = "%CB%D1%CB%F7";
            String url = "?searchkey=" + keyWords + "&submit=" + submit;
            alldoc = Jsoup.connect(searchURL + url).
                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            Elements elements = alldoc.getElementsByTag("tr");

            list = MultiThreadSpider.spiderBookInfo(elements, webURL, "xs69shuba");

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
        HashMap<String,String> book = new HashMap<String,String>();
        try {
            Thread.sleep(new Random().nextInt(GlobalConfig.threadPoolRandomSleep));
            bookLink = listItem.getElementsByAttributeValue("class", "odd").get(0).getElementsByTag("a").get(0).attr("href").trim();
            bookName = listItem.getElementsByAttributeValue("class", "odd").get(0).getElementsByTag("a").get(0).text().trim();
            author = listItem.getElementsByAttributeValue("class", "odd").get(1).text().trim();
            status = listItem.getElementsByAttributeValue("class", "even").get(2).text().trim();
            newChapter = listItem.getElementsByAttributeValue("class", "even").get(0).getElementsByTag("a").text().trim();
            newChapterLink = listItem.getElementsByAttributeValue("class", "even").get(0).getElementsByTag("a").attr("href");
            Document doc = Jsoup.connect(bookLink).
                    data("query", "Java").
                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            picLink = doc.getElementsByAttributeValue("class", "pic").get(0).getElementsByTag("img").attr("src");
            bookIntroduction = doc.getElementsByAttributeValue("class", "bookinfo_intro").get(0).text().trim();

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
        book.put("newChapter", newChapter);
        book.put("newChapterLink", newChapterLink);
        book.put("linkFrom", webURL);
        return book;
    }
}
