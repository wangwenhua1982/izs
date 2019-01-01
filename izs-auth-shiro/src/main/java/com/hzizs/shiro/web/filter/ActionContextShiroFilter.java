package com.hzizs.shiro.web.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.hzizs.web.util.RequestUtil;
import com.hzizs.ActionContext;

public class ActionContextShiroFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Subject user = SecurityUtils.getSubject();
		Session session = user.getSession();
		Map<String, Object> map = new HashMap<String, Object>();
		Collection<Object> objects = session.getAttributeKeys();
		for (Iterator<Object> iterator = objects.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			map.put(object.toString(), session.getAttribute(object));
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
