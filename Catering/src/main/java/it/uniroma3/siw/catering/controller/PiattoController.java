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

import it.uniroma3.siw.catering.model.Buffet;
import it.uniroma3.siw.catering.model.Piatto;
import it.uniroma3.siw.catering.service.BuffetService;
import it.uniroma3.siw.catering.service.IngredienteService;
import it.uniroma3.siw.catering.service.PiattoService;
import it.uniroma3.siw.catering.validator.PiattoValidator;

@Controller
public class PiattoController {

	@Autowired private PiattoService piattoService;
	@Autowired private BuffetService buffetService;
	@Autowired private IngredienteService ingredienteService;
	@Autowired private PiattoValidator piattoValidator;
	
	/* metodo che mostra la pagina per scegliere il buffet del quale si vogliono gestire i piatti */
	@GetMapping("/admin/buffet_choose")
	public String getBuffetChoose(Model model) {
		model.addAttribute("buffets", this.buffetService.findAll());
		return "/admin/buffet_choose.html";
	}
	
	/* metodo che mostra i piatti di un determinato buffet il quale id e' passato come parametro */
	@GetMapping("/admin/piatto_managment/{buffet_id}")
	public String getPiattoManagment(@PathVariable Long buffet_id, Model model) {
		Buffet buffet = this.buffetService.findById(buffet_id);
		model.addAttribute("piatti", this.piattoService.findAllByBuffet(buffet));
		model.addAttribute("buffet", buffet);
		return "/admin/piatto_managment.html";
	}
	
	//=======================================================
	//****CREAZIONE NUOVO PIATTO****//
	
	/* metodo per accedere alla form per creare nuovo piatto */
	@GetMapping("/admin/piatto_managment/add/{buffet_id}")
	public String createPiattoForm(@PathVariable Long buffet_id, Model model) {
		Piatto nuovoPiatto = new Piatto();
		model.addAttribute("piatto", nuovoPiatto);
		model.addAttribute("buffet_id", buffet_id);
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "/admin/piatto_form.html";
	}

	/* metodo che inserisce il piatto nella base di dati */
	@PostMapping("/admin/piatto_managment/{buffet_id}")
	public String addPiatto(@PathVariable Long buffet_id, @Valid @ModelAttribute("piatto") Piatto piatto, BindingResult bindingResults, Model model) {
		Buffet buffet = buffetService.findById(buffet_id);
		buffet.addPiatto(piatto);
		piatto.setBuffet(buffet);
		this.piattoValidator.validate(piatto, bindingResults);
		if(!bindingResults.hasErrors()) {
			piattoService.save(piatto);
			model.addAttribute("piatto", this.piattoService.findAllByBuffet(buffet));
			model.addAttribute("buffet_id", buffet_id);
			return "redirect:/admin/piatto_managment/" + buffet_id;
		}
		else {
			model.addAttribute("tuttiGliIngredienti", ingredienteService.findAll());
			return "/admin/piatto_form.html";
		}
	}
	
	//============================================================
	//****CANCELLAZIONE PIATTO****//
	
	/* metodo che elimina un determinato piatto dalla base di dati */
	@GetMapping("/admin/piatto_managment/delete/{piatto_id}")
	public String deletePiatto(@PathVariable Long piatto_id) {
		Long buffet_id = this.piattoService.findById(piatto_id).getBuffet().getId();
		this.piattoService.deletePiatto(piatto_id);
		return "redirect:/admin/piatto_managment/" + buffet_id;
	}
	
	/*============================================================*/
	//****ACCESSO ALLE INFORMAZIONI****//
	
	/* metodo che mostra i piatti di un determinato buffet */
	@GetMapping("/user/all_buffets/buffet/piatto/{piatto_id}")
	public String getPiatto(@PathVariable Long piatto_id, Model model) {
		model.addAttribute("piatto", this.piattoService.findById(piatto_id));
		model.addAttribute("buffet_id", this.piattoService.findById(piatto_id).getBuffet().getId());
		return "/user/piatto.html";
	}
}
