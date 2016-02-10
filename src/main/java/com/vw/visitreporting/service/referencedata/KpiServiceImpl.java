package com.vw.visitreporting.service.referencedata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.KpiDao;
import com.vw.visitreporting.entity.referencedata.DynamicKpiBrand;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.KpiBrand;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;
import com.vw.visitreporting.service.AbstractVRService;

@Service
@Transactional
public class KpiServiceImpl extends AbstractVRService<Kpi> implements KpiService{

	private KpiDao kpiDao;
	
	@Autowired
	public void setKpiDao(KpiDao kpiDao){
		this.kpiDao = kpiDao;
	}
	
	@Override
	public VRDao<Kpi> getTDao() {
		return kpiDao;
	}
	
	@Override
	public Kpi loadDynamicEntities(Kpi kpi) {
		
		/*
		 * Set Transient Brands
		 */		
		Set<KpiBrand> kpiBrands = kpi.getKpiBrands();		
    	if(kpiBrands != null && !kpiBrands.isEmpty()){
    		
    		List<Brand> brands = new ArrayList<Brand>();
        	for (KpiBrand kpiBrand : kpiBrands) {
        		brands.add(kpiBrand.getBrand());
    		}
        	kpi.setBrands(brands.toArray(new Brand[0])); //NOPMD
    	}    
		
		
		/*
		 * Set Transient DynamicKpiB rands from KpiBrands
		 */		
		if(kpi.getKpiBrands() != null && !kpi.getKpiBrands().isEmpty()){
			
			List<DynamicKpiBrand> dynamicKpiBrands = new ArrayList<DynamicKpiBrand>();
			for (KpiBrand kpiBrand : kpi.getKpiBrands()) {
				
				DynamicKpiBrand dynamicKpiBrand = new DynamicKpiBrand();
				dynamicKpiBrand.setKpiBrandId(kpiBrand.getId());
		    	dynamicKpiBrand.setBenchMark(kpiBrand.getBenchmarkFigure());
		    	dynamicKpiBrand.setBrandId(kpiBrand.getBrand().getId());	
		    	dynamicKpiBrands.add(dynamicKpiBrand);
			}
	    	kpi.setDynamicKpiBrands(dynamicKpiBrands);
		}
		
		return kpi;
	}
	
	private KpiBrand getBrandFromBrandId(Set<KpiBrand> brands, int brandId){
						
		if(brands != null){
			for (KpiBrand kpiBrand : brands) {
				if(kpiBrand.getBrand().getId() == brandId){
					return kpiBrand;
				}
			}
		}
		return null;
	}
	
	private List<DynamicKpiBrand> deatchDynamicKpiBrands(List<DynamicKpiBrand> dynamicKpiBrands){
		
		List<DynamicKpiBrand> dynBrands = new ArrayList<DynamicKpiBrand>();
		for (DynamicKpiBrand dynamicKpiBrand : dynamicKpiBrands) {			
			if(dynamicKpiBrand.getBrandId() != null){
				dynBrands.add(dynamicKpiBrand);
			}
		}
		return dynBrands;
	}
	
