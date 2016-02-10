package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.vw.visitreporting.auditing.Audited;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A type or instance of an organisation. New organisations similar to Mondial would be added to this table.
 */
@Entity
@Table(schema = "PUBLIC", name = "ORGANISATION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Audited("Organisation")
public class Organisation implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String name;
	
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

	@ManyToMany(mappedBy = "organisations", fetch = FetchType.LAZY)
	private Set<BusinessArea> businessAreas;
	
	@OneToMany(mappedBy = "organisation", cascade = { CascadeType.ALL }, orphanRemoval=true, fetch = FetchType.EAGER)
	private Set<OrganisationFunction> allowedFunctions;
	
	@OneToMany(mappedBy = "organisation", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<GeographicalStructureCategory> geographicalStructureCategories;

	@OneToMany(mappedBy = "organisation", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<MessageBoardNotificationRule> messageBoardNotificationRules;
	
	@OneToMany(mappedBy = "organisation", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<DraftDocumentDeletionRule> draftDocumentDeletionRules;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="ORGANISATION_BRAND", joinColumns=@JoinColumn(name="ORGANISATION_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> brands;
	
	@OneToMany(mappedBy = "organisation", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<StandardTopic> standardTopics;
	
	@OneToMany(mappedBy = "rootOrganisation", cascade = { CascadeType.ALL }, orphanRemoval=true, fetch = FetchType.EAGER)
	private Set<SharingRule> sharingRules;
	
	/**
	 * This organisation is the beneficiary of the sharing and will have access to
	 * information about the other organisation listed in the sharing rule 
	 */
	@OneToMany(mappedBy = "targetOrganisation", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<SharingRule> sharingRulesSharedAsAtargetOrganisation;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="ORGANISATION_LEVEL", joinColumns=@JoinColumn(name="ORGANISATION_ID"))
	@Column(name="LEVEL")
	private Set<Integer> levels;
	
	
	/**
	 */
	public Organisation() {
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
	@Audited("permissions")
	public void setAllowedFunctions(Set<OrganisationFunction> functions) {
		this.allowedFunctions = functions;
	}

	/**
	 */
	public Set<OrganisationFunction> getAllowedFunctions() {
		return allowedFunctions;
	}

	/**
	 */
	public void setGeographicalStructureCategories(Set<GeographicalStructureCategory> geographicalStructureCategories) {
		this.geographicalStructureCategories = geographicalStructureCategories;
	}

	/**
	 */
	public Set<GeographicalStructureCategory> getGeographicalStructureCategories() {
		return geographicalStructureCategories;
	}

	/**
	 */
	public void setMessageBoardNotificationRules(Set<MessageBoardNotificationRule> messageBoardNotificationRules) {
		this.messageBoardNotificationRules = messageBoardNotificationRules;
	}

	/**
	 */
	public Set<MessageBoardNotificationRule> getMessageBoardNotificationRules() {
		return messageBoardNotificationRules;
	}

	/**
	 */
	public void setDraftDocumentDeletionRules(Set<DraftDocumentDeletionRule> draftDocumentDeletionRules) {
		this.draftDocumentDeletionRules = draftDocumentDeletionRules;
	}

	/**
	 */
	public Set<DraftDocumentDeletionRule> getDraftDocumentDeletionRules() {
		return draftDocumentDeletionRules;
	}

	/**
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands =  EnumUtil.convertFromEnum(brands);
	}

	/**
	 */
	public Set<Brand> getBrands() {
		return EnumUtil.convertToEnum(this.brands,Brand.class);
	}

	public Set<Integer> getBrandIds() {
		return this.brands;
	}

	/**
	 */
	public void setLevels(Set<Level> levels) {
		this.levels =  EnumUtil.convertFromEnum(levels);
	}

	/**
	 */
	public Set<Level> getLevels() {
		return EnumUtil.convertToEnum(this.levels, Level.class);
	}

	/**
	 */
	public Set<Integer> getLevelIds() {
		return this.levels;
	}

	/**
	 */
	public void setStandardTopics(Set<StandardTopic> standardTopics) {
		this.standardTopics = standardTopics;
	}

	/**
	 */
	public Set<StandardTopic> getStandardTopics() {
		return standardTopics;
	}

	/**
	 */
	@Audited("sharing rules")
	public void setSharingRules(Set<SharingRule> sharingRules) {
		this.sharingRules = sharingRules;
	}

	/**
	 */
	public Set<SharingRule> getSharingRules() {
		return sharingRules;
	}

	/**
	 */
	public void setSharingRulesSharedAsAtargetOrganisation(Set<SharingRule> sharingRulesSharedAsAtargetOrganisation) {
		this.sharingRulesSharedAsAtargetOrganisation = sharingRulesSharedAsAtargetOrganisation;
	}

	/**
	 */
	public Set<SharingRule> getSharingRulesSharedAsAtargetOrganisation() {
		return sharingRulesSharedAsAtargetOrganisation;
	}
	
	public String toString() {
		return this.name;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Organisation)){
			return false;
		}
		if(this.getId().equals(((Organisation)obj).getId())) {
			return true;
		}
		return false;
	}
}
