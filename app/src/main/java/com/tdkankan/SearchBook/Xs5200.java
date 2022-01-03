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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Xs5200 {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        String bookName = "";
        String author = "";  //作者
        String bookLink = "";    //书链接
        String picLink = ""; //封面链接
        String lastTime = "";    //最后更新时间
        String newChapter = "";  //最新章节
        String newChapterLink = "";  //最新章节链接
        String status = "";
        HashMap<String, String> book = null;
        try {
            alldoc = Jsoup.connect(searchURL+keyWords).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    post();
            Elements elements = alldoc.getElementsByAttributeValue("id", "nr");
            for (Element item : elements) {
                book = new HashMap<>();
                bookName = item.getElementsByTag("td").get(0).text();
                bookLink = item.getElementsByTag("td").get(0).getElementsByTag("a").attr("href");
                newChapter = item.getElementsByTag("td").get(1).text();
                newChapterLink = item.getElementsByTag("td").get(1).getElementsByTag("a").attr("href");
                author = item.getElementsByTag("td").get(2).text();
                lastTime = item.getElementsByTag("td").get(4).text();
                status = item.getElementsByTag("td").get(5).text();

                book.put("bookLink", bookLink);
                book.put("bookName", bookName);
                book.put("picLink", UrlConfig.commonPicLink);
                book.put("newChapter", newChapter);
                book.put("newChapterLink", newChapterLink);
                book.put("author", author);
                book.put("lastTime", lastTime);
                book.put("status", status);
                book.put("linkFrom", UrlConfig.xs5200);
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
        } catch (Exception e) {
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
                    post();

            bookName = doc.getElementsByTag("meta").get(8).attr("content");
            bookIntroduction = doc.getElementsByTag("meta").get(9).attr("content");
            picLink = doc.getElementsByTag("meta").get(10).attr("content");
            category = doc.getElementsByTag("meta").get(11).attr("content");
            author = doc.getElementsByTag("meta").get(12).attr("content");
            bookLink = doc.getElementsByTag("meta").get(14).attr("content");

            status = doc.getElementById("info").getElementsByTag("p").get(1).text().replace("态：", "").replace("状", "").replace(" ", "");
            lastTime = doc.getElementById("info").getElementsByTag("p").get(2).text().replace("最后更新：", "");
            newChapter = doc.getElementById("info").getElementsByTag("p").get(3).text().replace("最新章节：", "");
            newChapterLink = doc.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").attr("href");
            linkFrom = UrlConfig.xs5200;
            chapterNum = doc.getElementById("list").getElementsByTag("a").size();

        } catch (Exception e) {
            e.printStackTrace();
        }

//         缓存图片
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

    public void getChapterList(String bookLink) {
        Document doc;
        ConcurrentHashMap<String,String > chapterMap = null;
        try {
            doc = Jsoup.connect(bookLink).
                    userAgent(ProxyHost.getProxyAgent()).
                    get();
            Elements elements = doc.getElementById("list").getElementsByTag("a");

            for (Element item : elements) {
                chapterMap = new ConcurrentHashMap<>();

                String chapterLink = UrlConfig.xs5200URL + item.getElementsByTag("a").attr("href");//获取章节link
                String chapterTitle = item.getElementsByTag("a").text();//获取章节名
                chapterMap.put("chapterLink", chapterLink);
                chapterMap.put("chapterTitle", chapterTitle);
                GlobalConfig.list.add(chapterMap);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getBookContent(String chapterUrl) {
        Document doc;
        String content = "";
        String preStr;
        String porStr;
        try {
            doc = Jsoup.connect(chapterUrl).
                    userAgent(ProxyHost.getProxyAgent()).
                    get();
            Elements elements = doc.getElementsByAttributeValue("id", "content");
            content = elements.toString();
            content = content.substring("<div id=\"content\">".length() + 1, content.indexOf("</div>"));
            content = content.replace(" ", "").
                    replace("<br>\n", "").
                    replace("<br>", "").
                    replace("&nbsp;&nbsp;&nbsp;&nbsp;", "");

            while (content.charAt(content.length() - 1) == '\n') {
                content = content.substring(0, content.length() - 1);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
