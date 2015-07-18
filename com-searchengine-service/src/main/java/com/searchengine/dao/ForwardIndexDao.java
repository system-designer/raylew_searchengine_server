package com.searchengine.dao;

import com.searchengine.domain.ForwardIndex;
import com.searchengine.domain.InvertedIndex;
import com.searchengine.domain.WordDF;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by RayLew on 2015/2/4.
 */
public interface ForwardIndexDao {
    /**
     * 添加正排索引
     *
     * @param forwardIndex
     * @return
     */
    int add(ForwardIndex forwardIndex);

    ForwardIndex getById(long id);

    /**
     * 分页得到正排索引列表
     *
     * @return
     */
    List<ForwardIndex> getList(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 得到document frequency
     *
     * @return
     */
    List<WordDF> getWordDFList();

    /**
     * 得到正排索引列表：通过词id
     *
     * @param wordIdList
     * @return
     */
    List<ForwardIndex> getListByWordIdList(List<Long> wordIdList);

    /**
     * 得到已经建立了索引的网页
     *
     * @return
     */
    List<Long> getDistinctPidList();

    int update(ForwardIndex forwardIndex);

    int deleteById(long id);

    /**
     * 通过wordid得到所有倒排索引
     *
     * @return
     */
    List<InvertedIndex> getInvertedIndexList(@Param("wid") Long wid);
}
