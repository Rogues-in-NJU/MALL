package edu.nju.mall.config;

import edu.nju.mall.filter.OptionsRequestFilter;
import edu.nju.mall.filter.WechatAuthFiler;
import edu.nju.mall.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOrigins("*");
            }
        };

    }

    @Bean
    public FilterRegistrationBean buildOptionsRequestFilter() {
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new OptionsRequestFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        //order的数值越小 则优先级越高
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean buildWechatAuthFilter() {
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WechatAuthFiler(jwtUtils));
        filterRegistrationBean.addUrlPatterns("/wechat/api/*");
        //order的数值越小 则优先级越高
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }

}
