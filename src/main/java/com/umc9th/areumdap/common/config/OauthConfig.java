package com.umc9th.areumdap.common.config;

import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import com.umc9th.areumdap.domain.oauth.properties.OAuthNaverProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OAuthKakaoProperties.class,OAuthNaverProperties.class})
public class OauthConfig {
}
