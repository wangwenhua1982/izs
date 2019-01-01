package com.hzizs.shiro.spring;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hzizs.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {

	@Override
	public void afterPropertiesSet() throws IOException, TemplateException {
		super.afterPropertiesSet();
		this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
	}

}
