package com.eazybytes.eazyschool.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name="holidays")
public class Holiday extends BaseEntity {

    @Id
    private String day;

    private String reason;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        FESTIVAL, FEDERAL
    }

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
    
}
