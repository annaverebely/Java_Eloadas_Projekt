package hu.beadando;

import org.springframework.stereotype.Component;

@Component
public class NyeresegKalkulator {

    // Long pozíció nyeresége/vesztesége (az árjegyzés devizájában, pl. EUR_USD-nél USD)
    public double nyeresegLong(int egyseg, double nyitoAr, double zaroAr) {
        ellenoriz(egyseg, nyitoAr, zaroAr);
        return (zaroAr - nyitoAr) * egyseg;
    }

    // Short pozíció nyeresége/vesztesége
    public double nyeresegShort(int egyseg, double nyitoAr, double zaroAr) {
        ellenoriz(egyseg, nyitoAr, zaroAr);
        return (nyitoAr - zaroAr) * egyseg;
    }

    // Árkülönbség pipben: JPY-pároknál 1 pip = 0.01, egyébként 0.0001
    public double pipKulonbseg(double nyitoAr, double zaroAr, boolean jpyPar) {
        ellenoriz(1, nyitoAr, zaroAr);
        double pipMeret = jpyPar ? 0.01 : 0.0001;
        return (zaroAr - nyitoAr) / pipMeret;
    }

    // Százalékos árváltozás a nyitó árhoz képest
    public double szazalekosValtozas(double nyitoAr, double zaroAr) {
        ellenoriz(1, nyitoAr, zaroAr);
        return (zaroAr - nyitoAr) / nyitoAr * 100;
    }

    // Bemenet ellenőrzése (a minta nullával osztás hibájának megfelelője)
    private void ellenoriz(int egyseg, double nyitoAr, double zaroAr) {
        if (egyseg <= 0)
            throw new IllegalArgumentException("Az egység csak pozitív egész szám lehet!");
        if (nyitoAr <= 0 || zaroAr <= 0)
            throw new IllegalArgumentException("Az ár csak pozitív szám lehet!");
    }
}