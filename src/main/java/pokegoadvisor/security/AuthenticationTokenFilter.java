package pokegoadvisor.security;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Anthony on 8/17/2016.
 */
@Component("authTokenFilter")
public class AuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    @Resource(name ="googleAuth")
    private AuthenticationProvider googleAuthentificationProvider;

    private String authHeaderName = "googleAuthToken";

    public final static String LOGIN_URL = "/user";

    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();


    @PostConstruct
    private void init() {
        setAuthenticationManager(new ProviderManager(Arrays.asList(googleAuthentificationProvider)));
        setAllowSessionCreation(true);
        setAuthenticationSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {});
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public AuthenticationTokenFilter() {
        super(new AntPathRequestMatcher(LOGIN_URL));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }
        String token = request.getHeader(authHeaderName);
        AuthenticationToken authRequest = new AuthenticationToken(null,token);
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        boolean needAuth =  super.requiresAuthentication(request, response);
        needAuth = needAuth && StringUtils.hasText(request.getHeader(authHeaderName));
        return needAuth;
    }
}