package com.searchengine.service.impl;

import com.searchengine.dao.PageWordDao;
import com.searchengine.domain.PageWord;
import com.searchengine.service.PageWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pageWordService")
public class PageWordServiceImpl extends ServiceBase implements PageWordService {

    @Autowired
    private PageWordDao pageWordDao;

    @Override
    public List<PageWord> getListByWordIdList(List<Long> wordIdList) {
        List<PageWord> pageWordList = null;
        try {
            pageWordList = pageWordDao.getListByWordIdList(wordIdList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pageWordList;
    }

    @Override
    public List<PageWord> getListBy(int pageIndex, int pageSize) {
        List<PageWord> pageWordList = null;
        try {
            pageWordList = pageWordDao.getListBy((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pageWordList;
    }

    @Override
    public Long getCount() {
        return 1662511L;
    }
}
