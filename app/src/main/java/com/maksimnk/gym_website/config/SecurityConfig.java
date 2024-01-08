package com.maksimnk.gym_website.config;

import com.maksimnk.gym_website.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsServiceImpl userDetailsServicel;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsServicel) {
        this.userDetailsServicel = userDetailsServicel;
    }


    protected void configure(AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService(userDetailsServicel).passwordEncoder(getPasswordEncoder());

    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/auth/login", "/auth/registration").permitAll()
                .antMatchers("/css/landing.css", "/css/loginPage.css", "/js/bootstrap.js", "/css/styles.css", "/img/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("admin")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/process_login")
                .defaultSuccessUrl("/home") // для всех ролей, кроме admin
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .and()
                .csrf().disable();
    }
}
