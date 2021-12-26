package com.tdkankan.Cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tdkankan.Data.BitmapUtils;
import com.tdkankan.Data.Book;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.Data.GlobalConfig;
import com.tdkankan.Data.Picture;
import com.tdkankan.Reptile.GetBook;
import com.tdkankan.SearchBook.biquge;
import com.tdkankan.contains.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZQZESS
 * @date 12/13/2020.
 * @file BookInfoCache
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class BookInfoCache extends HashMap {
    private static ConcurrentHashMap<String, Bitmap> imageMap = new ConcurrentHashMap<>();
    public static Bitmap loadImage(String picLink) {
        if (imageMap.containsKey(picLink)) {
            Log.d("picCacheGet", "图片缓存已存在:" + picLink);
            return imageMap.get(picLink);
        } else {
//            Log.d("picCacheGet", "图片缓存不存在：" + picLink);
            initPicture(picLink);
            return imageMap.get(picLink);
        }
    }

    private static void initPicture(String picLink) {
        try {
            //通过传入的图片地址，获取图片
            HttpURLConnection connection = (HttpURLConnection) (new URL(picLink).openConnection());
            InputStream is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                Log.d("pic", "图片获取成功");
                bitmap = BitmapUtils.compressImage(bitmap);//图片压缩
                imageMap.put(picLink, bitmap);
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public static BookInfo loadBook(String bookLink) {
        if (GlobalConfig.bookmap.containsKey(bookLink)) {
            BookInfo book = GlobalConfig.bookmap.get(bookLink);
            Log.d("bookCacheGet", "图书缓存已存在:" + bookLink);
            return book;
        } else {
            Log.d("bookCacheGet", "图书缓存不存在:" + bookLink);
//            BookInfo book2 = GetBook.GetBookInfo(id);//爬虫爬取书本信息
//            BookInfo book2 = new BookInfo(id, bookInfo);
//            GlobalConfig.bookmap.put(id, book2);//书本信息存入hashmap缓存
//            return book2;
            return null;
        }
    }

}
