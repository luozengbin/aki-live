package com.appspot.piment.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthFilter implements Filter {

  private static final Logger log = Logger.getLogger(Constants.FQCN + AuthFilter.class.getName());

  @Override
  public void init(FilterConfig config) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

	log.info("---------- doFilter ---------");

	HttpServletRequest httpReq = (HttpServletRequest) req;
	HttpServletResponse httpResp = (HttpServletResponse) resp;

	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();

	if (user == null) {
	  log.info("redirect to login page...");
	  String ru = userService.createLoginURL(httpReq.getRequestURL().toString() + "?" + httpReq.getQueryString());
	  log.info(ru);
	  httpResp.sendRedirect(ru);
	} else{
	  chain.doFilter(req, resp);
	}
  }

  @Override
  public void destroy() {

  }

}
