package com.eazybytes.eazyschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eazybytes.eazyschool.model.Person;
//import com.eazybytes.eazyschool.service.PersonService;
import com.eazybytes.eazyschool.service.PersonService;

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequestMapping("public")
public class PublicController {

	@Autowired
	PersonService personService;
	
	@RequestMapping(value = "/register", method = {RequestMethod.GET})
	public String displayRegisterPage(Model model) {
/*	we are populating a new object Person. Cuz we are going to store all the details that an user is
	entering while registering we are going to save in a table called person.
    So this person, Pojo will represent as an entity for my person table.*/

//	Assigning Person Pojo class, to the attribute person here.
		model.addAttribute("person", new Person());
		return "register.html";
	}
	
	
	@RequestMapping(value = "/createUser", method = {RequestMethod.POST})
	public String createUser(@Valid @ModelAttribute("person") Person person, Errors errors) {
		if(errors.hasErrors()) {
			return "register.html";
		}
		boolean isSaved = personService.createNewPerson(person);
		if(isSaved)
			return "redirect:/login?register=true;";
		else return "register.html";
			
		//we are passing this register = true for my login path, I need to make 
//		sure that I'm catching this query param inside my LoginController.
	}
}
