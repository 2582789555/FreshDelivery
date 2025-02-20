package org.example.freshdeliveryserver.config;

import org.example.freshdeliveryserver.auth.AppAccessDenyHandler;
import org.example.freshdeliveryserver.auth.AppAuthenticationEntryPoint;
import org.example.freshdeliveryserver.filter.JwtCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private JwtCheckFilter jwtCheckFilter;
    @Resource
    private AppAccessDenyHandler appAccessDenyHandler;
    @Resource
    private AppAuthenticationEntryPoint authenticationEntryPoint;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 禁用CSRF防护
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeRequests()
                    .antMatchers("/user/auth/**").permitAll()  // 允许“认证”相关接口公开访问
                    .anyRequest().authenticated()          // 其他所有请求都需要认证,认证失败401，权限不足403
                .and()
                .addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class) // 确保在其他过滤器之前执行JWT校验。
                .exceptionHandling()
                .accessDeniedHandler(appAccessDenyHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement((sessionManagementConfigurer)->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
