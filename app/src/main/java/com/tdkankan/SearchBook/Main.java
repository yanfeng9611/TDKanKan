package com.tdkankan.SearchBook;

import static java.lang.Thread.sleep;

import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.UrlConfig;
import com.tdkankan.Reptile.GetBook;
import com.tdkankan.Reptile.SearchBook;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{

        String searchContext = "诱惑";

        String webURL = UrlConfig.xs59URL;
        String searchURL = UrlConfig.xs59SearchURL;
        Xs59 web = new Xs59();
        ArrayList<HashMap<String, String>> list = web.searchBookEvent(webURL, searchURL, searchContext);
        BookInfo bookInfo = null;
        for (int idx = 0; idx < list.size(); idx++) {
            bookInfo = web.spiderBookInfo(list.get(idx).get("bookLink"));
            System.out.println(bookInfo);
        }

//        GetBook.ruku();

    }
}
