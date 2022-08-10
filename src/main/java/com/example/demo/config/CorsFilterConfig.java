package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Slf4j
@Configuration
public class CorsFilterConfig {

    @Bean
    public FilterRegistrationBean customCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.FALSE);
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.addAllowedHeader("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);


        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(
                Ordered.HIGHEST_PRECEDENCE); // 필터들중 가장먼저 필터되게 설정됨. (int의 최소값) 다른필터가 먼저 적용이 되어야 하면 숫자를 좀 더 높게 설정하면 됨.
        return bean;
    }
}
