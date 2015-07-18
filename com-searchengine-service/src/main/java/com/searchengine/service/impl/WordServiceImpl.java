package com.searchengine.service.impl;

import com.searchengine.dao.WordDao;
import com.searchengine.domain.Word;
import com.searchengine.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("wordService")
public class WordServiceImpl extends ServiceBase implements WordService {

    @Autowired
    private WordDao wordDao;

    @Override
    public long add(Word word) {
        long id = GenerateId();
        word.setId(id);
        Date nowDate = new Date();
        word.setCreatedTime(nowDate);
        word.setLastUpdatedTime(nowDate);
        int ret = wordDao.add(word);
        if (ret > 0) {
            return id;
        }
        return 0;
    }

    @Override
    public List<Word> getList() {
        List<Word> wordList = wordDao.getList();
        return wordList;
    }

    @Override
    public List<Word> getListBy(int pageIndex, int pageSize) {
        List<Word> wordList = wordDao.getListBy((pageIndex - 1) * pageSize, pageSize);
        return wordList;
    }

    @Override
    public long getCount() {
        return wordDao.getCount();
    }

    @Override
    public Word getById(long id) {
        Word word = wordDao.getById(id);
        return word;
    }

    @Override
    public int update(Word word) {
        return wordDao.update(word);
    }
}
