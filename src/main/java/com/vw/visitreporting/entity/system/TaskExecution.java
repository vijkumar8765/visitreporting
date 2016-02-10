package com.vw.visitreporting.entity.system;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.Entity;


/**
 *	Encapsulates the data required to uniquely identify a task execution occasion. 
 */
@Entity
public class TaskExecution {
	
	/**
	 * Encapsulate the fields of the composite primary key for Hibernate
	 */
	@Embeddable
	public class TaskExecutionPrimaryKey implements Serializable {
		private static final long serialVersionUID = 1L;

		private String time;
		private String taskName;

		/**
		 * Gets the time that a scheduled task is next scheduled to run, in the
		 * format: dd_MM_yyyy_HH_CRON
		 */
		public String getTime() {
			return time;
		}

		/**
		 * Sets the time that a scheduled task is next scheduled to run, in the
		 * format: dd_MM_yyyy_HH_CRON
		 */
		public void setTime(String time) {
			this.time = time;
		}

		/**
		 * Gets a unique identifier for the task that is scheduled to run.
		 */
		public String getTaskName() {
			return taskName;
		}

		/**
		 * Sets a unique identifier for the task that is scheduled to run.
		 */
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
	}
	
	
	@Id
	private final TaskExecutionPrimaryKey key;
	
	
	/**
	 * Constructor. Sets all properties to null.
	 */
	public TaskExecution() {
		this(null, null);
	}
	
	/**
	 * Constructor. Initialises all properties.
	 */
	public TaskExecution(String time, String taskName) {
		this.key = new TaskExecutionPrimaryKey();
		this.key.setTime(time);
		this.key.setTaskName(taskName);
	}

	/**
	 * Get the primary key (time + taskName) of this task execution.
	 */
	public TaskExecutionPrimaryKey getKey() {
		return key;
	}
}
