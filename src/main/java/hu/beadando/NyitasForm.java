package hu.beadando;

public class NyitasForm {
    private String instrument;   // pl. EUR_USD
    private int units;           // mennyiség: pozitív = Long (vétel), negatív = Short (eladás)

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
