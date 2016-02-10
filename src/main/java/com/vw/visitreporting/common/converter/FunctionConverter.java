package com.vw.visitreporting.common.converter;

import org.springframework.core.convert.converter.Converter;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Provides a converter from String to the Function enum to be used by the Spring conversion API.
 * The String value must be either the integer id property of the function or the enum name. 
 */
public class FunctionConverter implements Converter<String, Function> {

	public Function convert(String source) {
		int intValue;
		try{
			intValue = Integer.parseInt(source);
		}
		catch(NumberFormatException nfe){
			return Function.valueOf(source);
		}
		return EnumUtil.getEnumObject(intValue, Function.class);
	}
}
