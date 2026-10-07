package hu.beadando;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import soapclient.MNBArfolyamServiceSoapImpl;

@Controller
public class SoapController {

    // legördülő menü
    private static final List<String> CURRENCIES =
            List.of("EUR", "USD", "GBP", "CHF", "JPY", "CZK", "PLN", "RON", "AUD", "CAD");

    @GetMapping("/soap")
    public String form(Model model) {
        SoapForm form = new SoapForm();
        form.setCurrency("EUR");
        form.setStartDate(LocalDate.now().minusDays(30).toString()); // 30 napja
        form.setEndDate(LocalDate.now().toString());                  // ma

        model.addAttribute("title", "SOAP – MNB árfolyamok");
        model.addAttribute("form", form);
        model.addAttribute("currencies", CURRENCIES);
        return "soap";   // a templates/soap.html sablon
    }

    @PostMapping("/soap")
    public String query(@ModelAttribute("form") SoapForm form, Model model) {
        model.addAttribute("title", "SOAP – MNB árfolyamok");
        model.addAttribute("currencies", CURRENCIES);

        try {
            var service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();
            String xml = service.getExchangeRates(
                    form.getStartDate(), form.getEndDate(), form.getCurrency());
            model.addAttribute("xml", xml);
        } catch (Exception e) {
            model.addAttribute("error", "Az MNB adatok lekérdezése sikertelen: " + e.getMessage());
        }
        return "soap";
    }
}
