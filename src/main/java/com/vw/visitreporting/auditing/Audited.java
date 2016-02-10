package com.vw.visitreporting.auditing;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


/**
 * Annotation to flag an entity or entity method as requiring auditing and to provide
 * a more user-friendly description of an entity for audit event descriptions.
 * 
 * NOTE: the auditing framework in this system imposes the following restrictions on audited entities:
 *  - audited properties should not be of primitive types (e.g. Integer should be used instead of int)
 *  - audited properties of type Boolean should have set* and get* methods, (not is* methods)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Audited {
	
	/**
	 * A user-friendly description of the entity for revision descriptions.
	 */
	String value();
}
