package com.tdkankan.SearchBook;

import static java.lang.Thread.sleep;

import com.tdkankan.Reptile.SearchBook;
import com.tdkankan.contains.url;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception{
//        getSpiderBiqugePage();
        Date date = new Date();
        long timeStart = date.getTime();
        String searchContext = "校花";
        ArrayList<HashMap<String,String>> bookList = SearchBook.searchBookEvent(searchContext);
        System.out.println(bookList);
        System.out.println(bookList.size());
        long timeEnd = date.getTime();
        System.out.println(timeEnd - timeStart);

        String webURL = url.xixiURL;
        String searchURL = url.xixiSearchURL;
        xixi xixi = new xixi();

        timeStart = date.getTime();
        ArrayList<HashMap<String, String>> list = xixi.searchBookEvent(webURL, searchURL, searchContext);
        System.out.println(list);
        System.out.println(list.size());
        timeEnd = date.getTime();
        System.out.println(timeEnd - timeStart);


    }

}
