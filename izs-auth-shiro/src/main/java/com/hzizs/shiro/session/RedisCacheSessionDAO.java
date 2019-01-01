package com.hzizs.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import com.hzizs.cache.redis.util.RedisSessionUtil;

public class RedisCacheSessionDAO extends EnterpriseCacheSessionDAO {

	private String keyPrefix = "redis_session:";
	// 3小时
	private static int THREE_HOURS = 3 * 60 * 60;

	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		Session session = getCachedSession(sessionId);
		if (session == null) {
			session = this.doReadSession(sessionId);
			if (session == null) {
				throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
			} else {
				// 缓存
				cache(session, session.getId());
			}
		}
		return session;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = RedisSessionUtil.get(this.keyPrefix + sessionId);
		return session;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		assignSessionId(session, sessionId);
		if (session.getTimeout() < 0) {
			RedisSessionUtil.put(this.keyPrefix + session.getId(), session, THREE_HOURS);
		} else {
			RedisSessionUtil.put(this.keyPrefix + session.getId(), session, (int) (session.getTimeout() / 1000));
		}
		return sessionId;
	}

	@Override
	protected void doUpdate(Session session) {
		if (session instanceof ValidatingSession) {
			if (((ValidatingSession) session).isValid()) {
			} else {
				RedisSessionUtil.remove(this.keyPrefix + session.getId());
				return;
			}
		}
		if (session.getTimeout() < 0) {
			RedisSessionUtil.expire(this.keyPrefix + session.getId(), THREE_HOURS);
		} else {
			RedisSessionUtil.expire(this.keyPrefix + session.getId(), (int) (session.getTimeout() / 1000));
		}
	}

	@Override
	protected void doDelete(Session session) {
		RedisSessionUtil.remove(this.keyPrefix + session.getId());
	}

}
