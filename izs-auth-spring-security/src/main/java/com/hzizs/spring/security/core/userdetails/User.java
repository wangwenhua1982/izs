package com.hzizs.spring.security.core.userdetails;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class User extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3444488820042182119L;
	/**
	 * 用户主键
	 */
	private Serializable userId;

	public final Serializable getUserId() {
		return userId;
	}

	public final void setUserId(Serializable userId) {
		this.userId = userId;
	}

	public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

}
