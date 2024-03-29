package com.optimagrowth.licenseservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContext userContext = UserContextHolder.getContext();

        userContext.setCorrelationId( httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
        userContext.setUserId( httpServletRequest.getHeader(UserContext.USER_ID));
        userContext.setAuthToken( httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        userContext.setOrganizationId( httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));

        logger.info("UserContextFilter Correlation id: {}", userContext.getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
