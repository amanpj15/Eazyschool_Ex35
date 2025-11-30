package com.eazybytes.eazyschool.model;

import org.hibernate.annotations.GenericGenerator;

import com.eazybytes.eazyschool.annotations.FieldsValueMatch;
import com.eazybytes.eazyschool.annotations.PasswordValidator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
//Here we are defining what will be the field 1 and field 2 name
//is going to perform the validations on top of the two fields like email
//Since this annotation always try to check the content of the 2 diff fields 
//instead of mentioning this annotation on top of two different fields.
//I can mention this annotation on top of my Pojo class itself.

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "pwd",
                fieldMatch = "confirmPwd",
                message = "Passwords do not match!"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email addresses do not match!"
        )
})
public class Person extends BaseEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name="native", strategy="native")
    private int personId;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email;

    @NotBlank(message="Confirm Email must not be blank")
    @Email(message = "Please provide a valid confirm email address" )
    @Transient //communicate to spring data JPA. Please do not consider 
//               this field for any type of DB related operations because 
//    			 obviously we are not going to save the confirmEmail, 
//    			 confirmPassword also into the DB private String confirmPwd
    private String confirmEmail;

    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")
    @PasswordValidator
    private String pwd;

    @NotBlank(message="Confirm Password must not be blank")
    @Size(min=5, message="Confirm Password must be at least 5 characters long")
    @Transient
    private String confirmPwd;

//    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Roles.class)
//    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
//    private Roles roles;

//    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, targetEntity = Address.class)
//    @JoinColumn(name = "address_id", referencedColumnName = "addressId",nullable = true)
//    private Address address;
    
    
    public int getPersonId() {
  		return personId;
  	}

  	public void setPersonId(int personId) {
  		this.personId = personId;
  	}

  	public String getName() {
  		return name;
  	}

  	public void setName(String name) {
  		this.name = name;
  	}

  	public String getMobileNumber() {
  		return mobileNumber;
  	}

  	public void setMobileNumber(String mobileNumber) {
  		this.mobileNumber = mobileNumber;
  	}

  	public String getEmail() {
  		return email;
  	}

  	public void setEmail(String email) {
  		this.email = email;
  	}

  	public String getConfirmEmail() {
  		return confirmEmail;
  	}

  	public void setConfirmEmail(String confirmEmail) {
  		this.confirmEmail = confirmEmail;
  	}

  	public String getPwd() {
  		return pwd;
  	}

  	public void setPwd(String pwd) {
  		this.pwd = pwd;
  	}

  	public String getConfirmPwd() {
  		return confirmPwd;
  	}

  	public void setConfirmPwd(String confirmPwd) {
  		this.confirmPwd = confirmPwd;
  	}
}