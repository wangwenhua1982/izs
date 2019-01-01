package com.hzizs.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import com.hzizs.cache.redis.util.RedisSessionUtil;

public class RedisSessionDAO extends AbstractSessionDAO {
	private String keyPrefix = "redis_session:";
	// 一天时间
	private static int THREE_HOURS = 3 * 60 * 60;

	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session instanceof ValidatingSession) {
			if (((ValidatingSession) session).isValid()) {
			} else {
				RedisSessionUtil.remove(this.keyPrefix + session.getId());
				return;
			}
		}
		if (session.getTimeout() < 0) {
			RedisSessionUtil.expire(this.keyPrefix + session.getId(),THREE_HOURS);
		} else {
			RedisSessionUtil.expire(this.keyPrefix + session.getId(),(int) (session.getTimeout() / 1000));
		}
	}

	@Override
	public void delete(Session session) {
		RedisSessionUtil.remove(this.keyPrefix + session.getId());

	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<String> keys = RedisSessionUtil.keys(this.keyPrefix + "*");
		Collection<Session> sessions = new HashSet<Session>();
		for (String key : keys) {
			Session session = RedisSessionUtil.get(key);
			sessions.add(session);
		}
		return sessions;
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
	protected Session doReadSession(Serializable sessionId) {
		Session session = RedisSessionUtil.get(this.keyPrefix + sessionId);
		return session;
	}

}
