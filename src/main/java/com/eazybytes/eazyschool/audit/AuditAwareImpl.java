package com.eazybytes.eazyschool.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

//what is the bean name of the class is passing here
@Component("auditAwareImpl") 
public class AuditAwareImpl implements AuditorAware<String> {

//	This method will help my SD JPA to identify who is the 
//	logged in user trying to perform certain action inside my web app.
    @Override
    public Optional<String> getCurrentAuditor() {
            return Optional.ofNullable(SecurityContextHolder
            							.getContext()
            							.getAuthentication()
            							.getName());
    }
}