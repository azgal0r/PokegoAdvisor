package pokegoadvisor.security;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 8/17/2016.
 */
@Component("PTCAuth")
public class PtcAuthentificationProvider implements AuthenticationProvider {

    @Resource
    private SessionContext session = null;

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        OkHttpClient httpClient = new OkHttpClient();
        try {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

            PokemonGo go = new PokemonGo(new PtcCredentialProvider(httpClient, userName, password), httpClient);
            session.setGoApi(go);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, password, grantedAuths);
            return auth;
        } catch (LoginFailedException e) {
            throw new BadCredentialsException(e.getMessage(),e);
        } catch (RemoteServerException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
