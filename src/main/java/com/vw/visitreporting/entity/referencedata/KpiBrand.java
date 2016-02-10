package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
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
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * An association between a KPI and a brand, with accompanying properties
 * such as a benchmark figure for the brand.
 */
@Entity
@Table(schema = "PUBLIC", name = "KPI_BRAND")
public class KpiBrand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumns({ @JoinColumn(name = "KPI_ID", referencedColumnName = "ID", nullable = false) })
	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	@JoinColumns({ @JoinColumn(name = "KPI_ID", referencedColumnName = "ID", nullable = false) })
	private Kpi kpi;

	@Column(name = "BRAND_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer brandId;

	@Column(name = "BENCHMARK_FIGURE")
	@Basic(fetch = FetchType.EAGER)
	private Float benchmarkFigure;


	/**
	 * Constructor. Creates an empty instance of this entity.
	 */
	public KpiBrand() {
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
	 * Gets the KPI that this association involves.
	 */
	public Kpi getKpi() {
		return kpi;
	}

	/**
	 * Sets the KPI that this association involves.
	 */
	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}

	/**
	 * Gets the brand that this association involves.
	 */
	public void setBrand(Brand brand) {
		this.brandId = brand.getId();
	}

	/**
	 * Sets the brand that this association involves.
	 */
	public Brand getBrand() {
		return EnumUtil.getEnumObject(this.brandId, Brand.class);
	}

	/**
	 * Gets the benchmark KPI value set for this KPI and brand.
	 */
	public Float getBenchmarkFigure() {
		return benchmarkFigure;
	}

	/**
	 * Sets the benchmark KPI value set for this KPI and brand.
	 */
	public void setBenchmarkFigure(Float benchmarkFigure) {
		this.benchmarkFigure = benchmarkFigure;
	}
	
	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		//return this.getBrand().getShortName()+" benchmark for KPI: "+this.kpi.getKpiDescription()+" = "+this.benchmarkFigure+(this.kpi.isPercentage() ? "%" : "");
		return "";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =  (id == null) ?  super.hashCode() : (int) (prime * result + id.hashCode());
		return result;
	} 

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof KpiBrand)) {
			return false;
		}
		KpiBrand equalCheck = (KpiBrand) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}	
}
