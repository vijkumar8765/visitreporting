package com.vw.visitreporting.common.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Level;

public final class VRCommonValidator {

	private VRCommonValidator(){
	}

    /**
	 * Below method checks if the supplied Organisation - Brand(s) combination is valid or not. 
	 * It does so by comparing the supplied brand value(s) against the Organisation's brand value(s).
	 * 
	 * @return boolean value
	 */
	public static boolean isOrgBrandCombinationValid(Organisation org, Set<Brand> brands){
		int temp = 0;
		List<Brand> orgBrands = new ArrayList<Brand>(org.getBrands());
		List<Brand> suppliedBrands = new ArrayList<Brand>(brands);
		
		for (Brand brand : suppliedBrands) {
			if(orgBrands.contains(brand)){
				temp++;
			}
		}
		
		if(temp == suppliedBrands.size()){
			return true;
		}
		
		return false;
	}
	
	
    /**
	 * Below method checks if the supplied Organisation - Level combination is valid or not. 
	 * It does so by comparing the supplied Level value against the Organisation's Level value(s).
	 * 
	 * @return boolean value
	 */
	public static boolean isOrgLevelCombinationValid(Organisation org, Level level){
		List<Level> orgLevels = new ArrayList<Level>(org.getLevels());
		if(orgLevels.contains(level)){
			return true;
		}
		return false;
	}

}
