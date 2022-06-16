package it.uniroma3.siw.catering.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.catering.model.Chef;
import it.uniroma3.siw.catering.service.ChefService;
import it.uniroma3.siw.catering.validator.ChefValidator;

@Controller
public class ChefController {

	@Autowired private ChefService chefService;
	@Autowired private ChefValidator chefValidator;

	/* accesso al managment degli chef */
	@GetMapping("/admin/chef_managment")
	public String getChefManagment(Model model) {
		model.addAttribute("chefs", this.chefService.findAll());
		return "/admin/chef_managment.html";
	}
	
	//=========================================================
	//****CREAZIONE NUOVO CHEF****//

	/* accesso alla form per creare un nuovo chef */
	@GetMapping("/admin/chef_managment/add")
	public String getChefForm(Model model) {
		Chef nuovoChef = new Chef();
		model.addAttribute("chef", nuovoChef);
		return "/admin/chef_form.html";
	}

	/* metodo che inserisce i dati del nuovo chef nella base di dati (dopo averli validati) */
	@PostMapping("/admin/chef_managment")
	public String addChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResults, Model model) {
		this.chefValidator.validate(chef, bindingResults);
		if(!bindingResults.hasErrors()) {
			this.chefService.save(chef);
			model.addAttribute("chef", model);
			return "redirect:/admin/chef_managment";
		}
		else
			return "/admin/chef_form.html";
	}
	
	//===========================================================
	//****MODIFICA DI UNO CHEF****//
	
	/* metodo per modificare uno chef */
	@GetMapping("/admin/chef_managment/edit/{chef_id}")
	public String getEditChefForm(@PathVariable Long chef_id, Model model) {
		model.addAttribute("chef", this.chefService.findById(chef_id));
		return "admin/chef_edit.html";
	}

	/* metodo che inserisce lo chef aggiornato nella base di dati */
	@PostMapping("/admin/chef_managment/edit/{chef_id}")
	public String editChef(@PathVariable Long chef_id, @Valid @ModelAttribute("chef") Chef chef, 
			BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Chef nuovoChef = this.chefService.findById(chef_id);
			nuovoChef.setId(chef_id);
			nuovoChef.setNome(chef.getNome());
			nuovoChef.setCognome(chef.getCognome());
			nuovoChef.setNazionalita(chef.getNazionalita());
			this.chefService.editChef(nuovoChef);
			model.addAttribute("chef", model);
			return "redirect:/admin/chef_managment";
		}
		else
			return "/admin/chef_edit.html";
	}
	
	//===============================================================
	//****ELIMINAZIONE CHEF****//
	
	/* metodo per eliminare uno chef e i buffet che offre */
	@GetMapping("/admin/chef_managment/delete/{chef_id}")
	public String deleteChef(@PathVariable Long chef_id) {
		this.chefService.deleteChefById(chef_id);
		return "redirect:/admin/chef_managment";
	}
	
	//==============================================================
	//****ACCESSO ALLE INFORMAZIONI****//
	
	/* metodo che restituisce la pagina con le informazioni dello chef il quale id e' passato per parametro */
	@GetMapping("/user/all_buffets/chef/{chef_id}")
	public String getChef(@PathVariable Long chef_id, Model model) {
		model.addAttribute("chef", this.chefService.findById(chef_id));
		model.addAttribute("buffets", this.chefService.findById(chef_id).getBuffet());
		return "/user/chef.html";
	}
}