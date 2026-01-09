package com.umc9th.areumdap.domain.auth.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


@Component
public class Sha256RefreshTokenHasher implements RefreshTokenHasher {

    private final String secret;

    public Sha256RefreshTokenHasher(
            @Value("${jwt.secret}") String secret
    ) {
        this.secret = secret;
    }

    @Override
    public String hash(String refreshToken) {
        return DigestUtils.sha256Hex(secret + ":" + refreshToken);
    }

    @Override
    public boolean matches(String refreshToken, String hashedToken) {
        String computedHash = hash(refreshToken);
        return MessageDigest.isEqual(
                computedHash.getBytes(StandardCharsets.UTF_8),
                hashedToken.getBytes(StandardCharsets.UTF_8)
        );
    }

}
