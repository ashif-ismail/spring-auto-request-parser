package com.ashifismail.autorequest.config;

import com.ashifismail.autorequest.web.RequestContentArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Ashif Ismail
 * @email ashifismail.ae@gmail.com
 */
@Configuration
public class WebAutoConfig implements WebMvcConfigurer {

    @Bean
    public RequestContentArgumentResolver requestContentArgumentResolver() {
        return new RequestContentArgumentResolver(objectMapper());
    }

    @Bean
    @Qualifier("spring-auto-request")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestContentArgumentResolver());
    }
}
