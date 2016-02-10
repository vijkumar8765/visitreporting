package com.vw.visitreporting.dto;


/**
 * Encapsulates the data that is looked up for a person in the Volkswagen Group.
 * This DTO will be used by implementations of PersonDao to return results.
 */
public class PersonDTO {

	private String username;
	private String firstname;
	private String surname;
	private String email;
	private String landline;
	private String mobile;
	private String organisation;
	private String brand;
	private String businessArea;
	
	/**
	 * Constructor. Creates an empty instance of this DTO.
	 */
	public PersonDTO() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLandline() {
		return landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}
}
