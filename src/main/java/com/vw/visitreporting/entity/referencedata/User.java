package com.vw.visitreporting.entity.referencedata;	//NOPMD

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.BrandRelated;
import com.vw.visitreporting.entity.DealerRelated;
import com.vw.visitreporting.entity.GeographyRelated;
import com.vw.visitreporting.entity.OrganisationRelated;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;


/**
 */
@Entity
@Table(schema = "PUBLIC", name = "USER")
public class User implements BaseVREntity, Serializable, BrandRelated, GeographyRelated, DealerRelated, OrganisationRelated {
	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	private String userId;

	@Column(name = "TYPE_OF_ORGANISATION", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer typeOfOrganisation;

	@Column(name = "FIRST_NAME", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String firstName;

	@Column(name = "SUR_NAME", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String surName;

	@Column(name = "EMAIL_ADDRESS", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String emailAddress;

	@Column(name = "LANDLINE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	@Max(50)
	private String landline;

	@Column(name = "MOBILE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	@Max(50)
	private String mobile;

	@Column(name = "PASSWORD", length = 15, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String password;

	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date createdDate;

	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	@Column(name = "IS_PASSWORD_CHANGE_REQUIRED")
	@Basic(fetch = FetchType.EAGER)
	private Byte isPasswordChangeRequired;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "POSITION_ID", referencedColumnName = "ID", nullable = false) })
	private Positions positions;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "DEALERSHIP_ID", referencedColumnName = "ID", nullable = true) })
	private Dealership dealership;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "FRANCHISE_GROUP_ID", referencedColumnName = "ID", nullable = true) })
	private FranchiseGroup franchiseGroup;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation organisation;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_GEOGRAPHICAL_AREA",
		joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
		inverseJoinColumns=@JoinColumn(name="AREA_ID", referencedColumnName="ID")
	)
	private Set<GeographicalArea> geographicalAreas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_USER_PROFILE",
		joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
		inverseJoinColumns=@JoinColumn(name="USER_PROFILE_ID", referencedColumnName="ID")
	)
	private Set<UserProfile> userProfiles;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_GEOGRAPHICAL_REGION",
		joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
		inverseJoinColumns=@JoinColumn(name="REGION_ID", referencedColumnName="ID")
	)
	private Set<GeographicalRegion> geographicalRegions;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_BUSINESS_AREA",
		joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
		inverseJoinColumns=@JoinColumn(name="BUSINESS_AREA_ID", referencedColumnName="ID")
	)
	private Set<BusinessArea> businessAreas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="USER_GS_CATEGORY",
		joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
		inverseJoinColumns=@JoinColumn(name="GS_CATEGORY_ID", referencedColumnName="ID")
	)
	private Set<GeographicalStructureCategory> geographicalStructureCategories;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="USER_BRAND", joinColumns=@JoinColumn(name="USER_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> userBrands;

	@Column(name = "IS_DISABLED", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Byte disabled;

	
	/**
	 */
	public User() {
	}
	
   public static User getLoginUser(){
		return ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();
    }
	
	//this can be cached
	public Set<Organisation> getAllowedOrganisations(){
		Set<Organisation> allowedOrgs = new HashSet<Organisation>();
		for(SharingRule aSharingRule: this.getOrganisation().getSharingRules()){
			allowedOrgs.add(aSharingRule.getTargetOrganisation());
			
			}
		allowedOrgs.add(this.getOrganisation());
		return allowedOrgs;
		}

	//this can be cached
	public Set<Level> getPossibleLevels(){
		Set<Level> possibleLevels = new HashSet<Level>();
		for(Organisation anOrganisation: getAllowedOrganisations()){
			for(Level aLevel: anOrganisation.getLevels()){
				possibleLevels.add(aLevel);
				}
			}
		
		return possibleLevels;
	}
	
	/**
	 */
	public void setUserId(String userId) {
		this.userId = userId.toLowerCase();
	}

	/**
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 */
	public void setId(String userId) {
		this.userId = userId;
	}

	/**
	 */
	public String getId() {
		return this.userId;
	}

	/**
	 */
	public void setTypeOfOrganisation(Integer typeOfOrganisation) {
		this.typeOfOrganisation = typeOfOrganisation;
	}

	/**
	 */
	public Integer getTypeOfOrganisation() {
		return this.typeOfOrganisation;
	}

	/**
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 */
	public void setSurName(String surName) {
		this.surName = surName;
	}

	/**
	 */
	public String getSurName() {
		return this.surName;
	}

	/**
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 */
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 */
	public void setLandline(String landline) {
		this.landline = landline;
	}

	/**
	 */
	public String getLandline() {
		return this.landline;
	}

	/**
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 */
	public String getMobile() {
		return this.mobile;
	}

	/**
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 */
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 */
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	/**
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 */
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	/**
	 */
	public void setIsPasswordChangeRequired(Byte isPasswordChangeRequired) {
		this.isPasswordChangeRequired = isPasswordChangeRequired;
	}

	/**
	 */
	public Byte getIsPasswordChangeRequired() {
		return this.isPasswordChangeRequired;
	}

	/**
	 */
	public void setPositions(Positions positions) {
		this.positions = positions;
	}

	/**
	 */
	public Positions getPositions() {
		return positions;
	}

	/**
	 */
	public void setDealership(Dealership dealership) {
		this.dealership = dealership;
	}

	/**
	 */
	public Dealership getDealership() {
		return dealership;
	}

	/**
	 */
	public void setFranchiseGroup(FranchiseGroup franchiseGroup) {
		this.franchiseGroup = franchiseGroup;
	}

	/**
	 */
	public FranchiseGroup getFranchiseGroup() {
		return this.franchiseGroup;
	}

	/**
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	/**
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 */
	public void setGeographicalAreas(Set<GeographicalArea> geographicalAreas) {
		this.geographicalAreas = geographicalAreas;
	}

	/**
	 */
	public Set<GeographicalArea> getGeographicalAreas() {
		if (geographicalAreas == null) {
			geographicalAreas = new LinkedHashSet<GeographicalArea>();
		}
		return geographicalAreas;
	}

	/**
	 */
	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

	/**
	 */
	public Set<UserProfile> getUserProfiles() {
		if (userProfiles == null) {
			userProfiles = new LinkedHashSet<UserProfile>();
		}
		return userProfiles;
	}

	/**
	 */
	public void setGeographicalRegions(Set<GeographicalRegion> geographicalRegions) {
		this.geographicalRegions = geographicalRegions;
	}

	/**
	 */
	public Set<GeographicalRegion> getGeographicalRegions() {
		if (geographicalRegions == null) {
			geographicalRegions = new LinkedHashSet<GeographicalRegion>();
		}
		return geographicalRegions;
	}

	/**
	 */
	public void setGeographicalStructureCategories(Set<GeographicalStructureCategory> geographicalStructureCategories) {
		this.geographicalStructureCategories = geographicalStructureCategories;
	}

	/**
	 */
	public Set<GeographicalStructureCategory> getGeographicalStructureCategories() {
		if (geographicalStructureCategories == null) {
			geographicalStructureCategories = new LinkedHashSet<GeographicalStructureCategory>();
		}
		return geographicalStructureCategories;
	}

	/**
	 */
	public void setBrands(Set<Brand> brands) {
		this.userBrands =  EnumUtil.convertFromEnum(brands);
	}

	/**
	 */
	public Set<Brand> getBrands() {
		return EnumUtil.convertToEnum(this.userBrands, Brand.class);
	}
	
	public EnumSet<Brand> getRelatedBrands() {
		return (EnumSet<Brand>)this.getBrands();
	}

	public Collection<GeographicalArea> getRelatedAreas() {
		return geographicalAreas;
	}
	
	public Collection<GeographicalRegion> getRelatedRegions() {
		return geographicalRegions;
	}
	
	/**
	 * Gets a collection of dealer instances that this entity instance is related to.
	 */
	public Collection<Dealership> getRelatedDealers() {
		return Collections.singleton(dealership);
	}


	public Set<BusinessArea> getBusinessAreas() {
		return businessAreas;
	}


	public void setBusinessAreas(Set<BusinessArea> businessAreas) {
		this.businessAreas = businessAreas;
	}

	
	/**
	 * Gets a collection of franchise group instances that this entity instance is related to.
	 */
	public Collection<FranchiseGroup> getRelatedFranchiseGroups() {
		return Collections.singleton(dealership.getFranchiseGroup());
	}
	
	/**
	 * Gets the Organisation that this entity instance is related to.
	 */
	public Organisation getRelatedOrganisation() {
		return this.organisation;
	}
	
	/**
	 */
	public void setDisabled(Byte disabled) {
		this.disabled = disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = (byte)(disabled ? 1 : 0);
	}

	/**
	 */
	public Byte getDisabled() {
		return this.disabled;
	}
	
	public boolean isDisabled() {
		return (disabled != null && disabled > 0);
	}

	public String getFullname() {
		return this.firstName + " " + this.surName;
	}
	
	/**
	 * Returns a textual representation of a bean.
	 */
	public String toString() {
		return this.getFullname();
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((userId == null) ? 0 : userId.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof User)) {
			return false;
		}
		User equalCheck = (User) obj;
		if ((userId == null && equalCheck.userId != null) || (userId != null && equalCheck.userId == null)) {
			return false;
		} else if (userId != null && !userId.equals(equalCheck.userId)) {
			return false;
		}
		return true;
	}
}
