package com.umc9th.areumdap.common.jwt;

import com.umc9th.areumdap.common.exception.GeneralException;
import com.umc9th.areumdap.common.status.ErrorStatus;
import com.umc9th.areumdap.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    private final Key secretKey;
    @Getter
    private final Long accessTokenExpiration;
    @Getter
    private final Long refreshTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // Access Token 생성
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("loginType", user.getOauthProvider().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Access Token 검증
    public void validateAccessToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new GeneralException(ErrorStatus.JWT_TOKEN_NOT_FOUND);
        }
        getClaims(token);
    }

    // Refresh Token 검증
    public Claims validateRefreshToken(String refreshToken) {
        return getClaims(refreshToken);
    }

    // AccessToken에서 userId 추출
    public Long getUserIdFromJwtToken(String token) {
        try {
            return Long.parseLong(
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject()
            );
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.JWT_EXTRACT_ID_FAILED);
        }
    }

    // 내부 Claims 파싱 로직, 만료된 토큰, 서명 불일치 등의 예외 처리
    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            throw new GeneralException(ErrorStatus.JWT_INVALID_SIGNATURE);
        } catch (MalformedJwtException e) {
            throw new GeneralException(ErrorStatus.JWT_MALFORMED);
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new GeneralException(ErrorStatus.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorStatus.JWT_INVALID);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.JWT_GENERAL_ERROR);
        }
    }

}

