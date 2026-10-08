package hu.beadando;

public class Arfolyam {
    private final String datum;
    private final int egyseg;
    private final double ertek;

    public Arfolyam(String datum, int egyseg, double ertek) {
        this.datum = datum;
        this.egyseg = egyseg;
        this.ertek = ertek;
    }

    public String getDatum() { return datum; }
    public int getEgyseg() { return egyseg; }
    public double getErtek() { return ertek; }
}
