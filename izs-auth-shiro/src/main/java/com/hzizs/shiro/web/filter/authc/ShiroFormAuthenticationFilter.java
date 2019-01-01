package com.hzizs.shiro.web.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		return super.onLoginFailure(token, e, request, response);
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		return super.executeLogin(request, response);
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		super.issueSuccessRedirect(request, response);
	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean isAllowed = isAccessAllowed(request, response, mappedValue);
		if (isAllowed) {
			if (isLoginRequest(request, response)) {
				issueSuccessRedirect(request, response);
				return false;
			}
		}
		return isAllowed || onAccessDenied(request, response, mappedValue);
	}

	@Override
	protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
		return super.isLoginRequest(request, response);
	}

}
