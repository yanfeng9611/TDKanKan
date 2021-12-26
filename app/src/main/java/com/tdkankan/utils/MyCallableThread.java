package com.tdkankan.utils;

import com.tdkankan.proxy.ProxyHost;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MyCallableThread implements Callable<HashMap> {

    private Element listItem = null;
    private String webURL = "";
    private String proxy = "";
    private String className = "";
    private HashMap<String, String> book;

    public MyCallableThread(Element listItem, String webURL, String className) {
        this.listItem = listItem;
        this.webURL = webURL;
        this.className = className;
        this.proxy = ProxyHost.getProxyAgent();
    }

    @Override
    public HashMap call() throws Exception {
        Class clz = Class.forName(this.className);
        Constructor constructor = clz.getConstructor();
        Object obj = constructor.newInstance();
        Method method = clz.getMethod("spiderBookInfo", Element.class, String.class);
        book = (HashMap<String, String>) method.invoke(obj, this.listItem, this.webURL);
        return this.book;
    }
}