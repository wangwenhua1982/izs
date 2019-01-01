package com.hzizs.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;

import com.hzizs.cache.redis.util.RedisExUtil;
import com.hzizs.cache.redis.util.RedisSessionUtil;

public class RedisCache<K, V> implements Cache<K, V> {
	private String keyPrefix = "redis_session:";
	// 3小时
	private static int THREE_HOURS = 3 * 60 * 60;

	@Override
	public V get(K key) throws CacheException {
		return RedisSessionUtil.get(this.keyPrefix + key);
	}

	@Override
	public V put(K key, V value) throws CacheException {
		RedisSessionUtil.put(this.keyPrefix + key, value, THREE_HOURS);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
		V v = RedisExUtil.get(this.keyPrefix + key);
		RedisSessionUtil.remove(this.keyPrefix + key);
		return v;
	}

	@Override
	public void clear() throws CacheException {

	}

	@Override
	public int size() {
		return Integer.MAX_VALUE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keys() {
		Set<String> keys = RedisSessionUtil.keys(this.keyPrefix + "*");
		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		} else {
			return (Set<K>) keys;
		}
	}

	@Override
	public Collection<V> values() {
		Set<String> keys = RedisSessionUtil.keys(this.keyPrefix + "*");
		if (!CollectionUtils.isEmpty(keys)) {
			List<V> values = new ArrayList<V>(keys.size());
			for (String key : keys) {
				V value = RedisExUtil.get(key);
				if (value != null) {
					values.add(value);
				}
			}
			return Collections.unmodifiableList(values);
		} else {
			return Collections.emptyList();
		}
	}

}
