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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.MessageBoardNotificationRuleItemType;
import com.vw.visitreporting.entity.referencedata.enums.MessageBoardNotificationRuleStatus;
import com.vw.visitreporting.entity.referencedata.enums.MessageBoardNotificationRuleType;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A rule that specifies when new message board notifications should be generated.
 */
@Entity
@Table(schema = "PUBLIC", name = "MESSAGE_BOARD_NOTIFICATION_RULE")
public class MessageBoardNotificationRule implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NOTIFICATION_TYPE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer notificationType;

	@Column(name = "BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer brand;

	@Column(name = "ITEM_TYPE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer itemType;

	@Column(name = "BUSINESS_RULE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String businessRule;

	@Column(name = "NOTIFICATION_DESCRIPTION", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String notificationDescription;
	
	@Column(name = "STATUS")
	@Basic(fetch = FetchType.EAGER)
	private Integer status;
	
	@Column(name = "VISIBILITY_TIMING_RULE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String visibilityTimingRule;

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
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID") })
	private Organisation organisation;

	@ElementCollection
	@CollectionTable(name="MESSAGE_BOARD_NOTIFICATION_RULE_RECIPIENT", joinColumns=@JoinColumn(name="MESSAGE_BOARD_NOTIFICATION_RULE_ID"))
	@Column(name="RECIPIENT_USER_ID")
	private Set<String> messageBoardNotificationRuleRecipients;

	
	/**
	 */
	public MessageBoardNotificationRule() {
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
	public void setNotificationType(MessageBoardNotificationRuleType notificationType) {
		this.notificationType = notificationType.getId();
	}

	/**
	 */
	public MessageBoardNotificationRuleType getNotificationType() {
		return EnumUtil.getEnumObject(this.notificationType,MessageBoardNotificationRuleType.class);
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
	public void setItemType(MessageBoardNotificationRuleItemType itemType) {
		this.itemType = itemType.getId();
	}

	/**
	 */
	public MessageBoardNotificationRuleItemType getItemType() {
		return EnumUtil.getEnumObject(this.itemType, MessageBoardNotificationRuleItemType.class);
	}

	/**
	 */
	public void setBusinessRule(String businessRule) {
		this.businessRule = businessRule;
	}

	/**
	 */
	public String getBusinessRule() {
		return this.businessRule;
	}

	/**
	 */
	public void setNotificationDescription(String notificationDescription) {
		this.notificationDescription = notificationDescription;
	}

	/**
	 */
	public String getNotificationDescription() {
		return this.notificationDescription;
	}

	/**
	 */
	public void setStatus(MessageBoardNotificationRuleStatus status) {
		this.status = status.getId();
	}

	/**
	 */
	public MessageBoardNotificationRuleStatus getStatus() {
		return EnumUtil.getEnumObject(this.status, MessageBoardNotificationRuleStatus.class);
	}

	/**
	 */
	public void setVisibilityTimingRule(String visibilityTimingRule) {
		this.visibilityTimingRule = visibilityTimingRule;
	}

	/**
	 */
	public String getVisibilityTimingRule() {
		return this.visibilityTimingRule;
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
	public void setMessageBoardNotificationRuleRecipients(Set<String> messageBoardNotificationRuleRecipients) {
		this.messageBoardNotificationRuleRecipients = messageBoardNotificationRuleRecipients;
	}

	/**
	 */
	public Set<String> getMessageBoardNotificationRuleRecipients() {
		return messageBoardNotificationRuleRecipients;
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
		if (!(obj instanceof MessageBoardNotificationRule)){
			return false;
		}
		if(this.getId().equals(((MessageBoardNotificationRule)obj).getId())){
			return true;
		}
		return false;
	}

}
