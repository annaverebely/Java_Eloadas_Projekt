package hu.beadando;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.oanda.v20.Context;
import com.oanda.v20.trade.Trade;

@Controller
public class PositionsController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    @GetMapping("/positions")
    public String positions(Model model) {
        model.addAttribute("title", "Forex – Nyitott pozíciók");
        try {
            List<Trade> trades = ctx.trade.listOpen(Config.ACCOUNTID).getTrades();
            model.addAttribute("trades", trades);
        } catch (Exception e) {
            model.addAttribute("error", "Az Oanda lekérdezés sikertelen: " + e);
        }
        return "positions";
    }
}
