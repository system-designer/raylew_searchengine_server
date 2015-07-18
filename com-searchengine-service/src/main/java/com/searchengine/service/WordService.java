package com.searchengine.service;

import com.searchengine.domain.Word;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordService {
    /**
     * 添加新的词语
     *
     * @param word
     * @return
     */
    long add(Word word);

    /**
     * 通过wordId得到词
     *
     * @param id
     * @return
     */
    Word getById(long id);

    /**
     * 得到所有词语
     *
     * @return
     */
    List<Word> getList();

    /**
     * 得到所有词语
     *
     * @return
     */
    List<Word> getListBy(int pageIndex, int pageSize);

    /**
     * 得到词语总数
     * @return
     */
    long getCount();

    /**
     * 修改word
     *
     * @param word
     * @return
     */
    int update(Word word);
}
