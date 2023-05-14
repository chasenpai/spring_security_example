package com.securityexample.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthenticationLoggingFilter extends OncePerRequestFilter {

    /**
     * OncePerRequestFilter
     * - 인증 정보 확인, 로깅 등 중복 실행이 되면 안되는 요청에 대해 한번만 실행되도록 보장해준다
     * - HTTP 요청만 지원한다
     * - shouldNotFilter 메소드를 재정의하여 필터가 특정 요청엔 적용 되지 않도록 할 수 있다
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestToken = request.getHeader("request-token");
        log.info("authentication success! {}", requestToken);
        filterChain.doFilter(request, response);
    }

}
