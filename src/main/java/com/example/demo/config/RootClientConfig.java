package com.example.demo.config;

import com.example.demo.external.client.RootClientProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RootClientConfig {

    private final Integer MAX_THREAD = 5;
    private final Integer TIMEOUT_SECOND = 500;
    private static final String SEARCH_NAVER_URL = "https://search.naver.com/search.naver?nso=&where=blog&query=";

    @Bean
    public RootClientProxy munHoYoungRootClientProxy() {
        return rootClientProxy("munhoyoung", "http://visit.munhoyoung.com/blog_visit");
    }

    @Bean
    public RootClientProxy naverRootClientProxy() {
        return rootClientProxy("naver", SEARCH_NAVER_URL);
    }

    private RootClientProxy rootClientProxy(String serverName, String domain) {
        return new RootClientProxy(serverName, domain, TIMEOUT_SECOND, MAX_THREAD);
    }
}
