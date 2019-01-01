package com.hzizs.shiro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {
	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		@SuppressWarnings("rawtypes")
		Cache c = caches.get(name);
		if (c == null) {
			c = new RedisCache<K, V>();
			caches.put(name, c);
		}
		return c;
	}

}
