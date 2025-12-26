package com.eazybytes.eazyschool.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "class") //my table name and my entity class name is different.
public class EazyClass extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;
    
               // same as we've declared in the Person 
    @OneToMany(mappedBy = "eazyClass", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,targetEntity = Person.class)
    private Set<Person> persons; // A class can have multiple persons
/*  Reason for lazy by default is suppose a class has 1000 persons/students.
    If I'm trying to unnecessarily load all the 1000 students at a time, 
    whenever I'm trying to load the class details ->create a performance problem.*/

}
