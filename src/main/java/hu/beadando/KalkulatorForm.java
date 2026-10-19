package hu.beadando;

public class KalkulatorForm {
    private String instrumentum = "EUR_USD";  // pl. EUR_USD, USD_JPY
    private String irany = "long";            // long vagy short
    private int egyseg = 1000;                // a pozíció mérete
    private double nyitoAr;                   // nyitó ár
    private double zaroAr;                    // záró ár

    public String getInstrumentum() {
        return instrumentum;
    }

    public void setInstrumentum(String instrumentum) {
        this.instrumentum = instrumentum;
    }

    public String getIrany() {
        return irany;
    }

    public void setIrany(String irany) {
        this.irany = irany;
    }

    public int getEgyseg() {
        return egyseg;
    }

    public void setEgyseg(int egyseg) {
        this.egyseg = egyseg;
    }

    public double getNyitoAr() {
        return nyitoAr;
    }

    public void setNyitoAr(double nyitoAr) {
        this.nyitoAr = nyitoAr;
    }

    public double getZaroAr() {
        return zaroAr;
    }

    public void setZaroAr(double zaroAr) {
        this.zaroAr = zaroAr;
    }
}
