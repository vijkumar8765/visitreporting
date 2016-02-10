package com.vw.visitreporting.entity.system;

import java.io.Serializable;
import java.util.Date;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vw.visitreporting.common.exception.ExceptionCode;
import com.vw.visitreporting.common.exception.VisitReportingException;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Stores details about changes to entities or entity properties that must be audited.
 * Instance of this class store information about a single change to an entity or property.
 */
@Entity
@Table(schema = "PUBLIC", name = "AUDIT_RECORD")
public class AuditRecord implements BaseVREntity {

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ENTITY_CLASS", length = 250, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String entityClass;
	
	@Column(name = "ENTITY_ID", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String entityId;

	@Column(name = "EVENT_TYPE")
	@Basic(fetch = FetchType.EAGER)
	private Integer eventType;
	
	@Column(name = "PROPERTY_NAME", length = 50, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private String propertyName;
	
	@Column(name = "USER_ID", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String userId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_TIMESTAMP")
	@Basic(fetch = FetchType.EAGER)
	private Date eventTimestamp;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="AUDIT_RECORD_DEALERSHIPS",
		joinColumns=@JoinColumn(name="AUDIT_RECORD_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="DEALERSHIP_ID", referencedColumnName="ID")
	)
	private Collection<Dealership> dealersInvolved;
	
	
	/**
	 * Constructor. Sets all properties to null.
	 */
	public AuditRecord() {
	}
	

	/**
	 * Return a string representation of this object.
	 */
	public String toString() {
		String subject = this.getEntityClass().getSimpleName();
		if(getEventType() == AuditType.MOD) {
			subject += "." + propertyName;
		}
		return "On "+eventTimestamp+" user "+userId+" "+getEventType()+" "+subject+" id="+entityId;
	}
	
	
	/**
	 * Gets the auto-generate primary key of this entity.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the auto-generate primary key of this entity.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the class type of the entity that was modified in this auditing event.
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends BaseVREntity> getEntityClass() {
		try {
			return (Class<? extends BaseVREntity>)Class.forName(BaseVREntity.class.getPackage().getName() + "." + entityClass);
		} catch(ClassNotFoundException err) {
			throw new VisitReportingException(ExceptionCode.AUDIT_RECORD_ENTITY_CLASS_NOT_FOUND, "Entity class "+entityClass+" was not recognised.", err);
		}
	}

	/**
	 * Sets the class type of the entity that was modified in this auditing event.
	 */
	public void setEntityClass(Class<? extends BaseVREntity> entityClass) {
		this.entityClass = getEntityClassName(entityClass);
	}

	/**
	 * Gets the full name of the entity class, removing the base entity package that is common to all entities
	 */
	public static String getEntityClassName(Class<? extends BaseVREntity> entityClass) {
		return entityClass.getName().replace(BaseVREntity.class.getPackage().getName()+".", "");
	}

	/**
	 * Gets the ID of the entity that was modified in this auditing event.
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * Sets the ID of the entity that was modified in this auditing event.
	 */
	public void setEntityId(Serializable entityId) {
		this.entityId = entityId.toString();
	}

	/**
	 * Gets the type of audit event that this instance describes, e.g. entity creation of property modification.
	 */
	public AuditType getEventType() {
		return EnumUtil.getEnumObject(eventType, AuditType.class);
	}

	/**
	 * Sets the type of audit event that this instance describes, e.g. entity creation of property modification.
	 */
	public void setEventType(AuditType eventType) {
		this.eventType = eventType.getId();
	}

	/**
	 * Gets the name of the property that was modified in this auditing event.
	 * This can be null if this audit event is for an entity creation or deletion.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the name of the property that was modified in this auditing event.
	 * This can be null if this audit event is for an entity creation or deletion.
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Gets the user id (primary key to USER table) of the user that invoke this change.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id (primary key to USER table) of the user that invoke this change.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the date and time that this event occurred.
	 */
	public Date getEventTimestamp() {
		return eventTimestamp;
	}

	/**
	 * Sets the date and time that this event occurred.
	 */
	public void setEventTimestamp(Date eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	/**
	 * Gets the dealerships that were related to the entity that changed in this event.
	 */
	public Collection<Dealership> getDealersInvolved() {
		return dealersInvolved;
	}

	/**
	 * Sets the dealerships that were related to the entity that changed in this event.
	 */
	public void setDealersInvolved(Collection<Dealership> dealersInvolved) {
		this.dealersInvolved = dealersInvolved;
	}


	public void setCreatedBy(String createdBy) {
		//use setUserId() to change event creator
	}
	public String getCreatedBy() {
		return userId;
	}
	public void setCreatedDate(Date createdDate) {
		//use setEventTimestamp() to change event timestamp
	}
	public Date getCreatedDate() {
		return eventTimestamp;
	}
	public void setModifiedBy(String modifiedBy) {
		//use setUserId() to change event creator
	}
	public String getModifiedBy() {
		return userId;
	}
	public void setModifiedDate(Date modifiedDate) {
		//use setEventTimestamp() to change event timestamp
	}
	public Date getModifiedDate() {
		return eventTimestamp;
	}
}
