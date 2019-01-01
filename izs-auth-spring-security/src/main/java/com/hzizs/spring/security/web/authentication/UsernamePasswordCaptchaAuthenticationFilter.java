package com.hzizs.spring.security.web.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import com.hzizs.util.prefs.Md5Util;
import com.hzizs.constants.CommonConstants;
import com.hzizs.entity.IUser;
import com.hzizs.service.IUserService;

public class UsernamePasswordCaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static String SPRING_SECURITY_FORM_CAPTCHA_KEY = "j_captcha";
	private String captchaParameter = SPRING_SECURITY_FORM_CAPTCHA_KEY;
	private boolean captchaFlag = false;

	private IUserService userService;

	/**
	 * @param userService
	 *            the userService to set
	 */
	public final void setUserService(IUserService userService) {
		this.userService = userService;
	}

	protected String obtainCaptcha(HttpServletRequest request) {
		return request.getParameter(captchaParameter);
	}

	public final String getCaptchaParameter() {
		return captchaParameter;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (captchaFlag) {
			String captcha = obtainCaptcha(request);
			HttpSession session = request.getSession();
			String sessionCaptcha = (String) session.getAttribute(CommonConstants.CAPTCHA);
			session.removeAttribute(CommonConstants.CAPTCHA);
			if (!captcha.equals(sessionCaptcha)) {
				throw new AuthenticationServiceException("验证码错误");
			}

		}
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		IUser user = userService.findByUsername(username);
		if (user == null) {
			throw new AuthenticationServiceException("用户不存在");
		}
		if (!user.getPassword().equals(Md5Util.saltPassword(username, password))) {
			throw new AuthenticationServiceException("密码错误");
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, user.getPassword());
		setDetails(request, authRequest);
		return getAuthenticationManager().authenticate(authRequest);
	}

	public void setCaptchaParameter(String captchaParameter) {
		Assert.hasText(captchaParameter, "Captcha parameter must not be empty or null");
		this.captchaParameter = captchaParameter;
	}

	public final void setCaptchaFlag(boolean captchaFlag) {
		this.captchaFlag = captchaFlag;
	}

}
