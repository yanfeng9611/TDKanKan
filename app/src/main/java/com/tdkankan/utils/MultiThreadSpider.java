package com.tdkankan.utils;

import com.tdkankan.Data.GlobalConfig;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MultiThreadSpider {
    public static ArrayList<HashMap<String, String>> spiderBookInfo(Elements elements, String webURL, String className) throws Exception{
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        List<MyCallableThread> tasks = new ArrayList<MyCallableThread>();
        for (Element listItem : elements) {

            tasks.add(new MyCallableThread(listItem, webURL, "com.tdkankan.SearchBook." + className));
        }
        List<Future<HashMap>> futures = cachedThreadPool.invokeAll(tasks, GlobalConfig.threadPoolTimeOut, TimeUnit.SECONDS);
        HashMap map = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (Future future : futures) {
            map = (HashMap<String, String>)future.get();
            if (map.get("bookLink").equals("") || map.get("bookIntroduction").equals("")) {
                continue;
            }
            list.add((HashMap<String, String>)future.get());
        }
        cachedThreadPool.shutdown();
        return list;
    }
}
