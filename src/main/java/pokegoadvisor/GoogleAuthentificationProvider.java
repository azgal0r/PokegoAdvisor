package pokegoadvisor;

import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import org.apache.commons.logging.Log;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 8/15/2016.
 */
@Component("googleAuth")
public class GoogleAuthentificationProvider implements AuthenticationProvider {
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = authentication.getCredentials().toString();
        OkHttpClient httpClient = new OkHttpClient();
        GoogleUserCredentialProvider provider = null;
        try {
            provider = new GoogleUserCredentialProvider(httpClient);
            provider.login(token);
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(provider.getRefreshToken(), "", grantedAuths);
            return auth;
        } catch (LoginFailedException e) {
            throw new BadCredentialsException(e.getMessage(),e);
        } catch (RemoteServerException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
