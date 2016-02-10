package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 */
@Entity
@Table(schema = "PUBLIC", name = "FRANCHISE_GROUP")
public class FranchiseGroup implements Serializable, BaseVREntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="FRANCHISE_GROUP_BRAND", joinColumns=@JoinColumn(name="FRANCHISE_GROUP_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> brands;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="FRANCHISE_GROUP_ID") 
	private Set<Dealership> dealerships;
	
	@Column(name = "WEBSITE_LABEL", length = 150)
	@Basic(fetch = FetchType.EAGER)
	private String websiteLabel;

	@Column(name = "WEBSITE_LINK", length = 200)
	@Basic(fetch = FetchType.EAGER)
	private String websiteLink;
	
	/*
	@ManyToMany(mappedBy = "franchiseGroups", fetch = FetchType.LAZY)
	private Set<Action> actions;
	*/

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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 */
	public String getName() {
		return this.name;
	}

	/**
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands =  EnumUtil.convertFromEnum(brands);
	}

	/**
	 */
	public Set<Brand> getBrands() {
		return EnumUtil.convertToEnum(this.brands, Brand.class);
	}
	
	
	/**
	 *
	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	public Set<Action> getActions() {
		if (actions == null) {
			actions = new java.util.LinkedHashSet<Action>();
		}
		return actions;
	}
	*/

	public Set<Dealership> getDealerships() {
		return dealerships;
	}

	public void setDealerships(Set<Dealership> dealerships) {
		this.dealerships = dealerships;
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
	public FranchiseGroup() {
	}


	/**
	 * Returns a textual representation of a bean.
	 */
	public String toString() {
		return name;
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
		} else if (!(obj instanceof FranchiseGroup)) {
			return false;
		}
		FranchiseGroup equalCheck = (FranchiseGroup) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}
}
