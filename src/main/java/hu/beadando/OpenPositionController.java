package hu.beadando;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oanda.v20.Context;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.transaction.OrderFillTransaction;

import com.oanda.v20.transaction.TradeReduce;

@Controller
public class OpenPositionController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    private static final List<String> INSTRUMENTS =
            List.of("EUR_USD", "USD_CHF", "GBP_USD", "USD_JPY");

    // 1) az üres űrlap megjelenítése
    @GetMapping("/open_position")
    public String form(Model model) {
        NyitasForm form = new NyitasForm();
        form.setInstrument("EUR_USD");
        form.setUnits(10);
        model.addAttribute("form", form);
        alapAdatok(model);
        return "open_position";
    }

    // 2) az elküldött űrlap feldolgozása: pozíció nyitása piaci áron
    @PostMapping("/open_position")
    public String open(@ModelAttribute("form") NyitasForm form, Model model) {
        alapAdatok(model);

        if (form.getUnits() == 0) {
            model.addAttribute("error", "A mennyiség nem lehet 0 (pozitív: Long, negatív: Short).");
            return "open_position";
        }

        try {
            MarketOrderRequest marketOrder = new MarketOrderRequest();
            marketOrder.setInstrument(new InstrumentName(form.getInstrument()));
            marketOrder.setUnits(form.getUnits());   // + : Long, - : Short

            OrderCreateRequest request = new OrderCreateRequest(Config.ACCOUNTID);
            request.setOrder(marketOrder);
            OrderCreateResponse response = ctx.order.create(request);

            OrderFillTransaction fill = response.getOrderFillTransaction();
            if (fill == null) {
                // a megbízás nem teljesült, pl. zárt piac (hétvége)
                model.addAttribute("error", "A megbízás nem teljesült: "
                        + response.getOrderCancelTransaction().getReason());
            } else {
                String irany = form.getUnits() > 0 ? "Long (vétel)" : "Short (eladás)";
                String uzenet = "Megbízás teljesítve: " + form.getInstrument() + ", "
                        + Math.abs(form.getUnits()) + " egység, " + irany
                        + ", ár: " + fill.getPrice() + ".";

                // ellentétes irányú megbízás: meglévő trade(ek) lezárása
                if (fill.getTradesClosed() != null) {
                    for (TradeReduce t : fill.getTradesClosed()) {
                        uzenet += " Lezárta a(z) " + t.getTradeID() + " azonosítójú tradet.";
                    }
                }
                // ellentétes irányú megbízás: meglévő trade részleges csökkentése
                if (fill.getTradeReduced() != null) {
                    uzenet += " Csökkentette a(z) " + fill.getTradeReduced().getTradeID() + " azonosítójú tradet.";
                }
                // új pozíció nyílt
                if (fill.getTradeOpened() != null) {
                    uzenet += " Új pozíció nyílt, tradeId: " + fill.getTradeOpened().getTradeID() + ".";
                }
                model.addAttribute("ok", uzenet);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Az Oanda lekérdezés sikertelen: " + e);
        }
        return "open_position";
    }

    private void alapAdatok(Model model) {
        model.addAttribute("title", "Forex – Pozíció nyitás");
        model.addAttribute("instruments", INSTRUMENTS);
    }
}