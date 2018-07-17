package com.piles.common.util;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.*;

public class EnumUtil {
	
	public static <T> Map<String, String> parseEumn(Class<T> ref){
		Map<String, String> map = new LinkedHashMap<String, String>() ;
		if(ref.isEnum()){
			T[] ts = ref.getEnumConstants() ; 
			for(T t : ts){
				String text = getInvokeValue(t, "getLabel") ; 
				Enum<?> tempEnum = (Enum<?>) t ;
				if(text == null){
					text = tempEnum.name() ;
				}
				String code = t.toString() ; 
				if(code == null){
					continue;
				}
				map.put(code , text ) ; 
			}
		}
		return map ;
	}
	
	
	public static <T> List<Map<String,String>> parseEumnList(Class<T> ref){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(ref.isEnum()){
			T[] ts = ref.getEnumConstants() ; 
			for(T t : ts){
				Map<String, String> map = new HashMap<String, String>() ;
				String text = getInvokeValue(t, "getLabel") ; 
				Enum<?> tempEnum = (Enum<?>) t ;
				if(text == null){
					text = tempEnum.name() ;
				}
				String code = t.toString() ; 
				if(code == null){
					continue;
				}
				map.put("code", code);
				map.put("value", text);
				list.add(map);
			}
		}
		return list ;
	}
	
	static <T> String getInvokeValue(T t , String methodName){
		try {
			Method method = MethodUtils.getAccessibleMethod( t.getClass() , methodName); 
			Object obj = method.invoke( t );
			return  obj == null?null:String.valueOf(obj);
		} catch (Exception e) {
			return null ;
		}
	}
}
