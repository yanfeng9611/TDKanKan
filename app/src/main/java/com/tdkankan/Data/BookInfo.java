package com.tdkankan.Data;

/**
 * @author ZQZESS
 * @date 12/13/2020.
 * @file BookInfo
 * GitHub：https://github.com/zqzess
 * 不会停止运行的app不是好app w(ﾟДﾟ)w
 */
public class BookInfo {
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
    String status;
    String category;

    public BookInfo(String bookName, String author, String bookLink,
                    String picLink, String bookIntroduction,
                    String lastTime, String newChapter, String newChapterLink,
                    int chapterNum, String status, String category, String linkFrom) {
        this.bookName = bookName;
        this.author = author;
        this.bookLink = bookLink;
        this.picLink = picLink;
        this.bookIntroduction = bookIntroduction;
        this.lastTime = lastTime;
        this.newChapter = newChapter;
        this.newChapterLink = newChapterLink;
        this.chapterNum = chapterNum;
        this.status = status;
        this.category = category;
        this.linkFrom = linkFrom;
    }

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

    public void setBookIntroduction(String bookIntroduction) {
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
}