package com.example.locnguyen.techzone.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserSecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(DetailService deltailSerVice) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(deltailSerVice);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(
                                "/products/load-product",
                                "/login",
                                "/main-page",
                                "/users/save",
                                "/users/check-username-unique",
                                "/users/register",
                                "/auth/**")
                        .permitAll()
                        .requestMatchers("/cart")
                        .permitAll()
                        .requestMatchers("cart/**")
                        .authenticated()
                        .requestMatchers("/users/**")
                        .hasAnyAuthority("Admin", "User")
                        .requestMatchers("/orders/**")
                        .hasAnyAuthority("Admin", "User")
                        .requestMatchers("/category/**")
                        .hasAnyAuthority("Admin")
                        .requestMatchers("/products/**")
                        .hasAnyAuthority("Admin")
                        .requestMatchers("/wishlist/**")
                        .permitAll()
                        .requestMatchers(
                                "/resources/**", "/static/**", "/css/**", "/javascripts/**", "/images/**", "/public/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form.loginPage("/login-form")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/main-page", true)
                        .permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login-form")
                        .invalidateHttpSession(true) // Invalidates the HttpSession
                        .deleteCookies("JSESSIONID"))
                .rememberMe(rememberme -> rememberme
                        .rememberMeParameter("rememberMe")
                        .tokenValiditySeconds(604800)
                        .key("loggedIn"))
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/access-denied"));
        ;
        return security.build();
    }
}
