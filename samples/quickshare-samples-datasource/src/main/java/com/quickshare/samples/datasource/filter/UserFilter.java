package com.quickshare.samples.datasource.filter;

import com.quickshare.samples.datasource.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author liu_ke
 */
@Component
public class UserFilter implements Filter {

    @Autowired
    private User user;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String customerId = servletRequest.getParameter("customerId");
        user.setCustomerId(customerId);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
