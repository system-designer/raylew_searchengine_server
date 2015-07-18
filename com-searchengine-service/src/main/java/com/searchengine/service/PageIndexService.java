package com.searchengine.service;

import com.searchengine.domain.vo.PageIndexVO;
import com.searchengine.domain.PageIndex;
import org.springframework.stereotype.Service;

@Service
public interface PageIndexService {
    /**
     * 添加新的网页索引
     * @param pageIndex
     * @return
     */
    long add(PageIndexVO pageIndex);

    PageIndexVO getById(long id);
}
