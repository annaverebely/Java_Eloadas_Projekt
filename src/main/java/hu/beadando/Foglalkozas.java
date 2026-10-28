package hu.beadando;

import jakarta.persistence.*;

// React menü: a foglalkozasok tábla egy sora (megyénként az ágazatokban dolgozók száma)
@Entity
@Table(name = "foglalkozasok")
public class Foglalkozas {
    @Id
    private Integer fkod;         // a sor azonosítója (pl. 101), az adatfájlból jön
    private Integer megyekod;     // melyik megye (a megyek tábla megyekod mezője)
    private int mezogazdasag;
    private int ipar;
    private int szolgaltatas;

    public Foglalkozas() {
    }

    public Foglalkozas(Integer fkod, Integer megyekod, int mezogazdasag, int ipar, int szolgaltatas) {
        this.fkod = fkod;
        this.megyekod = megyekod;
        this.mezogazdasag = mezogazdasag;
        this.ipar = ipar;
        this.szolgaltatas = szolgaltatas;
    }

    public Integer getFkod() {
        return fkod;
    }

    public void setFkod(Integer fkod) {
        this.fkod = fkod;
    }

    public Integer getMegyekod() {
        return megyekod;
    }

    public void setMegyekod(Integer megyekod) {
        this.megyekod = megyekod;
    }

    public int getMezogazdasag() {
        return mezogazdasag;
    }

    public void setMezogazdasag(int mezogazdasag) {
        this.mezogazdasag = mezogazdasag;
    }

    public int getIpar() {
        return ipar;
    }

    public void setIpar(int ipar) {
        this.ipar = ipar;
    }

    public int getSzolgaltatas() {
        return szolgaltatas;
    }

    public void setSzolgaltatas(int szolgaltatas) {
        this.szolgaltatas = szolgaltatas;
    }
}
