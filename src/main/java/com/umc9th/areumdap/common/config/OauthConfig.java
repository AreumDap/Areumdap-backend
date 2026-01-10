package com.umc9th.areumdap.common.config;

import com.umc9th.areumdap.domain.oauth.properties.OAuthKakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OAuthKakaoProperties.class)
public class OauthConfig {
}
