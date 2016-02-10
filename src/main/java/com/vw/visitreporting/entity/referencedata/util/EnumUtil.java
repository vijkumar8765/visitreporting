package com.vw.visitreporting.entity.referencedata.util;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.vw.visitreporting.entity.referencedata.enums.EnumEntity;

//This Class should ideally be able to work on collections i.e.
//Return a list back for a list and set back for a set
public final class EnumUtil {

	private EnumUtil(){
		
	}
	/**
	 * @param <T>
	 */
	public static<T extends EnumEntity> Set<Integer> convertFromEnum(Set<T> enumEntities) {
		Set<Integer> setOfIntegers = new HashSet<Integer>();
		for(T t: enumEntities){
			setOfIntegers.add(t.getId());
		}
		return setOfIntegers;
	}

	
	@SuppressWarnings("unchecked")
	public static<T extends Enum<T>> EnumSet<T> convertToEnum(Set<Integer> setOfIntegers, Class<T> enumClass) {
				
		EnumSet<T> setOfEnumEntities = EnumSet.noneOf(enumClass);
		
		if(null == setOfIntegers) {
			return setOfEnumEntities;
		}
		Class<? extends EnumEntity> entityClass = (Class<? extends EnumEntity>)enumClass;
				
		for(Integer integer: setOfIntegers){
			T enumVal = (T)getEnumObject(integer, entityClass);
			setOfEnumEntities.add(enumVal);
				
		}
		return setOfEnumEntities;
	}
	
	@SuppressWarnings("unchecked")
	public static<T extends EnumEntity> T getEnumObject(Integer integer, Class<T> entityClass) {		
		
			Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>)entityClass;
		
			for(Object enumObject: enumClass.getEnumConstants()){
				if(((EnumEntity)enumObject).getId().equals(integer)){
					return (T)enumObject;
				}
			}
			//If I come here then there is a problem - the integer has no representation in the enum
			//better throw an exception
			return null;
		}
	
}
