package com.eazybytes.eazyschool.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
public class Address extends BaseEntity{

    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
//    @GenericGenerator(name = "native",strategy = "native")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @NotBlank(message="Address1 must not be blank")
    @Size(min=5, message="Address1 must be at least 5 characters long")
    private String address1;

    private String address2;

    @NotBlank(message="City must not be blank")
    @Size(min=5, message="City must be at least 5 characters long")
    private String city;

    @NotBlank(message="State must not be blank")
    @Size(min=5, message="State must be at least 5 characters long")
    private String state;

    @NotBlank(message="Zip Code must not be blank")
    @Pattern(regexp="(^$|[0-9]{5})",message = "Zip Code must be 5 digits")
//    private int zipCode;
    private String zipCode; //otherwise by default we have 0 here

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	

}
