package org.temkarus0070.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private String someSecret = "abcdedfghjkl;";
    @Autowired
    private AuthProvider authenticationProvider;

    @Autowired
    private UserDetailsManager userDetailsManager;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/register/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:80", "http://example.com"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        });
        http.authorizeRequests()
                .mvcMatchers(("/api/auth/**")).permitAll()
                .mvcMatchers("/**").authenticated()
                .and()
                .logout(withDefaults());
        http
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index")
                .permitAll();
        http.authenticationProvider(authenticationProvider);

        http.rememberMe(withDefaults());
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
