package com.searchengine.domain;

/**
 * Created by RayLew on 2015/3/3.
 */
public class WordDF {
    private long wordId;
    private int docFrequency;

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public int getDocFrequency() {
        return docFrequency;
    }

    public void setDocFrequency(int docFrequency) {
        this.docFrequency = docFrequency;
    }
}
