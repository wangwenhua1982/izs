package com.hzizs.shiro.web.filter.authc;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;

public class ShiroUserFilter extends UserFilter {
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		super.redirectToLogin(request, response);
	}

}
