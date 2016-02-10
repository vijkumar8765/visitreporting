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
import javax.validation.constraints.NotNull;

import com.vw.visitreporting.entity.BaseVREntity;


/**
 * A set of key-performance-indicator measurements for a dealership and related
 * areas/regions/etc at a point in time.
 */
@Entity
@Table(schema = "PUBLIC", name = "DEALER_KPI_ACTUAL")
public class DealerKpiActual implements BaseVREntity, Serializable {
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

	@Column(name = "SCORE_MONTH")
	@Basic(fetch = FetchType.EAGER)
	private Byte scoreMonth;

	@Column(name = "SCORE_YEAR")
	@Basic(fetch = FetchType.EAGER)
	private Short scoreYear;

	@Column(name = "DEALER_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float dealerScore;

	@Column(name = "VARIANCE_TOTAL")
	@Basic(fetch = FetchType.EAGER)
	private Float varianceScore;

	@Column(name = "AREA_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float areaScore;

	@Column(name = "REGIONAL_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float regionalScore;

	@Column(name = "NATIONAL_ACTUAL_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float nationalActualScore;

	@Column(name = "NATIONAL_LEAGUE_POSITION")
	@Basic(fetch = FetchType.EAGER)
	private Short nationalLeaguePosition;
	
	@Column(name = "TOTAL_ACTUAL_SCORE")
	@Basic(fetch = FetchType.EAGER)
	private Float totalActualScore;


	/**
	 * Constructor. Creates an empty instance of this entity.
	 */
	public DealerKpiActual() {
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
	 * Gets the dealership that this score is for.	
	 */
	public Dealership getDealership() {
		return dealership;
	}

	/**
	 * Sets the dealership that this score is for.	
	 */
	public void setDealership(Dealership dealership) {
		this.dealership = dealership;
	}

	/**
	 * Gets the KPI that this score is based on.
	 */
	public Kpi getKpi() {
		return kpi;
	}

	/**
	 * Sets the KPI that this score is based on.
	 */
	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}

	/**
	 * Gets the month of the date that this kpi measurement was calculated.
	 */
	public Byte getScoreMonth() {
		return scoreMonth;
	}

	/**
	 * Sets the month of the date that this kpi measurement was calculated.
	 */
	public void setScoreMonth(Byte scoreMonth) {
		this.scoreMonth = scoreMonth;
	}

	/**
	 * Gets the year of the date that this kpi measurement was calculated.
	 */
	public Short getScoreYear() {
		return scoreYear;
	}

	/**
	 * Sets the year of the date that this kpi measurement was calculated.
	 */
	public void setScoreYear(Short scoreYear) {
		this.scoreYear = scoreYear;
	}

	/**
	 * Gets the KPI score obtained by the dealership at this time.
	 */
	public Float getDealerScore() {
		return dealerScore;
	}

	/**
	 * Sets the KPI score obtained by the dealership at this time.
	 */
	public void setDealerScore(Float dealerScore) {
		this.dealerScore = dealerScore;
	}

	/**
	 * Gets the variance of the KPI score obtained by the dealership at this time.
	 */
	public Float getVarianceScore() {
		return varianceScore;
	}

	/**
	 * Sets the variance of the KPI score obtained by the dealership at this time.
	 */
	public void setVarianceScore(Float varianceScore) {
		this.varianceScore = varianceScore;
	}

	/**
	 * Gets the average KPI score obtained by the dealership's area at this time.
	 */
	public Float getAreaScore() {
		return areaScore;
	}

	/**
	 * Sets the average KPI score obtained by the dealership's area at this time.
	 */
	public void setAreaScore(Float areaScore) {
		this.areaScore = areaScore;
	}

	/**
	 * Gets the average KPI score obtained by the dealership's region at this time.
	 */
	public Float getRegionalScore() {
		return regionalScore;
	}

	/**
	 * Sets the average KPI score obtained by the dealership's region at this time.
	 */
	public void setRegionalScore(Float regionalScore) {
		this.regionalScore = regionalScore;
	}

	/**
	 * Gets the average KPI score obtained accross the UK at this time.
	 */
	public Float getNationalActualScore() {
		return nationalActualScore;
	}

	/**
	 * Sets the average KPI score obtained accross the UK at this time.
	 */
	public void setNationalActualScore(Float nationalActualScore) {
		this.nationalActualScore = nationalActualScore;
	}

	/**
	 * Gets the rank position of this dealership for this KPI accross the UK. 
	 */
	public Short getNationalLeaguePosition() {
		return nationalLeaguePosition;
	}

	/**
	 * Sets the rank position of this dealership for this KPI accross the UK. 
	 */
	public void setNationalLeaguePosition(Short nationalLeaguePosition) {
		this.nationalLeaguePosition = nationalLeaguePosition;
	}

	/**
	 * Gets sum total of KPI scores accross all KPIs for this dealership at this poinr in time.
	 */
	public Float getTotalActualScore() {
		return totalActualScore;
	}

	/**
	 * Sets sum total of KPI scores accross all KPIs for this dealership at this poinr in time.
	 */
	public void setTotalActualScore(Float totalActualScore) {
		this.totalActualScore = totalActualScore;
	}

	
	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return "KPI Scores for KPI:"+this.kpi.getKpiDescription()+", Date:"+this.scoreMonth+"/"+this.scoreYear;
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
		} else if (!(obj instanceof DealerKpiActual)) {
			return false;
		}
		DealerKpiActual equalCheck = (DealerKpiActual) obj;
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
