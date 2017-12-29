package org.boreas320.demo.api.model;

import java.util.Date;

/**
 * Created by xiangshuai on 2017/12/29.
 */
public class Note {
    public Note(Date cts, String content, String title, Person author) {
        this.cts = cts;
        this.content = content;
        this.title = title;
        this.author = author;
    }

    private Date cts;
    private String content;
    private String title;
    private Person author;

    public Date getCts() {
        return cts;
    }

    public void setCts(Date cts) {
        this.cts = cts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }
}
