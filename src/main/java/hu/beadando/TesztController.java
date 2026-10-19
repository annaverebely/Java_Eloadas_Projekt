package hu.beadando;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TesztController {

    private static final List<String> INSTRUMENTUMOK =
            List.of("EUR_USD", "GBP_USD", "USD_CHF", "USD_JPY");

    private final NyeresegKalkulator kalkulator;

    // a kalkulátort a Spring adja át (dependency injection), mint a mintában
    public TesztController(NyeresegKalkulator kalkulator) {
        this.kalkulator = kalkulator;
    }

    // 1) az üres űrlap megjelenítése
    @GetMapping("/teszt")
    public String urlap(Model model) {
        model.addAttribute("kalkForm", new KalkulatorForm());
        alapAdatok(model);
        return "teszt";
    }

    // 2) az elküldött űrlap feldolgozása: nyereség/veszteség számítása
    @PostMapping("/teszt")
    public String szamol(@ModelAttribute("kalkForm") KalkulatorForm form, Model model) {
        alapAdatok(model);
        try {
            int egyseg = form.getEgyseg();
            double nyito = form.getNyitoAr();
            double zaro = form.getZaroAr();

            double pl = "short".equalsIgnoreCase(form.getIrany())
                    ? kalkulator.nyeresegShort(egyseg, nyito, zaro)
                    : kalkulator.nyeresegLong(egyseg, nyito, zaro);
            boolean jpyPar = form.getInstrumentum().endsWith("JPY");

            double plKerekitett = Math.round(pl * 100) / 100.0 + 0.0;  // +0.0: a -0.0-ból 0.0 lesz
            model.addAttribute("eredmeny", String.format(Locale.US, "%.2f", plKerekitett));
            model.addAttribute("nyereseges", plKerekitett >= 0);
            double pip = Math.round(kalkulator.pipKulonbseg(nyito, zaro, jpyPar) * 10) / 10.0 + 0.0;
            model.addAttribute("pip", String.format(Locale.US, "%.1f", pip));
            model.addAttribute("szazalek", String.format(Locale.US, "%.3f",
                    kalkulator.szazalekosValtozas(nyito, zaro)));
            model.addAttribute("deviza", form.getInstrumentum().substring(4)); // pl. EUR_USD -> USD
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "teszt";
    }

    private void alapAdatok(Model model) {
        model.addAttribute("title", "Teszt – Forex nyereség-kalkulátor");
        model.addAttribute("instrumentumok", INSTRUMENTUMOK);
    }
}
