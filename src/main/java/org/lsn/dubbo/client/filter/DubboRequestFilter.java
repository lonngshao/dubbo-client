package org.lsn.dubbo.client.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lsn.dubbo.client.DubboContext;
import org.springframework.web.filter.OncePerRequestFilter;

public class DubboRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String appId = request.getHeader(DubboContext.APP_ID);
		String appSecret = request.getHeader(DubboContext.APP_SECRET);
		Map<String , Object>  appInfo = new HashMap<>();
		appInfo.put(DubboContext.APP_ID, appId);
		appInfo.put(DubboContext.APP_SECRET, appSecret);
		DubboContext.APPINFO.set(appInfo);
		filterChain.doFilter(request, response);
	}

}
