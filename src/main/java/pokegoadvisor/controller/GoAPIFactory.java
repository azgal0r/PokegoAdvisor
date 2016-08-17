package pokegoadvisor.controller;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pokegoadvisor.security.AuthenticationToken;
import pokegoadvisor.security.SessionContext;

import javax.annotation.Resource;

/**
 * Created by Anthony on 8/17/2016.
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GoAPIFactory {

    @Resource
    SessionContext session = null;

    public PokemonGo createApiForCurrentUser() throws LoginFailedException, RemoteServerException {
        if (session.getGoApi() != null) {
            return session.getGoApi();
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            OkHttpClient httpClient = new OkHttpClient();
            if (auth instanceof AuthenticationToken) {
                AuthenticationToken authToken = (AuthenticationToken) auth;
                PokemonGo go = new PokemonGo(new GoogleUserCredentialProvider(httpClient, authToken.getPrincipal().toString()), httpClient);
                return go;
            } else if (auth instanceof UsernamePasswordAuthenticationToken) {
                //Le mot de passe n'existe plus, il faut se relogger
                throw new BadCredentialsException("Api lost, relog please");
            } else {
                throw new IllegalArgumentException("Authentication method unknown");
            }
        }
    }
}
