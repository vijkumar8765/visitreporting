package com.vw.visitreporting.scheduling;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


/**
 * Annotation to be used in conjunction with the @Scheduled annotation to ensure that a task
 * scheduled on a cluster will only run on one node at each interval.
 * CRON expressions in the @Scheduled annotation should only be specified to the precision of minutes,
 * not seconds, as it is possible that the clocks on the cluster machines are not synchronised to this precision.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RunOnce {
	
	/**
	 * Use this annotation attribute to uniquely identify the task.
	 */
	String taskName();
}
