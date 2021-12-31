package com.tdkankan.SearchBook;


import static java.lang.Thread.sleep;

import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.R;
import com.tdkankan.contains.url;
import com.tdkankan.proxy.ProxyHost;
import com.tdkankan.utils.MultiThreadSpider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class xixi {
    public ArrayList searchBookEvent(String webURL, String searchURL, String keyWords) {
        Document alldoc;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        xixi xixi = new xixi();
        String res = xixi.sendPost(searchURL, keyWords);
        alldoc = Jsoup.parse(res);
        Elements elements = alldoc.getElementsByTag("tr");
        String bookLink = "";
        String bookName = "";
        String author = "";
        String newChapter = "";
        String newChapterLink = "";
        HashMap<String, String > book = null;
        int idx = 0;
        try {
            for (Element item : elements) {
                if (idx++ == 0) continue;
                book = new HashMap<>();
                bookLink = item.getElementsByAttributeValue("class", "odd").get(0).getElementsByTag("a").get(0).attr("href");
                bookName = item.getElementsByAttributeValue("class", "odd").get(0).getElementsByTag("a").get(0).text().trim();
                author = item.getElementsByAttributeValue("class", "odd").get(1).text().trim();
                newChapter = item.getElementsByTag("a").get(1).text().trim();
                newChapterLink = item.getElementsByTag("a").get(1).attr("href");

                book.put("bookLink", bookLink);
                book.put("bookName", bookName);
                book.put("author", author);
                book.put("newChapter", newChapter);
                book.put("newChapterLink", newChapterLink);
                book.put("linkFrom", webURL);
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String sendPost(String searchURL, String keyWords){
        StringBuffer buff = new StringBuffer();
        BufferedWriter writer = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        try{
            URL url = new URL(searchURL);
//            connection = (HttpURLConnection) url.openConnection(ProxyHost.getProxyHost());
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("POST");   //设置请求方式为POST
            connection.setDoOutput(true);   //允许写出
            connection.setDoInput(true);    //允许读入
            connection.setUseCaches(false);    //不使用缓存
//            connection.setConnectTimeout(GlobalConfig.jsoupTimeOut);
//            connection.setReadTimeout(GlobalConfig.jsoupTimeOut);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            String body = "type=articlename&s=" + keyWords;
            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "gbk"));
            writer.write(body);
            writer.flush();

            int responseCode = connection.getResponseCode();
            String line = "";
            if(responseCode == HttpURLConnection.HTTP_OK){
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gbk"));
                while (( line = bufferedReader.readLine()) != null) {
                    buff.append(line);
                }
            }

        } catch(Exception e){
//            e.printStackTrace();
        } finally {
            if (writer != null) {
                try{
                    writer.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try{
                    bufferedReader.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return buff.toString();
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
        String linkFrom = "";    //书源
        String status = "";
        String category = "";
        int chapterNum = 0; //总章节
        HashMap<String,String> book = new HashMap<String,String>();
        try {
            Document doc = Jsoup.connect(bookURL).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
//                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            bookName = doc.getElementsByTag("meta").get(8).attr("content");
            bookLink = url.xixiURL + doc.getElementsByAttributeValue("class", "motion").get(0).getElementsByTag("a").attr("href");
            bookIntroduction = doc.getElementsByTag("meta").get(9).attr("content");
            picLink = doc.getElementsByTag("meta").get(10).attr("content");
            category = doc.getElementsByTag("meta").get(11).attr("content");
            author = doc.getElementsByTag("meta").get(12).attr("content");
            status = doc.getElementsByTag("meta").get(15).attr("content");
            lastTime = doc.getElementsByTag("meta").get(16).attr("content");
            newChapter = doc.getElementsByTag("meta").get(17).attr("content");
            newChapterLink = doc.getElementsByTag("meta").get(18).attr("content");
            linkFrom = url.xixiURL;

            doc = Jsoup.connect(bookLink).
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            chapterNum = doc.getElementsByTag("dd").size();

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
