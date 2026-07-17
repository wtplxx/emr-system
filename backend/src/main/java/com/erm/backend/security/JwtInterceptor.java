package com.erm.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.erm.backend.VO.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String uri = request.getRequestURI();
        if (uri.contains("login")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            sendUnauthorized(response, "未授权");
            return false;
        }
        token = token.substring(7);
        if (!JwtUtil.validateToken(token)) {
            sendUnauthorized(response, "Token已过期或无效");
            return false;
        }
        request.setAttribute("userId", JwtUtil.getUserIdFromToken(token));
        request.setAttribute("role", JwtUtil.getRoleFromToken(token));
        return true;
    }

    private void sendUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResultVO result = new ResultVO();
        result.setCode(-401);
        result.setData(msg);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
    }
}
