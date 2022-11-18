package com.petmily.config;

import com.petmily.config.formatter.BoardTypeFormatter;
import com.petmily.config.formatter.PhoneNumberFormatter;
import com.petmily.config.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new BoardTypeFormatter());
        registry.addFormatter(new PhoneNumberFormatter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**/auth/**");
    }
}
