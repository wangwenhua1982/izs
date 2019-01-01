package com.hzizs.shiro.realm;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.hzizs.entity.IUser;
import com.hzizs.service.IUserService;
import com.hzizs.util.StringUtil;

public class SimpleShiroAuthorizingRealm extends AuthorizingRealm {
	private IUserService loginService;

	public final void setLoginService(IUserService loginService) {
		this.loginService = loginService;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.fromRealm(getName()).iterator().next();
		if (StringUtil.isNotEmpty(username)) {
			SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
			simpleAuthorizationInfo.addRole("admin");
			return simpleAuthorizationInfo;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String username = token.getUsername();
		if (StringUtil.isEmpty(username)) {
			throw new AccountException("用户名不 存在");
		}
		IUser user = loginService.findByUsername(username);
		if (user == null) {
			throw new AccountException("用户名不 存在");
		}
		char[] password = token.getPassword();
		if (!user.getPassword().equals(new String(password))) {
			throw new AccountException("密码错误");
		}
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
		return simpleAuthenticationInfo;
	}

}
