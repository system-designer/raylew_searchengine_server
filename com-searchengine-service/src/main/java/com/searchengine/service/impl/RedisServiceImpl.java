package com.searchengine.service.impl;

import com.searchengine.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 可缓存任意类型的数据
 */
public class RedisServiceImpl<K,V> implements RedisService<K,V> {

    @Autowired
	private RedisTemplate<K,V> redisTemplate;

	@Override
	public int add(K key, V value, long timeout, TimeUnit timeUnit) {
		int isOk = 0;
		if (key == null){
			return isOk;
		}
		try {

			ValueOperations<K, V> valueOps = redisTemplate
					.opsForValue();
			valueOps.set(key,value,timeout,timeUnit);
			isOk = 1;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

	@Override
	public int add(K key,V value){
		// TODO Auto-generated method stub
		int isOk = 0;
		if (key == null){
			return isOk;
		}
		try {
			ValueOperations<K, V> valueOps = redisTemplate.opsForValue();
			valueOps.set(key, value);
		}catch (Exception e) {
		e.printStackTrace();
		}
		return isOk;
	}

	@Override
	public V get(K key) {
		if (key == null)
			return null;

		V value = null;
		try {
			value= redisTemplate.opsForValue().get(key);
		} catch (RedisConnectionFailureException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public int remove(K key) {
		int isOk = 0;

		if (key == null)
			return isOk;

		try {
			redisTemplate.delete(key);
			isOk = 1;
		} catch (RedisConnectionFailureException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

	@Override
	public int multiAdd(Map<K, V> keyValues) {
		int isOk = 0, total = 0;

		if (keyValues == null)
			return isOk;

		for(K key:keyValues.keySet()){
			try {
				redisTemplate.opsForValue().set(key, keyValues.get(key));
				total++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (total == keyValues.size()){
			isOk = 1;
			return isOk;
		} else {
			return isOk;
		}
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
