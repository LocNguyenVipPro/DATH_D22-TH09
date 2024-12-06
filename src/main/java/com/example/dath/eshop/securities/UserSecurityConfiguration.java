package com.example.dath.eshop.securities;

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

    // Bean cho BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(DetailService detailService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(detailService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    // Cấu hình bảo mật HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(
                                "/products/load-product",
                                "/login",
                                "/main-page",
                                "/users/save",
                                "/users/check-username-unique",
                                "/users/register",
                                "/auth/**",
                                "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/public/**")
                        .permitAll()  // Cho phép tất cả các yêu cầu không cần xác thực

                        .requestMatchers("/cart", "/cart/**")
                        .authenticated()  // Các yêu cầu liên quan đến giỏ hàng phải đăng nhập

                        .requestMatchers("/users/**", "/orders/**")
                        .hasAnyAuthority("Admin", "User")  // Cả Admin và User có quyền truy cập

                        .requestMatchers("/category/**", "/products/**")
                        .hasAuthority("Admin")  // Chỉ Admin mới có quyền truy cập các URL này

                        .anyRequest()
                        .authenticated()  // Các yêu cầu còn lại đều phải xác thực
                )

                // Cấu hình form login
                .formLogin(form -> form
                        .loginPage("/login-form")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/main-page", true)
                        .permitAll()  // Cho phép mọi người truy cập trang login
                )

                // Cấu hình logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login-form")
                        .invalidateHttpSession(true)  // Xóa session khi logout
                        .deleteCookies("JSESSIONID")  // Xóa cookie JSESSIONID
                )

                // Cấu hình RememberMe
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("rememberMe")
                        .tokenValiditySeconds(604800)  // Giữ đăng nhập trong 7 ngày
                        .key("loggedIn")
                )

                // Cấu hình xử lý lỗi truy cập không hợp lệ
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                );

        return security.build();
    }
}
