package com.tdkankan.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.dialog.dialog.LoadingDialog;
import com.tdkankan.Cache.BookInfoCache;
import com.tdkankan.Cache.ImageCacheManager;
import com.tdkankan.Data.BookInfo;
import com.tdkankan.R;
import com.tdkankan.greendao.DaoHelper;
import com.tdkankan.greendao.model.Bookinfodb;

/**
 * @author ZQZESS
 * @date 12/9/2020-9:21 PM
 * @file BookInfoDetailActivity.java
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class BookInfoDetailActivity extends AppCompatActivity {
    TextView tv_title;
    TextView tv_name;
    TextView tv_info;
    TextView tv_lasttime;
    TextView tv_newchapter;
    TextView tv_author;
    ImageView img_pic;
    Button btn_add;
    Button btn_read;
    String bookName;    //书名
    String author;  //作者
    String bookLink;    //书链接
    String picName; //封面名字
    String picLink; //封面链接
    String bookIntroduction;    //简介
    String lastTime;    //最后更新时间
    String newChapter;  //最新章节
    String newChapterLink;  //最新章节链接
    int chapterNum; //总章节
    String linkFrom;    //书源
    String status;    // 状态
    String category;    //类别
    BookInfo bookInfo;
    LoadingDialog loadingDialog;
    DaoHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfodetail);
        getSupportActionBar().hide();

        findId();
//        initView();
        loadingDialog=new LoadingDialog(this);
        new GetDataTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        mDb=DaoHelper.getInstance(BookInfoDetailActivity.this);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookInfoDetailActivity.this, ReadingActivity.class);
                intent.putExtra("bookLink", bookLink);
                intent.putExtra("linkFrom",linkFrom);
                startActivity(intent);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Long bookid;
                String name;    //书名
                String author;  //作者
                String link;    //书链接
                String piclink; //封面链接
                String info;    //简介
                String lasttime;    //最后更新时间
                String newchapter;  //最新章节
                String newchapterlink;  //最新章节链接
                int chapternum; //总章节
                String linkfrom;    //书源
                 */
//                String name=BookInfoCache.loadBook(link).getName();
                if(btn_add.getText().toString().equals("加入书架"))
                {//加入书架
                    Bookinfodb book=new Bookinfodb(null, bookName, author,
                            bookLink, picLink, bookIntroduction, lastTime, newChapter,
                            newChapterLink,chapterNum,linkFrom, status, category);
                    mDb.insertOrReplace(book);
                    if(mDb.search(bookLink)!=null)
                    {
                        Toast.makeText(BookInfoDetailActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        btn_add.setText("移出书架");
                    }
                }else if(btn_add.getText().toString().equals("移出书架"))
                {//移出书架
                    mDb.delete(mDb.search(bookLink).getBookID());
                    if(mDb.search(bookLink)==null)
                    {
                        btn_add.setText("加入书架");
                    }
                }
            }
        });
    }

    private  void GetData() {
        Intent intent = getIntent();
        bookName = intent.getStringExtra("bookName");
        bookIntroduction = intent.getStringExtra("bookIntroduction");
        bookLink = intent.getStringExtra("bookLink");
        author = intent.getStringExtra("author");
        picLink = intent.getStringExtra("picLink");
//        newChapter = intent.getStringExtra("newChapter");
//        lastTime = intent.getStringExtra("lastTime");
//        chapterNum = intent.getIntExtra("chapterNum", 0);

//        System.out.println("bookLink: " + bookLink);
        preInitBookInfo(bookLink);
//        System.out.println("bookInfo: " + bookInfo);
        newChapter = bookInfo.getNewChapter();
        newChapterLink = bookInfo.getNewChapterLink();
        lastTime = bookInfo.getLastTime();
        chapterNum = bookInfo.getChapterNum();
        linkFrom = bookInfo.getLinkFrom();
        bookLink = bookInfo.getBookLink();
        if(mDb.search(bookLink) != null) {
            btn_add.setText("移出书架");
        }
    }
    private void initView() {

        Bitmap bitmap= BookInfoCache.loadImage(picLink);

        tv_title.setText(bookName);
        tv_name.setText(bookName);
        tv_info.setText(bookIntroduction);
        tv_author.setText(author);
        img_pic.setImageBitmap(bitmap);
        tv_lasttime.setText(lastTime);
        tv_newchapter.setText(newChapter);
    }

    private void findId() {
        tv_title = findViewById(R.id.tv_bookinfo_detail_title);
        tv_name = findViewById(R.id.tv_bookinfo_detail_name);
        tv_info = findViewById(R.id.tv_bookinfo_detail_info);
        tv_lasttime = findViewById(R.id.tv_bookinfo_detail_lasttime);
        tv_newchapter = findViewById(R.id.tv_bookinfo_detail_newchapter);
        tv_author = findViewById(R.id.tv_bookinfo_detail_author);
        img_pic = findViewById(R.id.img_bookinfo_detail_1);
        btn_add = findViewById(R.id.btn_bookinfo_detail_add);
        btn_read = findViewById(R.id.btn_bookinfo_detail_read);
    }

    private Bitmap getBitmapFromRes(int resId) {
        Resources res = this.getResources();
        return BitmapFactory.decodeResource(res, resId);
    }

    private void preInitBookInfo(String url) {
        bookInfo = BookInfoCache.loadBook(url);
    }
    private class GetDataTask extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.setMessage("加载中...");
            loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
            loadingDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            GetData();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            loadingDialog.dismiss();
            initView();
            ImageCacheManager.loadImage(picLink, img_pic, getBitmapFromRes(R.drawable.nonepic), getBitmapFromRes(R.drawable.nonepic));
        }
    }
}
