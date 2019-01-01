package com.hzizs.spring.security.core.userdetails.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hzizs.entity.IAuth;
import com.hzizs.entity.IUser;
import com.hzizs.service.IAuthService;
import com.hzizs.service.IUserService;
import com.hzizs.spring.security.core.userdetails.User;

/**
 * 用户 管理类
 * 
 * @author crazy_cabbage
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	private String platform;
	// ===================spring===============
	private IUserService userService;
	private IAuthService authService;

	public final void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public final void setAuthService(IAuthService authService) {
		this.authService = authService;
	}
	public final void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1.查找用户
		IUser user = userService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		List<IAuth> auths = authService.findByUsername(username,platform);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (IAuth auth : auths) {
			grantedAuthorities.add(new SimpleGrantedAuthority(String.valueOf(auth.getAuthCode())));
		}
		boolean enables = user.isCanUse();
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		User securityUser = new User(user.getUsername(), user.getPassword(), enables, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
		securityUser.setUserId(user.getUserId());
		return securityUser;
	}

}
