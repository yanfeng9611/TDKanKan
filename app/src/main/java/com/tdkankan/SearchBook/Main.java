package com.tdkankan.SearchBook;

import static java.lang.Thread.sleep;

import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.Data.UrlConfig;
import com.tdkankan.Reptile.GetBook;
import com.tdkankan.Reptile.SearchBook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception{

        String searchContext = "诱惑";

//        String webURL = UrlConfig.xs5200URL;
//        String searchURL = UrlConfig.xs5200SearchURL;
//        Xs5200 web = new Xs5200();
//        ArrayList<HashMap<String, String>> list = web.searchBookEvent(webURL, searchURL, searchContext);
//        BookInfo bookInfo = null;
//        System.out.println(list.get(0).get("bookLink"));
//        bookInfo = web.spiderBookInfo(list.get(0).get("bookLink"));
//        System.out.println(bookInfo.getBookLink() + ": " + bookInfo.getChapterNum());
//        System.out.println(list.get(0).get("bookLink"));
//        String bookLink = list.get(0).get("bookLink");
//        web.getChapterList(bookLink);
//        String chapterLink = GlobalConfig.list.get(1).get("chapterLink");
//        System.out.println(chapterLink);
//        web.getBookContent(chapterLink);

//        String str = "    第2章 这是我的地狱()";
//        Pattern pattern = Pattern.compile("第[0-9]*章");
//        Matcher matcher = pattern.matcher(str);
//        System.out.println(matcher.find());

//        System.out.println(Pattern.matches("第[0-9]*章", str));
//        System.out.println(str.matches("第[0-9]*章"));
//        str = str.replaceAll("第[0-9]*章", "");
//        System.out.println(str);

//        str = str.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;第[0-9]+章：[^)]* <br> \n", "");


        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("HH:mm");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println("现在时间：" + sdf.format(date));

    }
}
