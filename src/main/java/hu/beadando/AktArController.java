package hu.beadando;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oanda.v20.Context;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;

@Controller
public class AktArController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    // a lenyíló lista elemei
    private static final List<String> INSTRUMENTS =
            List.of("EUR_USD", "USD_CHF", "GBP_USD", "USD_JPY");

    // 1) az üres űrlap megjelenítése
    @GetMapping("/actual_prices")
    public String form(Model model) {
        AktArForm form = new AktArForm();
        form.setInstrument("EUR_USD");   // alapértelmezett választás
        model.addAttribute("form", form);
        alapAdatok(model);
        return "actual_prices";
    }

    // 2) az elküldött űrlap feldolgozása
    @PostMapping("/actual_prices")
    public String query(@ModelAttribute("form") AktArForm form, Model model) {
        alapAdatok(model);
        try {
            PricingGetRequest request =
                    new PricingGetRequest(Config.ACCOUNTID, List.of(form.getInstrument()));
            PricingGetResponse resp = ctx.pricing.get(request);
            model.addAttribute("prices", resp.getPrices());
        } catch (Exception e) {
            model.addAttribute("error", "Az Oanda lekérdezés sikertelen: " + e);
        }
        return "actual_prices";
    }

    private void alapAdatok(Model model) {
        model.addAttribute("title", "Forex – Aktuális ár");
        model.addAttribute("instruments", INSTRUMENTS);
    }
}
