package engine.security;

import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {
    private UserService userDetailsService;

    @Autowired
    public WebSecurityConfigurerImpl(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // moramo da odradimo "override" nad metodom koja prima "AuthenticationManagerBuilder"
    // nakon toga povezuje "UserDetailsService"
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(userDetailsService.getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/api/register", "/actuator/shutdown", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable();
    }
}
