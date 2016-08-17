package pokegoadvisor.security;

import com.pokegoapi.api.PokemonGo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Created by Anthony on 8/17/2016.
 */
@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionContext {
    private PokemonGo goApi = null;


    public PokemonGo getGoApi() {
        return goApi;
    }

    public void setGoApi(PokemonGo goApi) {
        this.goApi = goApi;
    }
}
