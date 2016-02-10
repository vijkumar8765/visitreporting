package com.vw.visitreporting.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import com.vw.visitreporting.entity.referencedata.enums.EnumEntity;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Provides a converter from String to EnumEntity sub-classes to be used by the Spring conversion API.
 * The String value must be equal to the id property of the target enum entity. 
 */
public final class EnumEntityConverter implements ConverterFactory<String, EnumEntity> {

	public <T extends EnumEntity> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEnumEntityConverter<T>(targetType);
	}

	private final class StringToEnumEntityConverter<T extends EnumEntity> implements Converter<String, T> {

		private final Class<T> enumType;

		public StringToEnumEntityConverter(Class<T> enumType) {
			this.enumType = enumType;
		}
		
		public T convert(String source) {
			return (T)EnumUtil.getEnumObject(Integer.valueOf(source), enumType);
		}
		
	}
}
