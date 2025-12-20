package com.eazybytes.eazyschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.eazybytes.eazyschool.model.Address;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Profile;
import com.eazybytes.eazyschool.repository.PersonRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProfileController {
	
	@Autowired
	PersonRepository personRepository;
	
	@PostMapping(value = "/updateProfile") //since my UI web page is going to send data 
//	inside a Profile object, I need to catch 
//	@Valid @ModelAttribute will make sure that my S MVC will do the validations based upon d annotations
//                                    binds the form data from your UI into a Profile obj.
    public String updateProfile(@Valid @ModelAttribute("profile") Profile profile, Errors errors,
                                HttpSession session)
    {
		System.out.println("Inside updateProfile method");
        if(errors.hasErrors()){
            return "profile.html";
        }
        System.out.println("line no 39");
        Person person = (Person) session.getAttribute("loggedInPerson");
        person.setName(profile.getName());
        person.setEmail(profile.getEmail());
        person.setMobileNumber(profile.getMobileNumber());
        System.out.println("line no 44");
        if(null == person.getAddress() || !(person.getAddress().getAddressId()>0)){
        System.out.println("Inside if");
            person.setAddress(new Address()); //If I don't have new Address() I'll 
//            									get a NullPointerException here.
        }
        System.out.println("Outside if");
//      this code is again populating the address details in the form
        person.getAddress().setAddress1(profile.getAddress1());
        person.getAddress().setAddress2(profile.getAddress2());
        person.getAddress().setCity(profile.getCity());
        person.getAddress().setState(profile.getState());
        person.getAddress().setZipCode(profile.getZipCode());
        System.out.println("Before saved person");
//        Person savedPerson = personRepository.save(person);
        Person savedPerson = personRepository.save(person);
// once we store the info the DB, we we need to store the latest Pojo again into the 
// session because post this operation, user can go back again to the dashboard he can 
// come back to the profile web page or he can go to any other controller or web pages.
        session.setAttribute("loggedInPerson", savedPerson);
        return "redirect:/displayProfile";
    }
	
	 @RequestMapping("/displayProfile")
	    public ModelAndView displayMessages(Model model, HttpSession session) {
//		 getting the logged in user details
	        Person person = (Person) session.getAttribute("loggedInPerson");
	        Profile profile = new Profile();
	        profile.setName(person.getName());
	        profile.setMobileNumber(person.getMobileNumber());
	        profile.setEmail(person.getEmail());
	        if(person.getAddress() !=null && person.getAddress().getAddressId()>0){
	            profile.setAddress1(person.getAddress().getAddress1());
	            profile.setAddress2(person.getAddress().getAddress2());
	            profile.setCity(person.getAddress().getCity());
	            profile.setState(person.getAddress().getState());
	            profile.setZipCode(person.getAddress().getZipCode());
	        }
	        ModelAndView modelAndView = new ModelAndView("profile.html");
//	   I'm trying to add an object with the name Profile and I'm trying to assign the object
//     of Profile which I have created at the line number 21
	        modelAndView.addObject("profile",profile);
	        return modelAndView;
	    }
	
}
