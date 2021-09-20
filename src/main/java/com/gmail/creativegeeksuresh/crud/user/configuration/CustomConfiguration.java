package com.gmail.creativegeeksuresh.crud.user.configuration;

import com.gmail.creativegeeksuresh.crud.user.security.CustomAccessDeniedHandler;
import com.gmail.creativegeeksuresh.crud.user.security.CustomAuthSuccessHandler;
import com.gmail.creativegeeksuresh.crud.user.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class CustomConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  CustomUserDetailsService customUserDetailService;

  @Autowired
  CustomAccessDeniedHandler accessDeniedHandler;

  @Autowired
  CustomAuthSuccessHandler authSuccessHandler;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/resources/**", "/static/**", "/templates/**", "/css/**", "/js/**", "/images/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // http.authorizeRequests().antMatchers("/admin/**",
    // "/api/v1/admin/**").hasRole(AppConstants.ADMIN_ROLE_STRING)
    // .antMatchers("/user/**", "/api/v1/user/**", "/api/v1/get-all-books")
    // .hasAnyRole(AppConstants.USER_ROLE_STRING, AppConstants.ADMIN_ROLE_STRING)
    // .antMatchers("/", "/login", "/create-account",
    // "/api/v1/create-account","/error/**").permitAll().anyRequest()
    // .authenticated().and().csrf().disable().formLogin().loginPage("/login").permitAll()
    // .usernameParameter("userName").passwordParameter("password").successHandler(authSuccessHandler)
    // .failureUrl("/login?accessdenied").and().logout().invalidateHttpSession(true)
    // .deleteCookies("JSESSIONID").permitAll().and().exceptionHandling()
    // .accessDeniedHandler(accessDeniedHandler);

    http.authorizeRequests().antMatchers("/api/v1/user/**").permitAll().anyRequest().authenticated().and().csrf()
        .disable().formLogin().loginPage("/login").permitAll().usernameParameter("userName")
        .passwordParameter("password").successHandler(authSuccessHandler).failureUrl("/login?accessdenied").and()
        .logout().invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll().and().exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler);

    http.headers().frameOptions().disable();
  }
}
