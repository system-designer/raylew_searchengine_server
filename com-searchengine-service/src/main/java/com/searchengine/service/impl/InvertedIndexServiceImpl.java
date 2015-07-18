package com.searchengine.service.impl;

import com.searchengine.dao.ForwardIndexDao;
import com.searchengine.domain.InvertedIndex;
import com.searchengine.service.InvertedIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("invertedIndexService")
public class InvertedIndexServiceImpl extends ServiceBase implements InvertedIndexService {

    @Autowired
    private ForwardIndexDao forwardIndexDao;

    @Override
    public List<InvertedIndex> getList(Long wid) {
        List<InvertedIndex> invertedIndexList = forwardIndexDao.getInvertedIndexList(wid);
        return invertedIndexList;
    }
}
