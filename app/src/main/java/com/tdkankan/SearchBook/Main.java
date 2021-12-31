package com.tdkankan.SearchBook;

import static java.lang.Thread.sleep;

import com.tdkankan.Data.BookInfo;
import com.tdkankan.Reptile.SearchBook;
import com.tdkankan.contains.url;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{

        String searchContext = "诱惑";
//        ArrayList<HashMap<String,String>> bookList = SearchBook.searchBookEvent(searchContext);


        String webURL = url.xs5200URL;
        String searchURL = url.xs5200SearchURL;
        xs5200 biquge = new xs5200();
        ArrayList<HashMap<String, String>> list = biquge.searchBookEvent(webURL, searchURL, searchContext);
//        System.out.println(list);
//        System.out.println(list.size());
        BookInfo bookInfo = null;
        for (int idx = 0; idx < list.size(); idx++) {
            bookInfo = biquge.spiderBookInfo(list.get(idx).get("bookLink"));
            System.out.println(bookInfo);
        }
    }
}
