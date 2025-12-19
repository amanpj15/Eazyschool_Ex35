package com.eazybytes.eazyschool.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.PersonRepository;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {

//	with this i am able to fetch value from the DB.
	@Autowired
    PersonRepository personRepository;
	
    @RequestMapping("/dashboard")
    public String displayDashboard(Model model,Authentication authentication, HttpSession session) {
//  here we don't have the person email so far, So what I can do here is, 
//  I can go to the class where we are doing the custom authentication.
//    	Person person =personRepository.readByEmail(authentication.getName());
    	Person person =personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        session.setAttribute("loggedInPerson", person);
        return "dashboard.html";
    }

}
