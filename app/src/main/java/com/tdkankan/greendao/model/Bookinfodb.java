package com.tdkankan.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author ZQZESS
 * @date 2021/5/23.
 * @file BookinfoDao
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
@Entity
public class Bookinfodb {
    @Id(autoincrement = true)
    Long bookID;//数据库书籍id

    String bookName;    //书名
    String author;  //作者
    String bookLink;    //书链接
    String picLink; //封面链接
    String bookIntroduction;    //简介
    String lastTime;    //最后更新时间
    String newChapter;  //最新章节
    String newChapterLink;  //最新章节链接
    int chapterNum; //总章节
    String linkFrom;    //书源
    String status;    //书源
    String category;    //书源

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
    }

    public String getPicLink() {
        return picLink;
    }

    public void setPicLink(String picLink) {
        this.picLink = picLink;
    }

    public String getBookIntroduction() {
        return bookIntroduction;
    }

    public void setInfo(String bookIntroduction) {
        this.bookIntroduction = bookIntroduction;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getNewChapter() {
        return newChapter;
    }

    public void setNewChapter(String newChapter) {
        this.newChapter = newChapter;
    }

    public String getNewChapterLink() {
        return newChapterLink;
    }

    public void setNewChapterLink(String newChapterLink) {
        this.newChapterLink = newChapterLink;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    public String getLinkFrom() {
        return linkFrom;
    }

    public void setLinkFrom(String linkFrom) {
        this.linkFrom = linkFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Generated(hash = 2044530104)
    public Bookinfodb(Long bookID, String bookName, String author, String bookLink, String picLink,
            String bookIntroduction, String lastTime, String newChapter, String newChapterLink,
            int chapterNum, String linkFrom, String status, String category) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.author = author;
        this.bookLink = bookLink;
        this.picLink = picLink;
        this.bookIntroduction = bookIntroduction;
        this.lastTime = lastTime;
        this.newChapter = newChapter;
        this.newChapterLink = newChapterLink;
        this.chapterNum = chapterNum;
        this.linkFrom = linkFrom;
        this.status = status;
        this.category = category;
    }

    @Generated(hash = 61057257)
    public Bookinfodb() {
    }
    public Long getBookID() {
        return this.bookID;
    }
    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public void setBookIntroduction(String bookIntroduction) {
        this.bookIntroduction = bookIntroduction;
    }


    
}
