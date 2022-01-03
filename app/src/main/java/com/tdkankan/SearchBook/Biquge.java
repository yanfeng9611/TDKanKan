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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ZQZESS
 * @date 1/6/2021.
 * @file SearchBook
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class Biquge {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        String bookName = "";    //书名
        String author = "";  //作者
        String bookLink = "";    //书链接
        String lastTime = "";    //最后更新时间
        String newChapter = "";  //最新章节
        String newChapterLink = "";  //最新章节链接
        String category = "";
        HashMap<String, String > book = null;
        try {
            keyWords = URLEncoder.encode(keyWords, "gbk");
            alldoc = Jsoup.connect(searchURL + keyWords).
                    data("query", "Java").
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut)
                    get();
            Elements elements = alldoc.getElementsByAttributeValue("class", "novelslistss").get(0).getElementsByTag("li");
            for (Element item : elements) {
                book = new HashMap<>();
                category = item.getElementsByTag("span").get(0).text().trim();
                bookName = item.getElementsByTag("a").get(0).text().trim();
                bookLink = item.getElementsByTag("a").get(0).attr("href");
                newChapter = item.getElementsByTag("a").get(1).text().trim();
                newChapterLink = item.getElementsByTag("a").get(1).attr("href");
                author = item.getElementsByTag("span").get(3).text().trim();
                lastTime = item.getElementsByTag("span").get(4).text();

                book.put("bookLink", bookLink);
                book.put("bookName", bookName);
                book.put("author", author);
                book.put("newChapter", newChapter);
                book.put("newChapterLink", newChapterLink);
                book.put("category", category);
                book.put("lastTime", lastTime);
                book.put("picLink", UrlConfig.commonPicLink);
                book.put("linkFrom", UrlConfig.biquge);
                list.add(book);
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
        Document doc = null;
        try {
             doc = Jsoup.connect(bookURL).
                    data("query", "Java").
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut)
                    .get();
            bookName = doc.getElementsByTag("meta").get(12).attr("content");
            bookIntroduction = doc.getElementsByTag("meta").get(13).attr("content");
            picLink = doc.getElementsByTag("meta").get(14).attr("content");
            category = doc.getElementsByTag("meta").get(15).attr("content");
            author = doc.getElementsByTag("meta").get(16).attr("content");
            bookLink = doc.getElementsByTag("meta").get(18).attr("content");
            status = doc.getElementsByTag("meta").get(20).attr("content");
            lastTime = doc.getElementsByTag("meta").get(22).attr("content");
            newChapter = doc.getElementsByTag("meta").get(23).attr("content");
            newChapterLink = doc.getElementsByTag("meta").get(24).attr("content");
            linkFrom = UrlConfig.biquge;
            String docStr = doc.toString();
            String subStr = docStr.substring(docStr.indexOf("<center class=\"clear\">"), docStr.indexOf("<div class=\"footer\" id=\"footer\">"));
            doc = Jsoup.parse(subStr);
            chapterNum = doc.getElementsByTag("a").size();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        // 缓存图片
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
                linkFrom, status, category);
        GlobalConfig.bookmap.put(bookLink, bookInfo);

        return bookInfo;
    }

    public void getChapterList(String bookLink) {
        Document doc;
        ConcurrentHashMap<String,String > chapterMap = null;
        try {
            doc = Jsoup.connect(bookLink).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(5000).
                    get();
            String docStr = doc.toString();
            String subStr = docStr.substring(docStr.indexOf("<center class=\"clear\">"), docStr.indexOf("<div class=\"footer\" id=\"footer\">"));
            doc = Jsoup.parse(subStr);
            Elements elements = doc.getElementsByTag("a");
//
            for (Element item : elements) {
                chapterMap = new ConcurrentHashMap<>();

                String chapterLink = bookLink + item.getElementsByTag("a").attr("href");//获取章节link
                String chapterTitle = item.getElementsByTag("a").text();//获取章节名
                chapterMap.put("chapterLink", chapterLink);
                chapterMap.put("chapterTitle", chapterTitle);
                GlobalConfig.list.add(chapterMap);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getBookContent(String chapterLink) {
        Document doc;
        String content="";
        try {
            doc = Jsoup.connect(chapterLink).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(5000).
                    get();
//            System.out.println(doc);
            Elements elements = doc.getElementsByAttributeValue("name", "content");
            content = elements.toString();
            content = content.substring(content.indexOf("<br>"), content.indexOf("</div>"));
            content = content.replace("<br>", "").replace("\n", "");
            content = content.substring(" &nbsp;&nbsp;&nbsp;&nbsp;".length());
            content = content.replace(" &nbsp;&nbsp;&nbsp;&nbsp;", "\n");
            System.out.println(content);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}