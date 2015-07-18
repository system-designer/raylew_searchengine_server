package com.searchengine.preprocess.beta.invertedIndex;

import com.searchengine.domain.Word;

import java.util.List;

/**
 * 负责分发关键词到不同的线程
 */
public class WordDispatcher {

    /**
     * 当前还需要建立倒排索引的词语
     */
    private List<Word> wordList = null;

    public WordDispatcher(List<Word> wordList) {
        this.wordList = wordList;
    }

    /**
     * 同步从词列表中得到词
     *
     * @return
     */
    public synchronized Word getWord() {
        while (wordList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.notify();
        Word word = wordList.get(0);
        wordList.remove(word);
        return word;
    }

    /**
     * 同步得到词列表剩余词
     *
     * @return
     */
    public int getSize() {
        return wordList.size();
    }
}
