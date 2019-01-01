package com.hzizs.spring.security.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hzizs.ActionContext;
import com.hzizs.constants.CommonConstants;
import com.hzizs.spring.security.core.userdetails.User;
import com.hzizs.web.util.RequestUtil;

public class ActionContextSpringSecurityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Map<String, Object> map = new HashMap<String, Object>();
		SecurityContext sc = SecurityContextHolder.getContext();
		if (sc != null) {
			Authentication authentication = sc.getAuthentication();
			if (authentication != null) {
				Object object = authentication.getPrincipal();
				if (object instanceof User) {
					User user = (User) object;
					if (user != null) {
						map.put(CommonConstants.USER_ID, user.getUserId());
						map.put(CommonConstants.USER_NAME, user.getUsername());
					}
				}
			}
		}
		ActionContext.set(map);
		ActionContext.setRemoteIp(RequestUtil.getRemoteAddr((HttpServletRequest) request));
		chain.doFilter(request, response);
		ActionContext.remove();
	}

	@Override
	public void destroy() {
	}

}
