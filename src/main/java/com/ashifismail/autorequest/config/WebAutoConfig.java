package com.ashifismail.autorequest.config;

import com.ashifismail.autorequest.web.RequestContentArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Ashif Ismail
 * @email ashifismail.ae@gmail.com
 */

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class WebAutoConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebAutoConfig.class);

    @Bean
    public RequestContentArgumentResolver requestContentArgumentResolver() {
        logger.info("Auto Request resolver bean created");
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
        logger.info("Auto Request resolver added to resolvers");
    }
}
