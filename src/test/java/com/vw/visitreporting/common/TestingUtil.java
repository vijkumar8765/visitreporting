package com.vw.visitreporting.common;

import java.util.Collection;

import junit.framework.Assert;

import com.vw.visitreporting.entity.referencedata.enums.Function;

public class TestingUtil {

	public static void assertCollectionContainsFunctions(Collection<Function> functionsAllowedOnSeat, Function... functions ){
		
		if(functions.length != functionsAllowedOnSeat.size()){
			Assert.fail("Number of items in list and number of passed functions should be the same");
		}
		for(Function aFunction: functions){
			if (! functionsAllowedOnSeat.contains(aFunction)){
				Assert.fail("Function " + aFunction.toString() + "not found in the list");
			}
		}
		Assert.assertTrue(true);
	}
	
}
