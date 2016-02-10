package com.vw.visitreporting.entity.referencedata;

public class DynamicKpiBusinessRule {
	
	private Long kpiBusinessRuleId;
	private Integer brandId;
	private String businessRuleOpertor;
	private int actualTotal;	
	private int varianceColorId;
	
	public Long getKpiBusinessRuleId() {
		return kpiBusinessRuleId;
	}
	public void setKpiBusinessRuleId(Long kpiBusinessRuleId) {
		this.kpiBusinessRuleId = kpiBusinessRuleId;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getBusinessRuleOpertor() {
		return businessRuleOpertor;
	}
	public void setBusinessRuleOpertor(String businessRuleOpertor) {
		this.businessRuleOpertor = businessRuleOpertor;
	}
	public int getActualTotal() {
		return actualTotal;
	}
	public void setActualTotal(int actualTotal) {
		this.actualTotal = actualTotal;
	}
	public int getVarianceColorId() {
		return varianceColorId;
	}
	public void setVarianceColorId(int varianceColorId) {
		this.varianceColorId = varianceColorId;
	}
}
