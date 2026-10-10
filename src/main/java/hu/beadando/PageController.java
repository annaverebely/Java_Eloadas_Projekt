package hu.beadando;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Főoldal + ideiglenes (stub) oldalak a menühöz.
 * Ahogy egy menüpont elkészül, vegyétek ki innen az útvonalát, és
 * csináljatok neki saját controllert + sablont (pl. SoapController).
 */
@Controller
public class PageController {

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("title", "Főoldal");
		return "index";
	}



	@GetMapping("/actual_prices")
	public String actual(Model m) { return stub(m, "Forex – Aktuális ár"); }

	@GetMapping("/hist_prices")
	public String hist(Model m) { return stub(m, "Forex – Historikus ár"); }

	@GetMapping("/open_position")
	public String open(Model m) { return stub(m, "Forex – Pozíció nyitás"); }

	@GetMapping("/positions")
	public String positions(Model m) { return stub(m, "Forex – Nyitott pozíciók"); }

	@GetMapping("/close_position")
	public String close(Model m) { return stub(m, "Forex – Pozíció zárás"); }

	private String stub(Model model, String title) {
		model.addAttribute("title", title);
		return "stub";
	}
}
