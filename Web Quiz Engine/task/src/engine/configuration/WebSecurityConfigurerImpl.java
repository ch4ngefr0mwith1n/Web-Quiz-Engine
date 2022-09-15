package engine.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // podešavanja za testove:
                .mvcMatchers("/api/register", "/h2-console/**", "/actuator/shutdown").permitAll()
                // svi ostali endpoint-ovi su za verifikovane korisnike:
                .anyRequest().authenticated()
                .and()
                .httpBasic() // omogućava HTTP Basic verifikaciju
                .and()
                // ovo omogućava da šaljemo POST zahtjeve preko Postman-a:
                .csrf().disable().headers().frameOptions().disable();

        http.headers().frameOptions().disable();
    }

    // enkodiranje lozinke:
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
