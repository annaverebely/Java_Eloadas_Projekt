package hu.beadando;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("title", "Főoldal");
		return "index";
	}

	// React menü
	@GetMapping("/react")
	public String react(Model model) {
		return stub(model, "React");
	}

	private String stub(Model model, String title) {
		model.addAttribute("title", title);
		return "stub";
	}
}
