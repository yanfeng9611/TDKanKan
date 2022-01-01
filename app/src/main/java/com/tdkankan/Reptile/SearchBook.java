package com.tdkankan.Reptile;


import static java.lang.Thread.sleep;

import android.util.Log;

import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Cache.ImageCacheManager;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.utils.URLList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ZQZESS
 * @date 1/6/2021.
 * @file SearchBook
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class SearchBook {

    public static ArrayList<HashMap<String,String>> searchBookEvent(String keyWords){
        ArrayList<ArrayList<String>> list = URLList.getUrlList();
        ArrayList<HashMap<String,String>> bookList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String,String>> book;
        try {
            for (final List<String> webInfo : list) {
                String className = "com.tdkankan.SearchBook." + webInfo.get(0);
                Class clz = Class.forName(className);
                Constructor constructor = clz.getConstructor();
                Object obj = constructor.newInstance();
                Method method = clz.getMethod("searchBookEvent", String.class, String.class, String.class);
                book = (ArrayList<HashMap<String, String>>) method.invoke(obj, webInfo.get(1), webInfo.get(2), keyWords);
                bookList.addAll(book);
            }
//            sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

}
