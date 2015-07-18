package com.searchengine.service.impl;

import com.searchengine.dao.ForwardIndexDao;
import com.searchengine.domain.ForwardIndex;
import com.searchengine.domain.WordDF;
import com.searchengine.service.ForwardIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("forwardIndexService")
public class ForwardIndexServiceImpl extends ServiceBase implements ForwardIndexService {

    @Autowired
    private ForwardIndexDao forwardIndexDao;

    @Override
    public long add(ForwardIndex forwardIndex) {
        long id = GenerateId();
        forwardIndex.setId(id);
        Date nowDate = new Date();
        forwardIndex.setCreatedTime(nowDate);
        forwardIndex.setLastUpdatedTime(nowDate);
        int ret = forwardIndexDao.add(forwardIndex);
        if (ret > 0) {
            return id;
        }
        return 0;
    }

    @Override
    public List<ForwardIndex> getList(int pageIndex, int pageSize) {
        List<ForwardIndex> forwardIndexList = forwardIndexDao.getList((pageIndex - 1) * pageSize, pageSize);
        return forwardIndexList;
    }

    @Override
    public List<WordDF> getWordDFList() {
        List<WordDF> wordDFList = forwardIndexDao.getWordDFList();
        return wordDFList;
    }

    @Override
    public List<ForwardIndex> getListByWordIdList(List<Long> wordIdList) {
        List<ForwardIndex> forwardIndexList = null;
        try {
            forwardIndexList = forwardIndexDao.getListByWordIdList(wordIdList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return forwardIndexList;
    }

    @Override
    public List<Long> getDistinctPidList() {
        return forwardIndexDao.getDistinctPidList();
    }

    @Override
    public ForwardIndex getById(long id) {
        ForwardIndex forwardIndex = forwardIndexDao.getById(id);
        return forwardIndex;
    }

    @Override
    public int update(ForwardIndex forwardIndex) {
        return forwardIndexDao.update(forwardIndex);
    }
}
