package com.umc9th.areumdap.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<?> apiResponse = ApiResponse.error(ErrorStatus.AUTH_UNAUTHORIZED).getBody();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}
