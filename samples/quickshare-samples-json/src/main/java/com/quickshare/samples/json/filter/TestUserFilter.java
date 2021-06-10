package com.quickshare.samples.json.filter;

import com.quickshare.samples.json.accord.TestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author liu_ke
 */
@Component
public class TestUserFilter implements Filter {

    @Autowired
    private TestUser testUser;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String code = servletRequest.getParameter("code");
        testUser.setCode(code);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
