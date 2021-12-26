package com.tdkankan.SearchBook;


import static java.lang.Thread.sleep;

import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
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
        try {
            xixi xixi = new xixi();
            String res = xixi.sendPost(searchURL, keyWords);
            alldoc = Jsoup.parse(res);

            Elements elements = alldoc.getElementsByTag("tr");

            list = MultiThreadSpider.spiderBookInfo(elements, webURL, "xixi");

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
            connection.setConnectTimeout(GlobalConfig.jsoupTimeOut);
            connection.setReadTimeout(GlobalConfig.jsoupTimeOut);
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

    public HashMap spiderBookInfo(Element listItem, String webURL) {
//        System.out.println(listItem);
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

            bookLink = listItem.getElementsByAttributeValue("class", "odd").get(0).getElementsByTag("a").get(0).attr("href");
            bookName = listItem.getElementsByAttributeValue("class", "odd").get(0).getElementsByTag("a").get(0).text().trim();
            author = listItem.getElementsByAttributeValue("class", "odd").get(1).text().trim();
            newChapter = listItem.getElementsByTag("a").get(1).text().trim();
            newChapterLink = listItem.getElementsByTag("a").get(1).attr("href");
            Document doc = Jsoup.connect(bookLink).
                    data("query", "Java").
//                    proxy(ProxyHost.getProxyHost()).
                    userAgent(ProxyHost.getProxyAgent()).
                    timeout(GlobalConfig.jsoupTimeOut).
                    get();
            picLink = doc.getElementById("fmimg").getElementsByTag("img").attr("src");
            String subStr = doc.getElementsByAttributeValue("class", "motion").get(0).getElementsByTag("a").get(0).attr("href");
            bookLink = webURL + subStr;
            status = doc.getElementById("info").getElementsByTag("p").get(2).text().trim().replace("小说状态：", "");

            bookIntroduction = doc.getElementById("intro").text().trim().replace("小说简介：", "");

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
//        System.out.println(book);
        return book;
    }
}
