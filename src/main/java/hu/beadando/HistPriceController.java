package hu.beadando;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oanda.v20.Context;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.InstrumentName;

@Controller
public class HistPriceController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    // a lenyíló listák elemei
    private static final List<String> INSTRUMENTS =
            List.of("EUR_USD", "USD_CHF", "GBP_USD", "USD_JPY");
    private static final List<String> GRANULARITIES =
            List.of("M1", "H1", "D", "W", "M");

    // 1) az üres űrlap megjelenítése
    @GetMapping("/hist_prices")
    public String form(Model model) {
        HistForm form = new HistForm();
        form.setInstrument("EUR_USD");
        form.setGranularity("D");
        model.addAttribute("form", form);
        alapAdatok(model);
        return "hist_prices";
    }

    // 2) az elküldött űrlap feldolgozása
    @PostMapping("/hist_prices")
    public String query(@ModelAttribute("form") HistForm form, Model model) {
        alapAdatok(model);
        try {
            InstrumentCandlesRequest request =
                    new InstrumentCandlesRequest(new InstrumentName(form.getInstrument()));
            request.setGranularity(CandlestickGranularity.valueOf(form.getGranularity()));
            request.setCount(10L);   // az utolsó 10 ár

            InstrumentCandlesResponse resp = ctx.instrument.candles(request);
            model.addAttribute("candles", resp.getCandles());
        } catch (Exception e) {
            model.addAttribute("error", "Az Oanda lekérdezés sikertelen: " + e);
        }
        return "hist_prices";
    }

    private void alapAdatok(Model model) {
        model.addAttribute("title", "Forex – Historikus árak");
        model.addAttribute("instruments", INSTRUMENTS);
        model.addAttribute("granularities", GRANULARITIES);
    }
}
