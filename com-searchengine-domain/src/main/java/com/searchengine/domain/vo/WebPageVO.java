package com.searchengine.domain.vo;

import com.searchengine.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RayLew on 2015/2/11.
 */
public class WebPageVO implements BaseEntity, Serializable {
    private long id;
    private String url;
    private String title;
    private String charset;
    private String keywords;
    private String description;
    private int wordCount;
    private String domain;
    private String savePath;
    private String rootDomain;
    private String protocol;
    private String contentMD5;
    private Date createdTime;
    private Date lastUpdatedTime;
    private boolean analyzed;

    public WebPageVO() {
    }

    public WebPageVO(String url, String title, String charset, String keywords, String description, int wordCount, String domain, String savePath) {
        this.url = url;
        this.title = title;
        this.charset = charset;
        this.keywords = keywords;
        this.description = description;
        this.wordCount = wordCount;
        this.domain = domain;
        this.savePath = savePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getRootDomain() {
        return rootDomain;
    }

    public void setRootDomain(String rootDomain) {
        this.rootDomain = rootDomain;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public void setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public boolean isAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }
}
