package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.EazyClass;
//import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    EazyClassRepository eazyClassRepository;

    @Autowired
    PersonRepository personRepository;

// admin can see the list of classes that we already have inside the DB.
    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model) {
    	List<EazyClass> eazyClasses = eazyClassRepository.findAll(); //gives all classes info
//    	So the same List I need to send to my UI by populating a object inside my ModelAndView.
        ModelAndView modelAndView = new ModelAndView("classes.html");
/*  Since we are going to allow the admin to create the new classes by 
    providing the class name, we need to pass a new obj of my EasyClass entity. */

        modelAndView.addObject("eazyClasses",eazyClasses); //send the all EasyClasses list to my UI
        modelAndView.addObject("eazyClass",new EazyClass()); //classes.html ->iterating through them
        return modelAndView;
    }
/* And inside my classes.html I'm going to use this object and I'm going to tie this obj to my
 * HTML form using which my admin can create new classes & the same info can come back to the 
 * controller layer & we can save that info eventually into the DB.*/

    @PostMapping("/addNewClass")//2nd Param the same Pojo object that my form is trying to submit to 
//                                                        the backend server with the name EasyClass.
    public ModelAndView addNewClass(Model model, @ModelAttribute("eazyClass") EazyClass eazyClass) {
        eazyClassRepository.save(eazyClass);
//      redirect to same page once saved, before that we need to populate all the classes we have in DB
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

/* why @Transactional findById(id) returns Optional with persons collection still in lazy mode.
   When you call eazyClass.get().getPersons(), Hibernate tries to fetch the persons collection.
   But by this time, the Session is already closed (because v r outside of a transactional context in your controller).
   Result: failed to lazily initialize a collection of role … could not initialize proxy – no Session.
*/
/* Wrap the method in a transaction so the Hibernate session stays open while you access the lazy collection
   This ensures the persons collection can be initialized within the same transaction. */
    
    @Transactional
    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id) {
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(id);
// load all the persons associated to this class by calling getPersons() avl inside this EasyClass obj
        for(Person person : eazyClass.get().getPersons()){ //eazyClass.get() have to do this bcoz of Optional
            person.setEazyClass(null);
            personRepository.save(person);
        }
        
//  before invoking deleteById() make sure all the persons associated to this class are set to null
//  By this I am not using the automatic cascading effect
        eazyClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }
    
    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
    									@RequestParam(value = "error", required = false) String error) {
	  String errorMessage = null;
      ModelAndView modelAndView = new ModelAndView("students.html"); // false->not reqd in all cases 
      Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId); // session me store
//    Once I have this EasyClass object, I'm trying to send the same to my UI by adding
//    an object with the name EasyClass and using .get() due to Optional 
      modelAndView.addObject("eazyClass",eazyClass.get()); // -> to display current class name
      modelAndView.addObject("person",new Person()); 
//    Tie up the data from the form and sending it to backend      
      session.setAttribute("eazyClass", eazyClass.get());
     
      if(error != null) {
        errorMessage = "Invalid Email entered!!";
        modelAndView.addObject("errorMessage", errorMessage);
    }
        return modelAndView;
    }

    /*So now inside 2nd param that I'm receiving from the frontend, I will have only the email of the Person.
     * post that we are fetching student details on the basis of email
     * If there is a Person, 'personEntity' will be not null & vice versa.
     */
    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){ // since we are passing query params
//        											we have to add that inside the /displayStudents
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId()
                    +"&error=true"); // error->email entered is not valid
            return modelAndView;
        }
        personEntity.setEazyClass(eazyClass);
        personRepository.save(personEntity);
        eazyClass.getPersons().add(personEntity);
        eazyClassRepository.save(eazyClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

//    @GetMapping("/deleteStudent")
//    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
//        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
//        Optional<Person> person = personRepository.findById(personId);
//        person.get().setEazyClass(null);
//        eazyClass.getPersons().remove(person.get());
//        EazyClass eazyClassSaved = eazyClassRepository.save(eazyClass);
//        session.setAttribute("eazyClass",eazyClassSaved);
//        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
//        return modelAndView;
//    }
}
