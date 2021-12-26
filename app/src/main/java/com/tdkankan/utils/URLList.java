package com.tdkankan.utils;

import com.tdkankan.contains.url;


import java.util.ArrayList;

public class URLList {
    public static ArrayList<ArrayList<String>> getUrlList() {

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> subList = new ArrayList<String>();
//        subList.add("binifu");
//        subList.add(url.binifuURL);
//        subList.add(url.binifuSearchURL);
//        list.add(subList);
//
//        subList = new ArrayList<String>();
//        subList.add("biquge");
//        subList.add(url.biquge);
//        subList.add(url.biqugeSearchURL);
//        list.add(subList);

        subList = new ArrayList<String>();
        subList.add("xixi");
        subList.add(url.xixiURL);
        subList.add(url.xixiSearchURL);
        list.add(subList);

//        subList = new ArrayList<String>();
//        subList.add("xs59");
//        subList.add(url.xs59URL);
//        subList.add(url.xs59SearchURL);
//        list.add(subList);
//
//        subList = new ArrayList<String>();
//        subList.add("xs69shuba");
//        subList.add(url.xs69shubaURL);
//        subList.add(url.xs69shubaSearchURL);
//        list.add(subList);
//
//        subList = new ArrayList<String>();
//        subList.add("xs5200");
//        subList.add(url.xs5200URL);
//        subList.add(url.xs5200SearchURL);
//        list.add(subList);

        return list;
    }
}
