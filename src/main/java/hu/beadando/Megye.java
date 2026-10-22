package hu.beadando;

import jakarta.persistence.*;

// React menü: a megyek tábla egy sora (a megyenevekhez és a lenyíló listához kell)
@Entity
@Table(name = "megyek")
public class Megye {
    @Id
    private Integer megyekod;
    private Integer regiokod;
    private String megyenev;

    public Megye() {
    }

    public Megye(Integer megyekod, Integer regiokod, String megyenev) {
        this.megyekod = megyekod;
        this.regiokod = regiokod;
        this.megyenev = megyenev;
    }

    public Integer getMegyekod() {
        return megyekod;
    }

    public void setMegyekod(Integer megyekod) {
        this.megyekod = megyekod;
    }

    public Integer getRegiokod() {
        return regiokod;
    }

    public void setRegiokod(Integer regiokod) {
        this.regiokod = regiokod;
    }

    public String getMegyenev() {
        return megyenev;
    }

    public void setMegyenev(String megyenev) {
        this.megyenev = megyenev;
    }
}
