package com.eazybytes.eazyschool.security;

import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.model.Roles;
import com.eazybytes.eazyschool.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EazySchoolUsernamePwdAuthenticationProvider
        implements AuthenticationProvider
{
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Person person = personRepository.readByEmail(email);
/* whenever I try to load the Person object my roles details also will be loaded by my JPA.
   reason -> if you go and check this person entity config, FetchType we have configured is EAGER*/
        if(null != person && person.getPersonId()>0 && //null check for the email
        		passwordEncoder.matches(pwd,person.getPwd())) {
//            pwd.equals(person.getPwd())){ instead of this write 
        	
//        	post authentication we are setting the name of the user in the 
//        	auth obj insted of that populate email
            return new UsernamePasswordAuthenticationToken(
                    email, pwd, getGrantedAuthorities(person.getRoles()));
// Whatever we are passing in first paramater will display post login            
        }
        else
            throw new BadCredentialsException("Invalid credentials!");
        
    }
    
//    @Override
//    public Authentication authenticate(Authentication authentication)
//            throws AuthenticationException {
//        String email = authentication.getName();
//        String pwd = authentication.getCredentials().toString();
//        Person person = personRepository.readByEmail(email);
//        
//        if(null != person && person.getPersonId()>0 && 
//        		passwordEncoder.matches(pwd,person.getPwd())) {
//            return new UsernamePasswordAuthenticationToken(
//                    person.getName(), pwd, getGrantedAuthorities(person.getRoles()));
//        }else
//            throw new BadCredentialsException("Invalid credentials!");
//        
//    }

/*So now I have a problem here where I want to populate my roles, information that I have inside my
PersonEntity object into a format that my spring security can understand.
So that's where I want to return a new method with the name getGrantedAuthorities().
 * */
    
//GrantedAuthority is an interface so I need to create an object of SimpleGrantedAuthority.
//Reason why it is accepting list one person can have multiple roles.
    private List<GrantedAuthority> getGrantedAuthorities(Roles roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        return grantedAuthorities;
    }
//PasswordEncoder is there for encryption
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
