package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.validation.constraints.NotNull;
import com.google.common.base.Preconditions;
import com.vw.visitreporting.entity.BaseVREntity;


/**
 * A set of key-performance-indicator targets for a dealership for a calendar year.
 */
@Entity
@Table(schema = "PUBLIC", name = "DEALER_KPI_TARGET")
public class DealerKpiTarget implements BaseVREntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "DEALERSHIP_ID", referencedColumnName = "ID", nullable = false) })
	@NotNull
	private Dealership dealership;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "KPI_ID", referencedColumnName = "ID", nullable = false) })
	@NotNull
	private Kpi kpi;

	@Column(name = "YEAR")
	@Basic(fetch = FetchType.EAGER)
	private Short year;

	@Column(name = "JANUARY_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float januaryScore;

	@Column(name = "FEBRUARY_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float februaryScore;

	@Column(name = "MARCH_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float marchScore;

	@Column(name = "APRIL_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float aprilScore;

	@Column(name = "MAY_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float mayScore;

	@Column(name = "JUNE_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float juneScore;

	@Column(name = "JULY_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float julyScore;

	@Column(name = "AUGUST_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float augustScore;

	@Column(name = "SEPTEMBER_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float septemberScore;

	@Column(name = "OCTOBER_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float octoberScore;

	@Column(name = "NOVEMBER_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float novemberScore;

	@Column(name = "DECEMBER_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float decemberScore;


	/**
	 * Constructor. Creates an empty instance of this entity.
	 */
	public DealerKpiTarget() {
	}
	

	/**
	 * Sets the database primary key for this entity.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the database primary key for this entity.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Gets the dealership that this target is for.	
	 */
	public Dealership getDealership() {
		return dealership;
	}

	/**
	 * Sets the dealership that this target is for.	
	 */
	public void setDealership(Dealership dealership) {
		this.dealership = dealership;
	}

	/**
	 * Gets the KPI that this target is for.
	 */
	public Kpi getKpi() {
		return kpi;
	}

	/**
	 * Sets the KPI that this target is for.
	 */
	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}

	/**
	 * Gets the year that these targets are for.
	 */
	public Short getYear() {
		return year;
	}

	/**
	 * Sets the year that these targets are for.
	 */
	public void setYear(Short year) {
		this.year = year;
	}

	/**
	 * Gets the target KPI score for this dealership and year in January.
	 */
	public Float getJanuaryScore() {
		return januaryScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in January.
	 */
	public void setJanuaryScore(Float januaryScore) {
		this.januaryScore = januaryScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in February.
	 */
	public Float getFebruaryScore() {
		return februaryScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in February.
	 */
	public void setFebruaryScore(Float februaryScore) {
		this.februaryScore = februaryScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in March.
	 */
	public Float getMarchScore() {
		return marchScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in March.
	 */
	public void setMarchScore(Float marchScore) {
		this.marchScore = marchScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in April.
	 */
	public Float getAprilScore() {
		return aprilScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in April.
	 */
	public void setAprilScore(Float aprilScore) {
		this.aprilScore = aprilScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in May.
	 */
	public Float getMayScore() {
		return mayScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in May.
	 */
	public void setMayScore(Float mayScore) {
		this.mayScore = mayScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in June.
	 */
	public Float getJuneScore() {
		return juneScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in June.
	 */
	public void setJuneScore(Float juneScore) {
		this.juneScore = juneScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in July.
	 */
	public Float getJulyScore() {
		return julyScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in July.
	 */
	public void setJulyScore(Float julyScore) {
		this.julyScore = julyScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in August.
	 */
	public Float getAugustScore() {
		return augustScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in August.
	 */
	public void setAugustScore(Float augustScore) {
		this.augustScore = augustScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in September.
	 */
	public Float getSeptemberScore() {
		return septemberScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in September.
	 */
	public void setSeptemberScore(Float septemberScore) {
		this.septemberScore = septemberScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in October.
	 */
	public Float getOctoberScore() {
		return octoberScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in October.
	 */
	public void setOctoberScore(Float octoberScore) {
		this.octoberScore = octoberScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in November.
	 */
	public Float getNovemberScore() {
		return novemberScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in November.
	 */
	public void setNovemberScore(Float novemberScore) {
		this.novemberScore = novemberScore;
	}

	/**
	 * Gets the target KPI score for this dealership and year in December.
	 */
	public Float getDecemberScore() {
		return decemberScore;
	}

	/**
	 * Sets the target KPI score for this dealership and year in December.
	 */
	public void setDecemberScore(Float decemberScore) {
		this.decemberScore = decemberScore;
	}
	
	
	/**
	 * Gets the target KPI score for this dealership, month and year.
	 * @param month - the zero-based index of the month to get the target of. Use Calendar constants for clarity.
	 */
	public Float getScore(int month) {
		Preconditions.checkArgument(month >= 1 && month <= 12, "Month must be between 1 and 12 inclusive");
		switch(month) {
			case Calendar.JANUARY:   return this.getJanuaryScore();
			case Calendar.FEBRUARY:  return this.getFebruaryScore();
			case Calendar.MARCH:     return this.getMarchScore();
			case Calendar.APRIL:     return this.getAprilScore();
			case Calendar.MAY:       return this.getMayScore();
			case Calendar.JUNE:      return this.getJuneScore();
			case Calendar.JULY:      return this.getJulyScore();
			case Calendar.AUGUST:    return this.getAugustScore();
			case Calendar.SEPTEMBER: return this.getSeptemberScore();
			case Calendar.OCTOBER:   return this.getOctoberScore();
			case Calendar.NOVEMBER:  return this.getNovemberScore();
			case Calendar.DECEMBER:  return this.getDecemberScore();
			default: assert false : "month must be between 1 and 12 inclusive"; break;
		}
		return null;	//will never get here
	}
	
	/**
	 * Gets the target KPI score for this dealership, at today's month/year.
	 * If that value is not available, null is returned.
	 */
	public Float getCurrentTarget() {
		Calendar cal = GregorianCalendar.getInstance();
		if(this.year.intValue() == cal.get(Calendar.YEAR)) {
			return getScore(cal.get(Calendar.MONTH));
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the most recent month/year to today's date that has a score for this dealership.
	 */
	public int getMostRecentTargetMonth() {
		int startMonth = Calendar.DECEMBER;
		Calendar cal = GregorianCalendar.getInstance();
		if(this.year.intValue() == cal.get(Calendar.YEAR)) {
			startMonth = cal.get(Calendar.MONTH);
		}
		for(int month=startMonth; month>=Calendar.JANUARY; month--) {
			Float score = this.getScore(month);
			if(score != null) {
				return month;
			}
		}
		return Calendar.JANUARY;
	}
	
	/**
	 * Gets the target KPI score for this dealership, at the most recent month/year to today's date.
	 */
	public Float getMostRecentTarget() {
		return this.getScore(this.getMostRecentTargetMonth());
	}
	

	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return "KPI Targets for KPI:"+this.kpi.getKpiDescription()+", Year:"+this.year;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof DealerKpiTarget)) {
			return false;
		}
		DealerKpiTarget equalCheck = (DealerKpiTarget) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}	


	public void setCreatedBy(String createdBy) {
	}
	public String getCreatedBy() {
		return null;
	}
	public void setCreatedDate(Date createdDate) {
	}
	public Date getCreatedDate() {
		return null;
	}
	public void setModifiedBy(String modifiedBy) {
	}
	public String getModifiedBy() {
		return null;
	}
	public void setModifiedDate(Date modifiedDate) {
	}
	public Date getModifiedDate() {
		return null;
	}
}
