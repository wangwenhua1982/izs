package com.hzizs.spring.security.web.access.intercept;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.hzizs.entity.IAuth;
import com.hzizs.service.IAuthService;

/**
 * 判断 url 是否在权限管理中
 * 
 * @author crazy_cabbage
 */
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource  {

	// ========================spring 注入====================
	private IAuthService authService;
    private String platform;
    private String loginUrl;
	public void setAuthService(IAuthService authService) {
		this.authService = authService;
	}
	public final void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	private Map<RequestMatcher, Collection<ConfigAttribute>> authsMap;

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		//忽略启动时的测试
		if(loginUrl!=null&&request.getRequestURI().contains(loginUrl)){
			return null;
		}
		if (authsMap == null) {
			// 线程不安全
			reloadAuthMap();
		}
		for (RequestMatcher requestMatcher : authsMap.keySet()) {
			if (requestMatcher.matches(request)) {
				return authsMap.get(requestMatcher);
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}


	// 重新载入
	private void reloadAuthMap() {
		List<IAuth> auths = authService.findByPlatform(platform);
		authsMap = new HashMap<RequestMatcher, Collection<ConfigAttribute>>();
		for (IAuth auth : auths) {
			List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			ConfigAttribute configAttribute = new SecurityConfig(auth.getAuthCode());
			configAttributes.add(configAttribute);
			authsMap.put(new AntPathRequestMatcher(auth.getAuthUrl()), configAttributes);
		}
	}
 
}
