package pokegoadvisor.dto;

/**
 * Created by Anthony on 8/15/2016.
 */
public class PokemonDTO {
    private String name;
    private Double ivRatio;
    private Integer cp;
    private Integer numero;


    public PokemonDTO(String name, Double ivRatio, Integer cp, Integer numero) {
        this.name = name;
        this.ivRatio = ivRatio;
        this.cp = cp;
        this.numero = numero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getIvRatio() {
        return ivRatio;
    }

    public void setIvRatio(Double ivRatio) {
        this.ivRatio = ivRatio;
    }

    public Integer getCp() {
        return cp;
    }

    public void setCp(Integer cp) {
        this.cp = cp;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
