package com.vw.visitreporting.entity.referencedata.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vw.visitreporting.entity.referencedata.enums.Function;

public class FunctionManager {

	private static Map<String,Set<Function>> functionAndClassNameMap = createFunctionAndClassNameMap();

	public static Set<Function> getAllPossibleFunctionsForObjectType(
			String simpleClassName) {
		return functionAndClassNameMap.get(simpleClassName);
	}

	private static Map<String,Set<Function>> createFunctionAndClassNameMap() {
		Map<String,Set<Function>> functionAndClassNameMap =  new HashMap<String,Set<Function>>();
		EnumSet<Function> functions = EnumSet.allOf(Function.class);
		
		Set<Function> functionsForAClass = null;
		for(Function aFunction: functions){
			if(aFunction.getEntityClass() == null){
				continue; //can't store this
			}
			String className = aFunction.getEntityClass().getSimpleName();
			if(functionAndClassNameMap.containsKey(className)){
				functionsForAClass = functionAndClassNameMap.get(className);
			}
			else{
				functionsForAClass = new HashSet<Function>();
			}
			functionsForAClass.add(aFunction);
			functionAndClassNameMap.put(className, functionsForAClass);
		}
		
		return functionAndClassNameMap;
	}
	
	public static boolean doesClassHaveAnyAssociatedFunction(String simpleClassName){
		if(getAllPossibleFunctionsForObjectType(simpleClassName).size() == 0){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static Function getFunctionForUserActionAndClass(String functionPrefix, Class entity){
		//we are following a pattern on the name of a function. It depends of the user's action type (create,edit,list,view,delete)
		//and the name of the entity
		
		//this method helps us find the function object based on the user's action and the entity
		//if there is a function following pattern: functionPrefix_entityName then use it
		//if not, if there is a function following pattern: MAINTAIN_entityName then use it
		//else throw IllegalArgumentException 
		
		String functionName = functionPrefix + "_" + entity.getSimpleName().toUpperCase();
		Function theFunction = null;
		try{
			theFunction = Function.valueOf(functionName);
		}
		catch(IllegalArgumentException iae){
			functionName = "MAINTAIN_" + entity.getSimpleName().toUpperCase();
			//if the function is still not found IllegalArgumentException will be thrown from here
			theFunction = Function.valueOf(functionName);
		}
		
		return theFunction;
	}
	
}
