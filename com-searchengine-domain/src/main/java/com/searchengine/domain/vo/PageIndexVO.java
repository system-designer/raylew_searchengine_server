package com.searchengine.domain.vo;

import java.util.Date;

/**
 * Created by RayLew on 2015/2/4.
 */
@Deprecated
public class PageIndexVO {
    public PageIndexVO(){}

    public PageIndexVO(String url, String content, int offset, String raws) {
        this.url = url;
        this.content = content;
        this.offset = offset;
        this.raws = raws;
    }

    private long id;
    private String url;
    private String content;
    private int offset;
    private String raws;
    private Date createdTime;
    private Date lastUpdatedTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getRaws() {
        return raws;
    }

    public void setRaws(String raws) {
        this.raws = raws;
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
}
