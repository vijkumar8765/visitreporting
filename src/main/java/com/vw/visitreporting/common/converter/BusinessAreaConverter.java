package com.vw.visitreporting.common.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.service.referencedata.BusinessAreaService;


/**
 * Provides a converter from String to the BusinessArea to be used by the Spring conversion API.
 * The String value must be String id property of the BusinessArea. 
 */
public class BusinessAreaConverter implements Converter<String, BusinessArea> {
	
	@Autowired
	private BusinessAreaService businessAreaService;

	public BusinessArea convert(String source) {		
		return businessAreaService.getById(Integer.valueOf(source));
	}
}
