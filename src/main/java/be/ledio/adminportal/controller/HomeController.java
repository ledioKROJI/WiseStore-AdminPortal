package be.ledio.adminportal.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import be.ledio.adminportal.model.User;
import be.ledio.adminportal.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public String home(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "home";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
}
