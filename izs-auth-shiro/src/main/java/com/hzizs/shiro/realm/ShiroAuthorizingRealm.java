package com.hzizs.shiro.realm;

import java.util.List;

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

import com.hzizs.entity.IAuth;
import com.hzizs.entity.IRole;
import com.hzizs.entity.IUser;
import com.hzizs.service.IAuthService;
import com.hzizs.service.IRoleService;
import com.hzizs.service.IUserService;
import com.hzizs.util.StringUtil;

public class ShiroAuthorizingRealm extends AuthorizingRealm {
	private IUserService loginService;
	private IAuthService loginAuthService;
	private IRoleService loginRoleService;
    private String platform;
	public final void setLoginService(IUserService loginService) {
		this.loginService = loginService;
	}

	public final void setLoginAuthService(IAuthService loginAuthService) {
		this.loginAuthService = loginAuthService;
	}

	public final void setLoginRoleService(IRoleService loginRoleService) {
		this.loginRoleService = loginRoleService;
	}

	public final void setPlatform(String platform) {
		this.platform = platform;
	}

	// ==================================业务方法=========================================
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.fromRealm(getName()).iterator().next();
		if (StringUtil.isNotEmpty(username)) {
			IUser user = loginService.findByUsername(username);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<IRole> roles = loginRoleService.findByUserId(user.getUserId());
			if (roles != null) {
				for (IRole role : roles) {
					info.addRole(role.getRoleCode());
					List<IAuth> auths = loginAuthService.findByRoleId(role.getRoleId(),platform);
					if (auths != null) {
						for (IAuth auth : auths) {
							info.addStringPermission(auth.getAuthCode());
						}
					}
				}
			}
			return info;
		}
		return null;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
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
