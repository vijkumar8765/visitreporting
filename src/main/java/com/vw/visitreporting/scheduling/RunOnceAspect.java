package com.vw.visitreporting.scheduling;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vw.visitreporting.entity.system.TaskExecution;


/**
 * Provides the functionality to support the RunOnce annotation, ensuring that scheduled
 * tasks only run on one node in a cluster on each scheduled execution time.
 */
@Aspect
@Component
public class RunOnceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(RunOnceAspect.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy-HH_mm", Locale.getDefault());

	@Autowired
	private SessionFactory sessionFactory;

	
	/**
	 * Wraps the invocation of all methods annotated with the RunOnce and Scheduled
	 * annotations to ensure that, if this application is deployed to a cluster, the
	 * method is only executed on one node at each scheduled execution time.  
	 */
	@Around(value="@annotation(runOnce)")
	public void checkIfRunBefore(ProceedingJoinPoint pjp, RunOnce runOnce) throws Throwable {

		//get the Scheduled annotation details from the method
		Scheduled schedAnnotation = null;
		for(Method m : pjp.getSignature().getDeclaringType().getMethods()) {
			
			//find the Method that this advice is wrapping
			RunOnce other = m.getAnnotation(RunOnce.class);
			if(other != null && other.taskName().equals(runOnce.taskName())) {

				schedAnnotation = m.getAnnotation(Scheduled.class);
				break;
			}
		}
		
		//if the method the method was not annotated with @Scheduled then just run the method
		if(schedAnnotation == null) {
			pjp.proceed();
			return;
		}
		
		//get a consistent identifier for the time that this task was scheduled to run on this occasion
		String time = null;
		synchronized(DATE_FORMAT) {
			time = DATE_FORMAT.format(new Date()) + schedAnnotation.cron();
		}
		String taskName = runOnce.taskName();

		//try to add a row to the database for this task execution occasion
		TaskExecution dbRow = new TaskExecution(time, taskName);
		try {
			Session session = SessionFactoryUtils.getSession(this.sessionFactory, true);
			session.persist(dbRow);
			session.flush();
			
			//if persist succeeds (row has not been inserted before) then proceed with method
			LOG.info("[SCHEDULED-RUN] taskName="+taskName+" time="+time);
			try {
				
				//run the method
				pjp.proceed();
				
			} catch(Throwable err) {
				//log this task failure
				LOG.error("[SCHEDULED-ERROR] taskName="+taskName+" time="+time+" err="+err.getMessage(), err);
			}

		} catch(HibernateException err) {

			//if row inserting failed due to referential integrity (uniqueness) constraint,
			//then assume that this task has been executed an another node and does not need to
			//be executed again.
			LOG.info("[SCHEDULED-SKIP] taskName="+taskName+" time="+time);
		}
	}
}
