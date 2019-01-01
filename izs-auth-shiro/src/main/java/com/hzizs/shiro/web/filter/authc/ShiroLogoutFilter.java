package com.hzizs.shiro.web.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

public class ShiroLogoutFilter extends LogoutFilter {
	@Override
	protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
		return super.getRedirectUrl(request, response, subject);
	}

}
