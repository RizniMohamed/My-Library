package com.riznicreation.mylibrary;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public class Book {
    private int id;
    private String name;
    private String author;
    private int pages;
    private String imgURL;
    private String shortDesc;
    private String LongDesc;
    private boolean isExpanded;
    private String buyLink;

    public Book() {
        this.id = 0;
        this.name = "";
        this.author = "";
        this.pages = 0;
        this.imgURL = "";
        this.shortDesc = "";
        LongDesc = "";
        isExpanded = false;
        this.buyLink = "";
    }

    public Book(int id, String name, String author, int pages, String imgURL, String shortDesc, String longDesc, String buyLink) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.pages = pages;
        this.imgURL = imgURL;
        this.shortDesc = shortDesc;
        LongDesc = longDesc;
        isExpanded = false;
        this.buyLink = buyLink;
    }

    public boolean isExpanded() {
        return !isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return LongDesc;
    }

    public void setLongDesc(String longDesc) {
        LongDesc = longDesc;
    }

    public String getBuyLink() { return buyLink; }

    public void setBuyLink(String buyLink) { this.buyLink = buyLink; }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                ", imgURL='" + imgURL + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", LongDesc='" + LongDesc + '\'' +
                ", isExpanded=" + isExpanded +
                ", buyLink='" + buyLink + '\'' +
                '}';
    }
}
