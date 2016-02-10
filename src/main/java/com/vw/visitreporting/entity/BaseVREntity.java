package com.vw.visitreporting.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * Defines an interface that all entities in this system should implement to
 * provide common features required by the system.
 */
public interface BaseVREntity {

	public Serializable getId();
	
	public void setCreatedBy(String createdBy);
	public String getCreatedBy();
	public void setCreatedDate(Date createdDate);
	public Date getCreatedDate();
	
	public void setModifiedBy(String modifiedBy);
	public String getModifiedBy();
	public void setModifiedDate(Date modifiedDate);
	public Date getModifiedDate();
}
