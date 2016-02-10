package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.vw.visitreporting.auditing.Audited;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.accesscontrolled.BrandCollectionAccessControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.OrganisationControlledEntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Details of a role within the company that a user can be employed to perform.
 * NOTE: POSITION is a keyword in SQL, hence the slightly awkward class name.
 */
@Entity
@Table(schema = "PUBLIC", name = "POSITIONS")
public class Positions implements BaseVREntity,Serializable,BrandCollectionAccessControlledEntity,OrganisationControlledEntity{
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "DESCRIPTION", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull 
	private String description;
	
	@Column(name = "IS_DISABLED", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Byte disabled;
	
	@Column(name = "VISIBLE_WITHIN_CONTACT_SCREEN", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Byte visibleWithinContactScreen;
	
	@Column(name = "LEVEL", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Integer level;
	
	@Column(name = "POSITION_SEQUENCE")
	@Basic(fetch = FetchType.EAGER)
	private Integer positionSequence;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	@NotNull
	private Organisation organisation;
	
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
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="POSITION_BRAND", joinColumns=@JoinColumn(name="POSITION_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> brands;

	@Basic(fetch = FetchType.EAGER)
	@Column(name="BRANDCODE")
	private Integer brandcode;

	/**
	 * Constructor. Sets all properties to default (null) values.
	 */
	public Positions() {
		disabled=0;
	}
	
	
	/**
	 * Sets the database primary key for this entity.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the database primary key for this entity.
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets the title of this position.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the title of this position.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Sets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	@Audited("disabled")
	public void setDisabled(Byte disabled) {
		this.disabled = disabled;
	}

	/**
	 * Gets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	public Byte getDisabled() {
		return this.disabled;
	}

	/**
	 * Sets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = (byte)(disabled ? 1 : 0);
	}

	/**
	 * Gets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	public boolean isDisabled() {
		return (disabled != null && disabled > 0);
	}
	
	/**
	 * Sets whether or not this position is visible in lists.
	 */
	public void setVisibleWithinContactScreen(boolean visibleWithinContactScreen) {
		this.visibleWithinContactScreen = (byte)(visibleWithinContactScreen ? 1 : 0);
	}

	/**
	 * Gets whether or not this position is visible in lists.
	 */
	public boolean getVisibleWithinContactScreen() {
		return (visibleWithinContactScreen != null && visibleWithinContactScreen > 0);
	}

	/**
	 * Sets the level of authority that this position has within the company.
	 */
	public void setLevel(Level level) {
		this.level = level.getId();
	}

	/**
	 * Gets the level of authority that this position has within the company.
	 */
	public Level getLevel() {
		return EnumUtil.getEnumObject(this.level, Level.class);
	}
	
	/**
	 * Sets an integer value that can be compared with other Positions to indicate ordering
	 * within a sequence. Lower numbers mean higher in a list.
	 */
	public void setPositionSequence(Integer positionSequence) {
		this.positionSequence = positionSequence;
	}

	/**
	 * Gets an integer value that can be compared with other Positions to indicate ordering
	 * within a sequence. Lower numbers mean higher in a list.
	 */
	public Integer getPositionSequence() {
		return this.positionSequence;
	}

	/**
	 * Sets the organisation to which this position is associated.
	 * A user who holds this position will be associated to this organisation.
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	/**
	 * Gets the organisation to which this position is associated.
	 * A user who holds this position will be associated to this organisation.
	 */
	public Organisation getOrganisation() {
		return this.organisation;
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
	 * Sets the brands to which this position is associated.
	 * A user who holds this position will be associated to these brands.
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = EnumUtil.convertFromEnum(brands);
	      int code = 0;
	      for(Brand b : brands) {
	            code += (1 << (b.getId() - 1));
	      }
		this.brandcode= code;
	}
	
	public Integer getBrandcode() {
		return brandcode;
	}

	/**
	 * Gets the brands to which this position is associated.
	 * A user who holds this position will be associated to these brands.
	 */
	public Set<Brand> getBrands() {
		return EnumUtil.convertToEnum(this.brands, Brand.class);
	}

	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return this.description;
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
		if(obj == null){
			return false;
		}
		if (obj == this){
			return true;
		}
		if (!(obj instanceof Positions)){
			return false;
		}
		if(this.getId().equals(((Positions)obj).getId())){
			return true;
		}
		return false;
	}


	@Override
	public Collection<Brand> getAccessControlledBrands() {
		return getBrands();
	}

}
