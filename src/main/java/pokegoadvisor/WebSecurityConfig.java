package pokegoadvisor;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import pokegoadvisor.security.AuthenticationTokenFilter;
import pokegoadvisor.security.PtcAuthentificationProvider;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.Arrays;

/**
 * Created by Anthony on 8/9/2016.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Resource(name = "authTokenFilter")
    private Filter filterToken;

    @Resource(name = "PTCAuth")
    private AuthenticationProvider ptcAuth;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/index.html", "/home.html", "/login.html","/loginGoogle.html","/loginPtc.html", "/getGoogleCode", "/", "/*.js", "/*.css").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and().authenticationProvider(ptcAuth)
                .addFilterAfter(filterToken, UsernamePasswordAuthenticationFilter.class);
    }
}
