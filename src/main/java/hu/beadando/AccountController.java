package hu.beadando;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountSummary;

@Controller
public class AccountController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("title", "Forex – Számlainformációk");
        try {
            AccountSummary summary = ctx.account.summary(Config.ACCOUNTID).getAccount();
            model.addAttribute("summary", summary);
        } catch (Exception e) {
            model.addAttribute("error", "Az Oanda lekérdezés sikertelen: " + e);
        }
        return "account";
    }
}
