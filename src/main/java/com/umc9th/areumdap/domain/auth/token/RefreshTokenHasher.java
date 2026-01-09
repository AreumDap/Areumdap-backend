package com.umc9th.areumdap.domain.auth.token;

public interface RefreshTokenHasher {
    String hash(String refreshToken);
    boolean matches(String refreshToken, String hashedToken);
}
