package com.searchengine.dao;

import com.searchengine.domain.PageWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by RayLew on 2015/3/5.
 */
public interface PageWordDao {
    /**
     * 得到正排索引视图列表：通过词id
     *
     * @param wordIdList
     * @return
     */
    List<PageWord> getListByWordIdList(List<Long> wordIdList);

    /**
     * 分页得到索引数据
     *
     * @param offset
     * @param size
     * @return
     */
    List<PageWord> getListBy(@Param("offset") int offset, @Param("size") int size);
}
