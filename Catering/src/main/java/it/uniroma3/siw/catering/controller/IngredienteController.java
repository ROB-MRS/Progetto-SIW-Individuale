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
import it.uniroma3.siw.catering.model.Ingrediente;
//import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.IngredienteService;
//import it.uniroma3.siw.catering.service.PiattoService;
import it.uniroma3.siw.catering.validator.IngredienteValidator;

@Controller
public class IngredienteController {
	
	@Autowired private IngredienteService ingredienteService;
//	@Autowired private PiattoService piattoService;
	@Autowired private IngredienteValidator ingredienteValidator;
	
	/* accesso al managment degli ingredienti */
	@GetMapping("/admin/ingrediente_managment")
	public String getIngredienteManagment(Model model) {
		model.addAttribute("ingredienti", ingredienteService.findAll());
		return "/admin/ingrediente_managment.html";
	}
	
	//=============================================================
	//****CREAZIONE NUOVO INGREDIENTE****//
	
	/* accesso alla form per creare un nuovo ingrediente */
	@GetMapping("/admin/ingrediente_managment/add")
	public String getIngredienteForm(Model model) {
		Ingrediente nuovoIngrediente = new Ingrediente();
		model.addAttribute("ingrediente", nuovoIngrediente);
		return "/admin/ingrediente_form.html";
	}
	
	/* metodo che inserisce l'ingrediente all'interno della base di dati */
	@PostMapping("/admin/ingrediente_managment")
	public String addIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResults, Model model) {
		this.ingredienteValidator.validate(ingrediente, bindingResults);
		if(!bindingResults.hasErrors()) {
			this.ingredienteService.save(ingrediente);
			model.addAttribute("ingrediente", model);
			return "redirect:/admin/ingrediente_managment";
		}
		else
			return "/admin/ingrediente_form.html";
	}
	
	//====================================================
	//****MODIFICA INGREDINETE****//
	
	/* metodo che porta alla form per la modifica di un ingrediente */
	@GetMapping("/admin/ingrediente_managment/edit/{ingred_id}")
	public String getEditIngredienteForm(@PathVariable("ingred_id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "/admin/ingrediente_edit.html";
	}
	
	/* metodo che inserisce l'ingrediente aggiornato nella base di dati */
	@PostMapping("/admin/ingrediente_managment/edit/{ingr_id}")
	public String editIngrediente(@PathVariable("ingr_id") Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,
			BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Ingrediente nuovoIngrediente = new Ingrediente();
			nuovoIngrediente.setId(id);
			nuovoIngrediente.setNome(ingrediente.getNome());
			nuovoIngrediente.setDescrizione(ingrediente.getDescrizione());
			nuovoIngrediente.setOrigine(ingrediente.getOrigine());
			this.ingredienteService.save(nuovoIngrediente);
			return "redirect:/admin/ingrediente_managment";
		}
		
		return "/admin/ingrediente_edit.html";
	}
	
	
	//====================================================
	//****ELIMINAZIONE INGREDIENTE****//
	
	/* metodo per eliminare un ingrediente e i piatti che lo contengono */
	@GetMapping("/admin/ingrediente_managment/delete/{ingred_id}")
	public String deleteIngrediente(@PathVariable Long ingred_id) {
		this.ingredienteService.deleteIngrediente(ingred_id);
		return "redirect:/admin/ingrediente_managment";
	}
}