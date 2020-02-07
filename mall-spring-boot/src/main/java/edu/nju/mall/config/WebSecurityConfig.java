package edu.nju.mall.config;

import edu.nju.mall.security.JwtAuthenticationTokenFilter;
import edu.nju.mall.security.OptionsRequestFilter;
import edu.nju.mall.security.WechatMicroProgramAuthenticationFilter;
import edu.nju.mall.security.WechatMicroProgramAuthenticationManager;
import edu.nju.mall.security.handler.CustomAccessDeniedHandler;
import edu.nju.mall.security.handler.CustomAuthenticationEntryPoint;
import edu.nju.mall.security.handler.CustomAuthenticationSuccessHandler;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/error/**").permitAll()
//                .antMatchers("/api/auth").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors()
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                        new Header("Access-control-Allow-Origin","*"),
                        new Header("Access-Control-Expose-Headers","Authorization"))))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
        http.addFilterAt(wechatMicroProgramAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter(), WechatMicroProgramAuthenticationFilter.class)
                .addFilterBefore(new OptionsRequestFilter(), JwtAuthenticationTokenFilter.class);
    }

    @Autowired
    private WechatMicroProgramAuthenticationManager wechatMicroProgramAuthenticationManager;

    @Bean
    public WechatMicroProgramAuthenticationFilter wechatMicroProgramAuthenticationFilter() {
        WechatMicroProgramAuthenticationFilter wechatMicroProgramAuthenticationFilter = new WechatMicroProgramAuthenticationFilter("/api/auth");
        wechatMicroProgramAuthenticationFilter.setAuthenticationManager(wechatMicroProgramAuthenticationManager);
        wechatMicroProgramAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());
        return wechatMicroProgramAuthenticationFilter;
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD", "OPTION"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
