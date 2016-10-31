package com.smallprograms.utils;

import net.sf.json.util.PropertyFilter;

public class JSONNullPropertyFilter implements PropertyFilter {
	private JSONNullPropertyFilter(){}

	public boolean apply(Object target, String propName, Object propValue) {
		if (propValue == null || "".equals(propValue.toString().trim())) {
			return true;
		}
		return false;
	}
	
	private static JSONNullPropertyFilter instance;
	public static JSONNullPropertyFilter getInstance(){
		if (instance == null) {
			instance = new JSONNullPropertyFilter();
		}
		return instance;
	}
}
