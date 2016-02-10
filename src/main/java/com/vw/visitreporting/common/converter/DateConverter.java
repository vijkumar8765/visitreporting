package com.vw.visitreporting.common.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.springframework.core.convert.converter.Converter;


/**
 * Provides a converter from String to Date to be used by the Spring conversion API.
 * The String value must be in the format yyyy-MM-dd. 
 */
public class DateConverter implements Converter<String, Date> {

	private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() { 
			return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); 
		} 	
	};

	public Date convert(String source) {
		if(source == null || source.trim().equals("")){
			return null;
		}
		try {
			return DATE_FORMAT.get().parse(source);
		} catch(ParseException err) {
			throw new IllegalArgumentException("Invalid date "+source+": "+err.getMessage(), err);
		}
	}
}
