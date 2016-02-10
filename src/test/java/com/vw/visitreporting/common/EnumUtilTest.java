package com.vw.visitreporting.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Unit tests the EnumUtil class.
 */
public class EnumUtilTest {

	@Test
	public void testConvertToEnum() {
		
		//Create a set of integers
		Set<Integer> setOfIntegers  = new HashSet<Integer>();
		setOfIntegers.add(3);
		setOfIntegers.add(5);
		
		Set<Brand> brands = EnumUtil.convertToEnum(setOfIntegers,Brand.class);
		//Its difficult to do a position check on a Set
		assertThat(brands.size(), is(equalTo(2)));
		assertThat(brands, hasItems(Brand.SKODA, Brand.VWCV));
	}	
	
	@Test
	public void testConvertFromEnum() {
		Set<Brand> setOfBrands = new HashSet<Brand>();
		setOfBrands.add(Brand.SEAT);
		setOfBrands.add(Brand.SKODA);
		Set<Integer> integers = EnumUtil.convertFromEnum(setOfBrands);
		
		assertThat(integers, hasItems(2, 3));
	}
	
	@Test
	public void testGetEnumObject() {
		Brand brand = EnumUtil.getEnumObject(2, Brand.class);
		assertThat(brand, is(equalTo(Brand.SEAT)));
	}
}


