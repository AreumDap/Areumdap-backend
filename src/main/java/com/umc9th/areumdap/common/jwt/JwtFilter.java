package com.umc9th.areumdap.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc9th.areumdap.common.base.BaseStatus;
import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.response.ApiResponse;
import com.umc9th.areumdap.common.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String accessToken = resolveToken(request);
            if (accessToken != null) {
                jwtService.validateAccessToken(accessToken);
                Long userId = jwtService.getUserIdFromJwtToken(accessToken);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (GeneralException e) {
            throw new AuthenticationCredentialsNotFoundException(
                    e.getErrorStatus().getMessage());
        }
    }

    // Authorization Header에서 JWT 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 내부 시스템 예외 처리
    private void handleJwtError(String msg, HttpServletResponse response) throws IOException {
        log.error("[*] Internal Exception in JWT Filter → {}", msg);
        ErrorStatus errorStatus = ErrorStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<?> errorResponse =
                ApiResponse.error(errorStatus, msg != null ? msg : errorStatus.getMessage()).getBody();
        setHttpServletResponse(errorStatus.getHttpStatus().value(), errorResponse, response);
    }

    // JWT 관련 커스텀 예외 처리
    private void handleGeneralJwtError(BaseStatus errorStatus, HttpServletResponse response) throws IOException {
        log.error("[*] GeneralException in JWT Filter → {}", errorStatus.getMessage());
        ApiResponse<?> errorResponse = ApiResponse.error(errorStatus).getBody();
        setHttpServletResponse(errorStatus.getHttpStatus().value(), errorResponse, response);
    }

    /// Http 응답 JSON 포맷 설정
    private void setHttpServletResponse(
            int status,
            ApiResponse<?> errorResponse,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }

}
