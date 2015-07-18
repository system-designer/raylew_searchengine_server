package com.searchengine.domain;

import java.io.Serializable;

/**
 * Created by RayLew on 2015/4/24.
 * QQ:897929321
 */
public class InvertedIndex implements BaseEntity, Serializable {
    private Long pid;
    private int position;
    private double tfidf;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getTfidf() {
        return tfidf;
    }

    public void setTfidf(double tf_idf) {
        this.tfidf = tf_idf;
    }
}
