package com.vw.visitreporting.common.converter;

import org.springframework.core.convert.converter.Converter;
import com.vw.visitreporting.web.form.OrganisationBrand;


/**
 * Provides a converter from String to OrganisationBrand to be used by the Spring conversion API.
 * The String value must be in the format <orgId>:<brandId> as required by the method OrganisationBrand.fromId(). 
 */
public class OrganisationBrandConverter implements Converter<String, OrganisationBrand> {

	public OrganisationBrand convert(String source) {
		return OrganisationBrand.fromId(source);
	}
}
