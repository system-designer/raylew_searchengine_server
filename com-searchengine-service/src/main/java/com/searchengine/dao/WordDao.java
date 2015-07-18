package com.searchengine.dao;

import com.searchengine.domain.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by RayLew on 2015/2/4.
 * QQ:897929321
 */
public interface WordDao {
    /**
     * 添加词语
     *
     * @param word
     * @return
     */
    int add(Word word);

    Word getById(long id);

    /**
     * 得到所有词语
     *
     * @return
     */
    List<Word> getList();

    /**
     * 分页得到词语
     *
     * @param offset
     * @param size
     * @return
     */
    List<Word> getListBy(@Param("offset") int offset, @Param("size") int size);

    /**
     * 得到词语总数
     *
     * @return
     */
    Long getCount();

    /**
     * 更新词语
     *
     * @param word
     * @return
     */
    int update(Word word);

    /**
     * 删除词语
     *
     * @param id
     * @return
     */
    int deleteById(long id);
}