	public Kpi associateKpiBrands(Kpi entity){
		
		/*
    	 * Logic to Associate kpiBrands to kpi starts here
    	 */		
		List<DynamicKpiBrand> dynamicKpiBrands = deatchDynamicKpiBrands(entity.getDynamicKpiBrands());
		Set<KpiBrand> kpiBrands = entity.getKpiBrands();
		Set<KpiBrand> kpiUpdBrands = new LinkedHashSet<KpiBrand>();
		kpiBrands = (kpiBrands == null)? new LinkedHashSet<KpiBrand>() : kpiBrands;
		
		if(dynamicKpiBrands != null && !dynamicKpiBrands.isEmpty()){
			
			for (DynamicKpiBrand dynKpiBrand : dynamicKpiBrands) {
				
				if (dynKpiBrand.getBrandId() != null) {
					int brandId = dynKpiBrand.getBrandId();
					//dyn brand id is available in the kpiBrand id's list. So, it is a update case for bench mark
					KpiBrand kBrand = getBrandFromBrandId(kpiBrands, brandId);
					//User added new kpiBrand in the jsp
					if (kBrand == null) {
						kBrand = new KpiBrand();
						kBrand.setBrand(EnumUtil.getEnumObject(brandId,	Brand.class));
						kBrand.setBenchmarkFigure(dynKpiBrand.getBenchMark());
						kBrand.setKpi(entity);
						kpiBrands.add(kBrand);
					}//User updated/no change case  for kpiBrand in the jsp 
					else {
						kBrand.setBenchmarkFigure(dynKpiBrand.getBenchMark());
					}
				}					
			}
			
			// This for loop will caters delete kpiBrands associated to the parent kpi
    		if(kpiBrands != null && !kpiBrands.isEmpty() && dynamicKpiBrands.size() != kpiBrands.size()){     			
    			
				for (KpiBrand kpiBrand : kpiBrands) {					
					
					for (DynamicKpiBrand dynKpiBrand : dynamicKpiBrands) {						
						if(dynKpiBrand.getBrandId() == kpiBrand.getBrand().getId()){
							kpiUpdBrands.add(kpiBrand);
						}						
					}					
				}    			
    		}
		}
		
		entity.setKpiBrands(kpiUpdBrands.isEmpty()?kpiBrands:kpiUpdBrands);    	
    	/*
		 * Logic to Associate kpiBrands to kpi ends here
		 */
		
		return entity;
	}
	
	@SuppressWarnings("unused")
	private Kpi associateKpiBrandsOld(Kpi entity){
		
		/*
    	 * Logic to Associate kpiBrands to kpi starts here
    	 */
    	List<Brand> brands = Arrays.asList(entity.getBrands());
		Set<KpiBrand> kpiBrands = entity.getKpiBrands();
		kpiBrands = (kpiBrands == null)? new LinkedHashSet<KpiBrand>() : kpiBrands;
		Set<KpiBrand> kpiUpdBrands = new LinkedHashSet<KpiBrand>();
    	
    	if(brands != null){
    		
    		// This for loop will caters add and update kpiBrands to the parent kpi 
    		for (Brand brand : brands) {
    			
    			boolean isBrandAlreadyAvailable = false;    			
    						
				for(KpiBrand kpiBrand : kpiBrands) {
            		//for setting the parent Kpi into child object
        			kpiBrand.setKpi(entity); 
            		
            		//For taking care of brands
        			if(brand.equals(kpiBrand.getBrand())){        				
            			//Check for the new Benchmark
        				kpiBrand.setBenchmarkFigure(78.89f);
        				isBrandAlreadyAvailable = true;
            		}  
            	}     	
    			
    			//Create a new KpiBrand and associate it with the kpi
    			if(!isBrandAlreadyAvailable){
    				KpiBrand newKpiBrand = new KpiBrand();
            		newKpiBrand.setBrand(brand);
            		newKpiBrand.setBenchmarkFigure(12.32f);
            		newKpiBrand.setKpi(entity); 
            		kpiBrands.add(newKpiBrand);
    			}
    		}
    		
    		// This for loop will caters delete kpiBrands associated to the parent kpi
    		if(entity.getBrands() != null && entity.getBrands().length > 0 && kpiBrands != null && 
    				!kpiBrands.isEmpty() && entity.getBrands().length != kpiBrands.size()){     			
    			
				for (KpiBrand kpiBrand : kpiBrands) {
					if(brands.contains(kpiBrand.getBrand())) {
						kpiUpdBrands.add(kpiBrand);
					}
				}    			
    		}
    		entity.setKpiBrands(kpiUpdBrands.isEmpty()?kpiBrands:kpiUpdBrands);    		
    	}
    	/*
		 * Logic to Associate kpiBrands to kpi ends here
		 */
		
		return entity;
	}

}
