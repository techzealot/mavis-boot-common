package com.mavis.boot.common;

import com.mavis.boot.common.api.TokenVerifier;
import com.mavis.boot.common.aspect.AccessTokenVerifyInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnWebApplication
public class CommonWebAutoconfiguration {

    /**
     * TokenVerifier类型的bean一定要保证先创建
     */
    @ConditionalOnBean(TokenVerifier.class)
    @Bean
    public AccessTokenVerifyInterceptor accessTokenVerifyInterceptor(TokenVerifier tokenVerifier) {
        return new AccessTokenVerifyInterceptor(tokenVerifier);
    }

}
