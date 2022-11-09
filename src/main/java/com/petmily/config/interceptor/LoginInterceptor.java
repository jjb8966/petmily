package com.petmily.config.interceptor;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        String requestURI = request.getRequestURI();

        if (session == null || session.getAttribute(SessionConstant.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자");
            log.info("requestURI = {}", requestURI);

            response.sendRedirect("/login?redirectURL=" + requestURI);

            return false;
        }

        log.info("인증 사용");

        return true;
    }

}
