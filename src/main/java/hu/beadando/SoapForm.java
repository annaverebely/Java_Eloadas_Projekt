package hu.beadando;

public class SoapForm {
    private String currency;   // kiválasztott deviza
    private String startDate;  // kezdő dátum
    private String endDate;    // záró dátum

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
