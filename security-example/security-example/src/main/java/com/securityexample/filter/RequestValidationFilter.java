package com.securityexample.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class RequestValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestToken = httpRequest.getHeader("request-token");

        if(StringUtils.hasText(requestToken)){
           chain.doFilter(request, response);
        }else{
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

}
