package com.example.ims.config;

import com.example.ims.util.RateLimiter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitFilter implements Filter {

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().startsWith("/signals")) {
            if (!rateLimiter.tryAcquire()) {
                res.setStatus(429); // Too Many Requests
                res.getWriter().write("Too Many Requests");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
}
