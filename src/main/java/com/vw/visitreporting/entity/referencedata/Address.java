package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hsqldb.lib.StringUtil;

import com.vw.visitreporting.entity.referencedata.enums.AddressType;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


@Entity
@Table(schema = "PUBLIC", name = "ADDRESS")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ADDRESS_TYPE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer addressType;

	@Column(name = "HOUSE_NUMBER")
	@Basic(fetch = FetchType.EAGER)
	private Short houseNumber;

	@Column(name = "HOUSE_NAME", length = 200)
	@Basic(fetch = FetchType.EAGER)
	private String houseName;

	@Column(name = "STREET_NAME1", length = 150, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String streetName1;

	@Column(name = "STREET_NAME2", length = 150)
	@Basic(fetch = FetchType.EAGER)
	private String streetName2;

	@Column(name = "TOWN", length = 100)
	@Basic(fetch = FetchType.EAGER)
	private String town;

	@Column(name = "DISTRICT", length = 100)
	@Basic(fetch = FetchType.EAGER)
	private String district;

	@Column(name = "COUNTY", length = 100)
	@Basic(fetch = FetchType.EAGER)
	private String county;

	@Column(name = "POST_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String postCode;

	@Column(name = "COUNTRY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String country;

	
	/**
	 */
	public Address() {
	}
	
	public Address(AddressType type, Integer houseNumber, String houseName, String streetName1, String streetName2, String town, String district, String county, String postCode, String country) {//NOPMD
		this.addressType = type.getId();
		this.houseNumber = (houseNumber == null) ? null : houseNumber.shortValue();
		this.houseName = houseName;
		this.streetName1 = streetName1;
		this.streetName2 = streetName2;
		this.town = town;
		this.district = district;
		this.county = county;
		this.postCode = postCode;
		this.country = country;
	}
	
	
	/**
	 * Gets an array of the non-empty address entries, as would be written on an envelope.
	 */
	public String[] getLines() {
		ArrayList<String> lines = new ArrayList<String>();
		if(houseNumber != null) {
			lines.add(houseNumber.toString()+" "+streetName1);
		} else if( ! StringUtil.isEmpty(houseName)) {
			lines.add(houseName);
			if( ! StringUtil.isEmpty(streetName1)) {
				lines.add(streetName1);
			}
		}
		if( ! StringUtil.isEmpty(streetName2)) {
			lines.add(streetName2);
		}
		if( ! StringUtil.isEmpty(town)) {
			lines.add(town);
		}
		if( ! StringUtil.isEmpty(district)) {
			lines.add(district);
		}
		if( ! StringUtil.isEmpty(county)) {
			lines.add(county);
		}
		if( ! StringUtil.isEmpty(postCode)) {
			lines.add(postCode);
		}
		if( ! StringUtil.isEmpty(country)) {
			lines.add(country);
		}		
		return lines.toArray(new String[lines.size()]);
	}


	/**
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 */
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType.getId();
	}

	/**
	 */
	public AddressType getAddressType() {
		return EnumUtil.getEnumObject(this.addressType, AddressType.class);
	}

	/**
	 */
	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = (houseNumber == null) ? null : houseNumber.shortValue();
	}

	/**
	 */
	public Integer getHouseNumber() {
		return (this.houseNumber == null) ? null : this.houseNumber.intValue();
	}

	/**
	 */
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	/**
	 */
	public String getHouseName() {
		return this.houseName;
	}

	/**
	 */
	public void setStreetName1(String streetName1) {
		this.streetName1 = streetName1;
	}

	/**
	 */
	public String getStreetName1() {
		return this.streetName1;
	}

	/**
	 */
	public void setStreetName2(String streetName2) {
		this.streetName2 = streetName2;
	}

	/**
	 */
	public String getStreetName2() {
		return this.streetName2;
	}

	/**
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 */
	public String getTown() {
		return this.town;
	}

	/**
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 */
	public String getDistrict() {
		return this.district;
	}

	/**
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 */
	public String getCounty() {
		return this.county;
	}

	/**
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 */
	public String getPostCode() {
		return this.postCode;
	}

	/**
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 */
	public String getCountry() {
		return this.country;
	}
	
	/**
	 * Provides a string representaiton of this object
	 */
	public String toString() {
		return this.town + " " + this.streetName1;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Address)) {
			return false;
		}
		Address equalCheck = (Address) obj;
		return toString().equals(equalCheck);
	}
}
