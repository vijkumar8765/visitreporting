package com.vw.visitreporting.web.form;

import java.util.ArrayList;
import java.util.List;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


public class OrganisationBrand {

	private Integer organisationId;
	private String organisationName;
	private Brand brand;
	
	public OrganisationBrand() {
	}
	
	public OrganisationBrand(Organisation organisation, Brand brand) {
		this.organisationId = organisation.getId();
		this.organisationName = organisation.getName();
		this.brand = brand;
	}
	
	
	public static List<OrganisationBrand> listFrom(List<Organisation> orgs) {
		List<OrganisationBrand> result = new ArrayList<OrganisationBrand>();
		for(Organisation org : orgs) {
			if(isVwgOrDealer(org.getName())) {
				for(Brand brand : Brand.values()) {
					result.add(new OrganisationBrand(org, brand));
				}
			} else if(isFS(org.getName())) {
				result.add(new OrganisationBrand(org, null));
			} else {
				for(Brand brand : org.getBrands()) {
					result.add(new OrganisationBrand(org, brand));
				}
			}
		}
		return result;
	}
	
	private static boolean isVwgOrDealer(String orgName) {
		return ("Dealerships".equals(orgName) || "VWG-UK".equals(orgName));
	}
	private static boolean isFS(String orgName) {
		return ("VW-FS".equals(orgName));
	}

	public static OrganisationBrand fromId(String id) {
		if(id == null || id.length() == 0) {
			return null;
		}
		String[] tokens = id.split(":");
		OrganisationBrand orgBrand = new OrganisationBrand();
		orgBrand.setOrganisationId(Integer.valueOf(tokens[0]));
		if(tokens.length > 1) {
			orgBrand.setBrand(EnumUtil.getEnumObject(Integer.valueOf(tokens[1]), Brand.class));
		}
		return orgBrand;
	}

	public String getId() {
		if(brand == null) {
			return organisationId.toString();
		} else {
			return organisationId + ":" + brand.getId();
		}
	}
	
	public String toString() {
		if(isVwgOrDealer()) {
			return brand.getShortName() + " " + organisationName;
		} else if(isFS() || brand == null) {
			return organisationName;
		} else {
			return organisationName + " (" + brand.getShortName() + ")";
		}
	}
	
	public boolean isVwgOrDealer() {
		return isVwgOrDealer(organisationName);
	}
	
	public boolean isFS() {
		return isFS(organisationName);
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	
	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((organisationId == null) ? 0 : organisationId.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		OrganisationBrand other = (OrganisationBrand) obj;
		if (brand != other.brand) {
			return false;
		}
		if (organisationId == null) {
			if (other.organisationId != null) {
				return false;
			}
		} else if (!organisationId.equals(other.organisationId)) {
			return false;
		}
		return true;
	}
}
