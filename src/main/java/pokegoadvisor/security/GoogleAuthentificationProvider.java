package pokegoadvisor.security;

import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 8/15/2016.
 */
@Component("googleAuth")
public class GoogleAuthentificationProvider implements AuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isTrue(!authentication.isAuthenticated(), "Already authenticated");
        if (!StringUtils.hasText((String) authentication.getCredentials())) {
            throw new InternalAuthenticationServiceException("Token must not be empty");
        }

        OkHttpClient httpClient = new OkHttpClient();
        GoogleUserCredentialProvider provider = null;
        try {
            provider = new GoogleUserCredentialProvider(httpClient);
            provider.login(authentication.getCredentials().toString());
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            String refreshToken = provider.getRefreshToken();
            if (refreshToken == null) {
                throw new AuthenticationServiceException("Login with google failed : refresh token is null");
            }
            AuthenticationToken auth = new AuthenticationToken(provider.getRefreshToken(),authentication.getCredentials().toString(), grantedAuths);
            return auth;
        } catch (LoginFailedException e) {
            throw new BadCredentialsException(e.getMessage(),e);
        } catch (RemoteServerException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
