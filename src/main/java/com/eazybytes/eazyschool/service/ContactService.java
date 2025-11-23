package com.eazybytes.eazyschool.service;

import com.eazybytes.eazyschool.constants.EazySchoolConstants;
import com.eazybytes.eazyschool.model.Contact;
import com.eazybytes.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
@Slf4j, is a Lombok-provided annotation that will automatically generate an SLF4J
Logger static property in the class at compilation time.
* */
@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    /**
     * Save Contact Details into DB
     * @param contact
     * @return boolean
     */
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = false;
        contact.setStatus(EazySchoolConstants.OPEN);
        
//      With Auditing no need to populate manually
//      contact.setCreatedBy(EazySchoolConstants.ANONYMOUS);
//      contact.setCreatedAt(LocalDateTime.now());
        Contact savedContact = contactRepository.save(contact);
        if(null != savedContact && savedContact.getContactId()>0) {
            isSaved = true;
        }
        return isSaved;
    }
    
//    public boolean updateMsgStatus(int contactId, String updatedBy){
    public boolean updateMsgStatus(int contactId){
    	boolean isUpdated = false;
    	Optional<Contact> contact = contactRepository.findById(contactId);
    	contact.ifPresent(contact1 -> {
    		contact1.setStatus(EazySchoolConstants.CLOSE);
//            With Auditing no need to populate manually
//            contact1.setUpdatedBy(updatedBy);
//            contact1.setUpdatedAt(LocalDateTime.now());
    	});
    	Contact updatedContact = contactRepository.save(contact.get());
    	if(null != updatedContact && updatedContact.getUpdatedBy()!=null) {
    		isUpdated = true;
    	}
    	return isUpdated;
    }

    public List<Contact> findMsgsWithOpenStatus(){
        List<Contact> contactMsgs = contactRepository.findByStatus(EazySchoolConstants.OPEN);
        return contactMsgs;
    }


}
