package com.tdkankan.utils;

import com.tdkankan.Data.UrlConfig;


import java.util.ArrayList;

public class URLList {
    public static ArrayList<ArrayList<String>> getUrlList() {

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> subList = new ArrayList<String>();
//        subList.add("Binifu");
//        subList.add(UrlConfig.binifuURL);
//        subList.add(UrlConfig.binifuSearchURL);
//        list.add(subList);

//        subList = new ArrayList<String>();
//        subList.add("Biquge");
//        subList.add(UrlConfig.biqugeURL);
//        subList.add(UrlConfig.biqugeSearchURL);
//        list.add(subList);

//        subList = new ArrayList<String>();
//        subList.add("Xixi");
//        subList.add(UrlConfig.xixiURL);
//        subList.add(UrlConfig.xixiSearchURL);
//        list.add(subList);

//        subList = new ArrayList<String>();
//        subList.add("Xs59");
//        subList.add(UrlConfig.xs59URL);
//        subList.add(UrlConfig.xs59SearchURL);
//        list.add(subList);

//        subList = new ArrayList<String>();
//        subList.add("Xs69shuba");
//        subList.add(UrlConfig.xs69shubaURL);
//        subList.add(UrlConfig.xs69shubaSearchURL);
//        list.add(subList);

        subList = new ArrayList<String>();
        subList.add("Xs5200");
        subList.add(UrlConfig.xs5200URL);
        subList.add(UrlConfig.xs5200SearchURL);
        list.add(subList);

        return list;
    }
}
