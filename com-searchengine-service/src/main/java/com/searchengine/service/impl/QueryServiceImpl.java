package com.searchengine.service.impl;

import com.searchengine.dao.QueryDao;
import com.searchengine.domain.Query;
import com.searchengine.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("queryService")
public class QueryServiceImpl extends ServiceBase implements QueryService {

    @Autowired
    private QueryDao queryDao;

    @Override
    public long add(Query query) {
        long id = GenerateId();
        query.setId(id);
        Date nowDate = new Date();
        query.setCreatedTime(nowDate);
        int ret = queryDao.add(query);
        if (ret > 0) {
            return id;
        }
        return 0;
    }

    @Override
    public Query get(String queryContent) {
        return queryDao.get(queryContent);
    }

    @Override
    public List<Query> getList(int pageIndex, int pageSize) {
        List<Query> queryList = queryDao.getList((pageIndex - 1) * pageSize, pageSize);
        return queryList;
    }

    @Override
    public long getCount() {
        return queryDao.getCount();
    }
}
