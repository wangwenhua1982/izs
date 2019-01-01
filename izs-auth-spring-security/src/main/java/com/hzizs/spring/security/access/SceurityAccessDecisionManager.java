package com.hzizs.spring.security.access;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 安全 访问管理
 * 
 * @author crazy_cabbage
 */
public class SceurityAccessDecisionManager implements AccessDecisionManager {

	private boolean flag;

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		if (flag) {
			return;
		}
		if (configAttributes == null) {
			return;
		}
		for (ConfigAttribute configAttribute : configAttributes) {
			String permission = configAttribute.getAttribute();

			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (permission.equals(ga.getAuthority())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("no permission");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
