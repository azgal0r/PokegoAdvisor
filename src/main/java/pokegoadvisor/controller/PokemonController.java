package pokegoadvisor.controller;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.pokemon.PokemonMeta;
import com.pokegoapi.api.pokemon.PokemonMetaRegistry;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pokegoadvisor.dto.PokemonDTO;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Anthony on 8/15/2016.
 */
@RestController
public class PokemonController {
    private PokemonGo go;

    @RequestMapping("/getPokemons")
    public List<PokemonDTO> getPokemons(@RequestParam(name = "key", required = true) String googleKey) throws LoginFailedException, RemoteServerException {
        if (go == null) {
            OkHttpClient httpClient = new OkHttpClient();
            GoogleUserCredentialProvider provider = new GoogleUserCredentialProvider(httpClient);
            provider.login(googleKey);
            go = new PokemonGo(provider, httpClient);
        }
        Inventories inventories = go.getInventories();
        PokeBank pokeBank = inventories.getPokebank();
        List<PokemonDTO> listDTO = new ArrayList<>();
        for (Pokemon pokemon : pokeBank.getPokemons()) {
            listDTO.add(new PokemonDTO(pokemon.getPokemonId().name(),
                    new BigDecimal(pokemon.getIvRatio()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(),
                    pokemon.getCp(),
                    pokemon.getPokemonId().getNumber()));
        }
        Collections.sort(listDTO, (PokemonDTO o1, PokemonDTO o2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(o1.getNumero(), o2.getNumero())
                    .append(o2.getIvRatio(), o1.getIvRatio());
            return builder.toComparison();
             });
        return listDTO;
    }

    @RequestMapping("/getGoogleCode")
    public Map<String,String> getGoogleCode() {
        Map<String,String> map = new HashMap<>();
        map.put("url", GoogleUserCredentialProvider.LOGIN_URL);
        return map;
    }
}
