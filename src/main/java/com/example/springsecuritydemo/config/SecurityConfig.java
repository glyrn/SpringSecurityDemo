package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.filter.JwtAuthenticationTokenFilter;
import com.example.springsecuritydemo.handler.AccessDeniedHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    // 自定义异常处理器 认证异常
    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    // 自定义异常处理器 授权异常
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    // 自定义认证成功处理器
    @Autowired
    AuthenticationSuccessHandler successHandler;

    //自定义失败处理器
    @Autowired
    AuthenticationFailureHandler failureHandler;

    // 自定义登出处理器
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;


    /**
     *我们只需要使用把BCryptPasswordEncoder对象注入Spring容器中，SpringSecurity就会使用该PasswordEncoder来进行密码校验。
     *
     *我们可以定义一个SpringSecurity的配置类，SpringSecurity要求这个配置类要继承WebSecurityConfigurerAdapter。
     * ————————————————
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                // 不通过session获取securityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许匿名访问
                .antMatchers("user/login").anonymous()
                // 除了上面的接口以外，其余的都需要认证
                .anyRequest().authenticated();

        // 加入自定义的token校验器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 加入自定义异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);
        // 允许跨域
        http.cors();


        http.formLogin()
                // 自定义认证成功处理器
                .successHandler(successHandler).
                // 自定义失败处理器
                failureHandler(failureHandler);
        http.logout()
                // 自定义登出处理器
                .logoutSuccessHandler(logoutSuccessHandler);

        http.authorizeRequests().anyRequest().authenticated();


    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManagerBean();
    }
}
