package it.uniroma3.siw.catering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.catering.service.BuffetService;

@Controller
public class IndexController {
	
	@Autowired
	private BuffetService buffetService;
	
	/* mostra la pagina con info sugi eventi */
	@GetMapping("/user/events")
	public String getEventsPage(Model model) {
		return "/user/events.html";
	}
	
	/* riporta all'indice dell'amministratore */
	@GetMapping("/admin")
	public String getAdminIndex(Model model) {
		return "/admin/admin_index.html";
	}
	
	/* mostra la lista con tutti i buffet presenti nel sistema */
	@GetMapping("/user/all_buffets")
	public String getBuffetsIndex(Model model) {
		model.addAttribute("buffet", buffetService.findAll());
		return "/user/all_buffets.html";
	}
}
