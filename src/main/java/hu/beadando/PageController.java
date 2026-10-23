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

	// React menü: a react.html-be töltődik be a lefordított React alkalmazás
	@GetMapping("/react")
	public String react(Model model) {
		model.addAttribute("title", "React");
		return "react";
	}
}
