package com.searchengine.service.impl;

import com.searchengine.dao.PageIndexDao;
import com.searchengine.domain.PageIndex;
import com.searchengine.domain.vo.PageIndexVO;
import com.searchengine.service.PageIndexService;
import com.searchengine.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("pageIndexService")
public class PageIndexServiceImpl extends ServiceBase implements PageIndexService {

    @Autowired
    private PageIndexDao pageIndexDao;

    @Override
    public long add(PageIndexVO pageIndexVO){
        long id = GenerateId();
        PageIndex pageIndex = BeanUtils.copyProperties(pageIndexVO, PageIndex.class);
        pageIndex.setId(id);
        Date nowDate = new Date();
        pageIndex.setCreatedTime(nowDate);
        pageIndex.setLastUpdatedTime(nowDate);
        int ret=pageIndexDao.add(pageIndex);
        if(ret>0){
            return id;
        }
        return 0;
    }

    @Override
    public PageIndexVO getById(long id){
        PageIndex pageIndex = pageIndexDao.getById(id);
        if (pageIndex == null) {
            return null;
        }

        PageIndexVO pageIndexVO = BeanUtils.copyProperties(pageIndex, PageIndexVO.class);
        return pageIndexVO;
    }
}
