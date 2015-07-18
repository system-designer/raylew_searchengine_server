package com.searchengine.dao;

import com.searchengine.domain.PageIndex;
import com.searchengine.domain.vo.PageIndexVO;

/**
 * Created by RayLew on 2015/2/4.
 */
public interface PageIndexDao {
    /**
     * 添加页面索引
     * @param pageIndex
     * @return
     */
    int add(PageIndex pageIndex);

    PageIndex getById(long pageIndex);

    PageIndex getByUrl(String url);

    int update(PageIndex pageIndex);

    int deleteById(PageIndex pageIndex);
}
