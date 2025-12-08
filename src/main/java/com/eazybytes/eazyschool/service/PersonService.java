package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import com.eazybytes.eazyschool.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired 
    PasswordEncoder passwordEncoder;

    public boolean createNewPerson(Person person){
        boolean isSaved = false;
        Roles role = rolesRepository.getByRoleName(EazySchoolConstants.STUDENT_ROLE);
        person.setRoles(role);
        
        //Hashing the pass before saving  
//        #1
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        /* Here I am stored the pass user entered while registering
        So just before, I need to take the plain text pass that user is entered and
        convert the same to the hash value with the help of BCryptPasswordEncoder.*/
        person = personRepository.save(person);
        if (null != person && person.getPersonId() > 0) {
            isSaved = true;
        }
        return isSaved;
    }
// #1 the same BCryptPasswordEncoder we need to use during the login operation also.
//    And you know, the login related authentication logic we have written inside 
//    EazySchoolUsernamePasswordAuthenticatorProvider
}
