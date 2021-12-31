package com.tdkankan.SearchBook;

import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.contains.url;
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
        String bookName = "";
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
        try {

            keyWords = URLEncoder.encode(keyWords, "gbk");
            String submit = "%CB%D1%CB%F7";
            String url = "?searchkey=" + keyWords + "&submit=" + submit;
            alldoc = Jsoup.connect(searchURL + url).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            Elements elements = alldoc.getElementsByTag("tr");
            HashMap<String, String> book = null;
            int idx = 0;
            for (Element item : elements) {
                if (idx++ == 0) continue;
                book = new HashMap<>();
                bookLink = item.getElementsByTag("a").get(0).attr("href");
                bookName = item.getElementsByTag("a").get(0).text().trim();
                newChapter = item.getElementsByTag("a").get(1).text().trim();
                newChapterLink = item.getElementsByTag("a").get(1).attr("href");
                author = item.getElementsByTag("td").get(2).text();
                lastTime = item.getElementsByTag("td").get(4).text();
                status = item.getElementsByTag("td").get(5).text();

                book.put("bookLink", bookLink);
                book.put("bookName", bookName);
                book.put("newChapter", newChapter);
                book.put("newChapterLink", newChapterLink);
                book.put("author", author);
                book.put("status", status);
                book.put("lastTime", lastTime);
                book.put("linkFrom", webURL);
                list.add(book);
                break;
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
            Document doc = Jsoup.connect(bookURL).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            System.out.println();
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
            linkFrom = url.xixiURL;
            chapterNum = doc.getElementsByAttributeValue("class", "chapterlist").get(1).getElementsByTag("a").size();

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
