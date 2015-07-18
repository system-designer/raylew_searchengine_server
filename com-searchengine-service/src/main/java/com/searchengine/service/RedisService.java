package com.searchengine.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright （c）2015 RayLew
 * @author 刘雷
 * @version 1.0 alpha
 * @since 2015-02-01
 */
public interface RedisService<K,V> {

	/**
	 *
	 * @param key
	 * @param value
	 * @param timeout
	 * @param timeUnit
	 * @return
	 */
	int add(K key, V value, long timeout, TimeUnit timeUnit);

	/**
	 * 添加一条记录在Redis数据库中
	 * @param key K 一条记录的key
	 * 		  value V 一条记录的value
	 * @return <code>1</code> 执行成功
	 *		   <code>0</code> 执行失败
	 */
	int add(K key,V value);
	
	/**
	 * 从Redis数据库中获取一条记录为key的value值
	 * @param key the <code>K</code> 要获取记录的key值
	 * @return <code>K</code> 执行成功
	 *		   <code>null</code> 执行失败
	 */
	V get(K key);
	
	/**
	 * 删除一条记录在Redis数据库中
	 * @param key K 要删除记录的key
	 * @return <code>1</code> 执行成功
	 *		   <code>0</code> 执行失败
	 */
	int remove(K key);

	/**
	 * 批量添加记录到Redis数据库中
	 * @param keyValues 要添加的数据集合
	 * @return <code>1</code> 执行成功
	 *		   <code>0</code> 执行失败
	 */
	int multiAdd(Map<K, V> keyValues);

}
