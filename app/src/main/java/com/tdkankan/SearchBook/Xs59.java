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
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Xs59 {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        String bookName = "";
        String author = "";  //作者
        String bookLink = "";    //书链接
        String picLink = ""; //封面链接
        String newChapter = "";  //最新章节
        String newChapterLink = "";  //最新章节链接
        String status = "";
        String category = "";
        try {

            HashMap<String, String > book = null;
            Map<String, String> postParam = new HashMap<>();
            postParam.put("q", keyWords);
            alldoc = Jsoup.connect(searchURL).
                    data(postParam).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    post();
            Elements elements = alldoc.getElementsByTag("dl");
            for (Element item : elements) {
                book = new HashMap<>();
                bookLink = UrlConfig.xs59URL + item.getElementsByTag("a").get(0).attr("href");
                bookName = item.getElementsByTag("a").get(1).text().trim();
                picLink = UrlConfig.xs59URL + item.getElementsByTag("img").attr("src");
                author = item.getElementsByTag("span").get(1).text().trim();
                status = item.getElementsByTag("span").get(2).text().trim();
                category = item.getElementsByTag("span").get(3).text().trim();
                newChapter = item.getElementsByTag("a").get(4).text().trim();
                newChapterLink = UrlConfig.xs59URL + item.getElementsByTag("a").get(4).attr("href");

                book.put("bookLink", bookLink);
                book.put("bookName", bookName);
                book.put("picLink", picLink);
                book.put("author", author);
                book.put("status", status);
                book.put("category", category);
                book.put("newChapter", newChapter);
                book.put("newChapterLink", newChapterLink);
                book.put("linkFrom", UrlConfig.xs59);
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
        HashMap<String, String> book = new HashMap<String, String>();
        try {
            Document doc = Jsoup.connect(bookURL).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    get();

            bookName = doc.getElementsByTag("meta").get(8).attr("content");
            bookIntroduction = doc.getElementsByTag("meta").get(9).attr("content");
            picLink = UrlConfig.xs59URL + doc.getElementsByTag("meta").get(10).attr("content");
            category = doc.getElementsByTag("meta").get(11).attr("content");
            author = doc.getElementsByTag("meta").get(12).attr("content");
            bookLink = doc.getElementsByTag("meta").get(14).attr("content");
            status = doc.getElementsByTag("meta").get(16).attr("content");
            lastTime = doc.getElementsByTag("meta").get(17).attr("content");
            newChapter = doc.getElementsByTag("meta").get(18).attr("content");
            newChapterLink = doc.getElementsByTag("meta").get(19).attr("content");
            linkFrom = UrlConfig.xs59;
            chapterNum = doc.getElementById("allChapter").getElementsByTag("li").size();

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

    public void getChapterList(String bookLink) {
        Document doc;
        ConcurrentHashMap<String,String > chapterMap = null;
        try {
            doc = Jsoup.connect(bookLink).
                    userAgent(ProxyHost.getProxyAgent()).
                    get();
            Elements elements = doc.getElementById("allChapter").getElementsByTag("li");

            for (Element item : elements) {
                chapterMap = new ConcurrentHashMap<>();

                String chapterLink = UrlConfig.xs59URL + item.getElementsByTag("a").attr("href");//获取章节link
                String chapterTitle = item.getElementsByTag("a").get(0).getElementsByTag("span").text();//获取章节名
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
            Elements elements = doc.getElementsByAttributeValue("id", "BookText");
            content = elements.toString();
            while (content.contains("【")) {
                preStr = content.substring(0, content.indexOf("【"));
                porStr = content.substring(content.indexOf("】") + 1);
                content = preStr + porStr;
            }
            content = content.replaceAll("本书源[^)]*阅读网", "").
                    replaceAll("本部[^)]*阅读网", "").
                    replace(".net", "").
                    replace("(.com)", "").
                    replace("（书迷楼）", "").
                    replaceAll("书迷楼[^)]*书迷楼。", "").
                    replace("热门推荐：", "");

            content = content.replace("<div id=\"BookText\">", "").
                    replace("</div>", "").
                    replace("\uFEFF", "").
                    replace("\n &nbsp;&nbsp;&nbsp;&nbsp;", "").
                    replace(" &nbsp;&nbsp;&nbsp;&nbsp;\n", "").
                    replace(" <br> \n", "").
                    replace("<br>", "");
            preStr = content.substring(0, content.indexOf("\n"));
            Pattern pattern = Pattern.compile("第[0-9]*章");
            Matcher matcher = pattern.matcher(preStr);
            if (matcher.find()) {
                content = content.substring(preStr.length() + 1);
            }
            content = content.
                    replace(" ", "").
                    replace("\n", "").
                    replace("&nbsp;&nbsp;&nbsp;&nbsp;", "\n");

            if (content.charAt(0) == '\n') {
                content = content.substring(1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}