package com.vw.visitreporting.entity.referencedata;	//NOPMD

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.DealerRelated;
import com.vw.visitreporting.entity.GeographyRelated;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A franchisee retail outlet for VWG vehicles and services.
 */
@Entity
@Table(schema = "PUBLIC", name = "DEALERSHIP")
public class Dealership implements BaseVREntity, Serializable, GeographyRelated, DealerRelated {
	private static final long serialVersionUID = 1L;

	private static final NumberFormat DEALER_NUMBER_FORMAT = new DecimalFormat("00000");

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "BRAND_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer brandId;

	@Column(name = "DEALER_NUMBER", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer dealerNumber;

	@Column(name = "DEALER_NAME", length = 200, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String dealerName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "FRANCHISE_GROUP_ID", referencedColumnName = "ID", nullable = false) })
	private FranchiseGroup franchiseGroup;

	@Column(name = "MAINLINE_TELEPHONE")
	@Basic(fetch = FetchType.EAGER)
	private String mainlineTelephone;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumns({ @JoinColumn(name = "TRADING_ADDRESS_ID", referencedColumnName = "ID", nullable = false) })
	private Address tradingAddress;
	
	@OneToMany(orphanRemoval=true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="DEALERSHIP_ADDRESS",
		joinColumns=@JoinColumn(name="DEALERSHIP_ID"),
		inverseJoinColumns=@JoinColumn(name="ADDRESS_ID")
	)
	private Set<Address> addresses;

	@Column(name = "FAX_NUMBER")
	@Basic(fetch = FetchType.EAGER)
	private String faxNumber;

	@Column(name = "WEBSITE_LABEL", length = 100, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String websiteLabel;

	@Column(name = "WEBSITE_LINK", length = 200, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String websiteLink;

	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String modifiedBy;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="DEALERSHIP_BUSINESS_AREA",
		joinColumns=@JoinColumn(name="DEALERSHIP_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="BUSINESS_AREA_ID", referencedColumnName="ID")
	)
	private Set<BusinessArea> businessAreas;

	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name="DEALERSHIP_GEOGRAPHICAL_STRUCTURE_AREA",
		joinColumns=@JoinColumn(name="DEALERSHIP_ID"),
		inverseJoinColumns=@JoinColumn(name="GEOGRAPHICAL_AREA_ID")
	)
	private Set<GeographicalArea> geographicalAreas;
/*
	@OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinColumn(name="DEALERSHIP_ID")
	private Set<User> users;
*/
	@OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="DEALERSHIP_ID")
	private Set<DealerKpiTarget> dealerKpiTargets;

	@OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="DEALERSHIP_ID")
	private Set<DealerKpiActual> dealerKpiActuals;

	@ManyToMany(mappedBy = "dealerships", fetch = FetchType.LAZY)
	private Set<Agenda> agendas;

	@ManyToMany(mappedBy = "dealerships", fetch = FetchType.LAZY)
	private Set<Action> actions;

	
	/**
	 */
	public Dealership() {
	}

	
	/**
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 */
	public void setBrand(Brand brand) {
		this.brandId = brand.getId();
	}

	/**
	 */
	public Brand getBrand() {
		return EnumUtil.getEnumObject(this.brandId, Brand.class);
	}

	/**
	 */
	public void setDealerNumber(Integer dealerNumber) {
		this.dealerNumber = dealerNumber;
	}

	/**
	 */
	public Integer getDealerNumber() {
		return this.dealerNumber;
	}

	/**
	 * Returns a string representation of the dealerNumber property in the format 00000
	 */
	public String getDealerNumberString() {
		return DEALER_NUMBER_FORMAT.format(dealerNumber);
	}

	/**
	 */
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	/**
	 */
	public String getDealerName() {
		return this.dealerName;
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
	public void setMainlineTelephone(String mainlineTelephone) {
		this.mainlineTelephone = mainlineTelephone;
	}

	/**
	 */
	public String getMainlineTelephone() {
		return this.mainlineTelephone;
	}

	public Address getTradingAddress() {
		return tradingAddress;
	}

	public void setTradingAddress(Address tradingAddress) {
		this.tradingAddress = tradingAddress;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	/**
	 */
	public String getFaxNumber() {
		return this.faxNumber;
	}

	/**
	 */
	public void setWebsiteLabel(String websiteLabel) {
		this.websiteLabel = websiteLabel;
	}

	/**
	 */
	public String getWebsiteLabel() {
		return this.websiteLabel;
	}

	/**
	 */
	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}

	/**
	 */
	public String getWebsiteLink() {
		return this.websiteLink;
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
	public void setBusinessAreas(Set<BusinessArea> businessAreas) {
		this.businessAreas = businessAreas;
	}

	/**
	 */
	public Set<BusinessArea> getBusinessAreas() {
		return businessAreas;
	}

	/**
	 */
	public void setGeographicalAreas(Set<GeographicalArea> geographicalAreas) {
		this.geographicalAreas = geographicalAreas;
	}

	/**
	 */
	public Set<GeographicalArea> getGeographicalAreas() {
		return geographicalAreas;
	}

	public void setDealerKpiTargets(Set<DealerKpiTarget> dealerKpiTargets) {
		this.dealerKpiTargets = dealerKpiTargets;
	}

	public Set<DealerKpiTarget> getDealerKpiTargets() {
		return dealerKpiTargets;
	}

	public void setAgendas(Set<Agenda> agendas) {
		this.agendas = agendas;
	}

	public Set<Agenda> getAgendas() {
		return agendas;
	}

	public void setDealerKpiActuals(Set<DealerKpiActual> dealerKpiActuals) {
		this.dealerKpiActuals = dealerKpiActuals;
	}

	public Set<DealerKpiActual> getDealerKpiActuals() {
		return dealerKpiActuals;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	public Set<Action> getActions() {
		return actions;
	}

	
	/**
	 * Gets a collection of dealer instances that this entity instance is related to.
	 */
	public Collection<Dealership> getRelatedDealers() {
		return Collections.singleton(this);
	}

	/**
	 * Gets a collection of franchise group instances that this entity instance is related to.
	 */
	public Collection<FranchiseGroup> getRelatedFranchiseGroups() {
		return Collections.singleton(franchiseGroup);
	}
	
	public Collection<GeographicalArea> getRelatedAreas() {
		return geographicalAreas;
	}
	
	public Collection<GeographicalRegion> getRelatedRegions() {
		HashSet<GeographicalRegion> result = new HashSet<GeographicalRegion>();
		for(GeographicalArea geographicalArea : geographicalAreas) {
			result.add(geographicalArea.getGeographicalRegion());
		}
		return result;
	}
	
	
	/**
	 * Returns a textual representation of a bean.
	 *
	 */
	public String toString() {
		return DEALER_NUMBER_FORMAT.format(this.dealerNumber) + " - " + this.dealerName;
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Dealership)) {
			return false;
		}
		Dealership equalCheck = (Dealership) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}
}
