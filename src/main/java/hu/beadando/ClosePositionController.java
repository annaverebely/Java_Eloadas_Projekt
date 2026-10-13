package hu.beadando;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oanda.v20.Context;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeCloseResponse;
import com.oanda.v20.trade.TradeSpecifier;
import com.oanda.v20.transaction.OrderFillTransaction;

@Controller
public class ClosePositionController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    // 1) az üres űrlap megjelenítése a nyitott tradek listájával
    @GetMapping("/close_position")
    public String form(Model model) {
        model.addAttribute("form", new ZarasForm());
        alapAdatok(model);
        return "close_position";
    }

    // 2) az elküldött űrlap feldolgozása: a kiválasztott trade lezárása
    @PostMapping("/close_position")
    public String close(@ModelAttribute("form") ZarasForm form, Model model) {
        if (form.getTradeId() == null || form.getTradeId().isBlank()) {
            model.addAttribute("error", "Válassz ki egy lezárandó tradet.");
        } else {
            try {
                TradeCloseRequest request = new TradeCloseRequest(
                        Config.ACCOUNTID, new TradeSpecifier(form.getTradeId()));
                TradeCloseResponse response = ctx.trade.close(request);

                OrderFillTransaction fill = response.getOrderFillTransaction();
                if (fill == null) {
                    // a zárás nem teljesült, pl. zárt piac (hétvége)
                    model.addAttribute("error", "A zárás nem teljesült: "
                            + response.getOrderCancelTransaction().getReason());
                } else {
                    model.addAttribute("ok", "A(z) " + form.getTradeId()
                            + " azonosítójú trade lezárva. Záró ár: " + fill.getPrice()
                            + ", realizált nyereség/veszteség: " + fill.getPl() + ".");
                }
            } catch (Exception e) {
                model.addAttribute("error", "Az Oanda lekérdezés sikertelen: " + e);
            }
        }
        alapAdatok(model);   // a lista frissítése: a lezárt trade már nem lesz benne
        return "close_position";
    }

    private void alapAdatok(Model model) {
        model.addAttribute("title", "Forex – Pozíció zárás");
        try {
            List<Trade> trades = ctx.trade.listOpen(Config.ACCOUNTID).getTrades();
            model.addAttribute("trades", trades);
        } catch (Exception e) {
            model.addAttribute("error", "A nyitott tradek lekérdezése sikertelen: " + e);
        }
    }
}