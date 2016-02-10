package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
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
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.DocumentType;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A Rule that determines when agendas or visit reports in draft status should be discarded.
 */
@Entity
@Table(schema = "PUBLIC", name = "DRAFT_DOCUMENT_DELETION_RULE")
public class DraftDocumentDeletionRule implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer brand;
	
	@Column(name = "DOCUMENT_TYPE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer documentType;

	@Column(name = "DELETION_DAYS", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Short deletionDays;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation organisation;

	
	/**
	 */
	public DraftDocumentDeletionRule() {
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
		this.brand = brand.getId();
	}

	/**
	 */
	public Brand getBrand() {
		return EnumUtil.getEnumObject(this.brand, Brand.class);
	}


	/**
	 */
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType.getId();
	}

	/**
	 */
	public DocumentType getDocumentType() {
		return EnumUtil.getEnumObject(this.documentType,DocumentType.class);
	}

	/**
	 */
	public void setDeletionDays(Short deletionDays) {
		this.deletionDays = deletionDays;
	}

	/**
	 */
	public Short getDeletionDays() {
		return this.deletionDays;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	/**
	 */
	/**
	 */
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if (obj == this){
			return true;
		}
		if (!(obj instanceof DraftDocumentDeletionRule)){
			return false;
		}
		if(this.getId().equals(((DraftDocumentDeletionRule)obj).getId())){
			return true;
		}
		return false;
	}

}
