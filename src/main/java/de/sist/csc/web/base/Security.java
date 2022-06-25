package de.sist.csc.web.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    private static final int SECONDS_PER_DAY = 60 * 60 * 24;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .rememberMe()
                .alwaysRemember(true)
                .key("csc")
                .tokenValiditySeconds(100 * SECONDS_PER_DAY);
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("kuni")
                        .password("marienloh")
                        .roles("USER")
                        .build(),
                User.builder()
                        .username("hilde")
                        .password("marienloh")
                        .roles("USER")
                        .build()
                , User.builder()
                .username("marianne")
                .password("marienloh")
                .roles("USER")
                .build()
        );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
