package com.testtask.nauka.security;

import com.testtask.nauka.api.auth.UserService;
import com.testtask.nauka.common.utils.PasswordEncoderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    private final UserService userService;
    private final PasswordEncoderProvider encoderProvider;

    @Autowired
    public SecurityConfig(UserService userService, PasswordEncoderProvider encoderProvider) {
        this.userService = userService;
        this.encoderProvider = encoderProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception
    {
        builder.authenticationProvider(authenticationProvider());
    }

    @Configuration
    @Order(5)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        private final JwtFilter jwtFilter;

        public ApiWebSecurityConfigurationAdapter(JwtFilter jwtFilter) {
            this.jwtFilter = jwtFilter;
        }

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .cors().and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                    .requestMatchers().antMatchers("/api/v1/**")
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/v1/auth").authenticated()
                    .antMatchers("/api/v1/auth/**").permitAll()
                    .antMatchers("/api/v1/**").authenticated()
                    .antMatchers(HttpMethod.OPTIONS,"/api/v1/**").permitAll()
                    .antMatchers("/admin/**").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .and()
                    .exceptionHandling().authenticationEntryPoint(authenticationExceptionHandler())
                    .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        }

        private AuthenticationEntryPoint authenticationExceptionHandler() {
            return new AuthenticationExceptionHandler();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

    }



//    @Configuration
//    @Order(4)
//    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        private final CookieAuthenticationSuccessHandler cookieAuthenticationSuccessHandler;
//
//        public FormLoginWebSecurityConfigurerAdapter(CookieAuthenticationSuccessHandler cookieAuthenticationSuccessHandler) {
//            this.cookieAuthenticationSuccessHandler = cookieAuthenticationSuccessHandler;
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .requestMatchers().antMatchers("/admin/**")
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/admin/login").permitAll()
//                    .antMatchers("/admin/**").authenticated()
//                    .and()
//                    .formLogin()
//                        .loginPage("/admin/login")
//                        .successHandler(cookieAuthenticationSuccessHandler);
//        }
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoderProvider.getEncoder());
        return authProvider;
    }
}
