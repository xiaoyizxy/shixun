package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有域名进行跨域调用
        config.addAllowedOriginPattern("*");

        // 允许跨域发送cookie
        config.setAllowCredentials(true);

        // 允许全部请求方法
        config.addAllowedMethod("*");

        // 允许全部请求头
        config.addAllowedHeader("*");

        // 暴露哪些头部信息
        config.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
