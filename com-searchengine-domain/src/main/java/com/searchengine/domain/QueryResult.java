package com.searchengine.domain;

import java.io.Serializable;

public class QueryResult implements Serializable {

    private String title;
    private String content;
    private String url;
    private String filePath;
    private String fileCharset;
    private String date;
    private int rank;

    public QueryResult(String title, String content, String url, String filePath, String fileCharset, String date) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.filePath = filePath;
        this.fileCharset = fileCharset;
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileCharset() {
        return fileCharset;
    }

    public void setFileCharset(String fileCharset) {
        this.fileCharset = fileCharset;
    }

    public String getDate() {
        return date;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
