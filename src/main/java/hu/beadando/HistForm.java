package hu.beadando;

public class HistForm {
    private String instrument;    // pl. EUR_USD
    private String granularity;   // pl. D (napi)

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }
}
