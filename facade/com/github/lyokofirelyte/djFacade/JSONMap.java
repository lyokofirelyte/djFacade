package com.github.lyokofirelyte.djFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.simple.JSONObject;

public class JSONMap extends JSONObject {

	private static final long serialVersionUID = System.currentTimeMillis();

	public char getChar(Object i){
		
		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Character){
				return (Character) get(toString(i));
			}
		}
		
		return 'n';
	}

	public String getStr(Object i){

		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof String){
				return (String) get(toString(i));
			}
			return get(toString(i)) + "";
		}
		return "none";
	}
	
	public int getInt(Object i){

		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Integer){
				return (Integer) get(toString(i));
			}
			try {
				return Integer.parseInt(get(toString(i)) + "");
			} catch (Exception e){
				return 0;
			}
		}
		return 0;
	}
	
	public long getLong(Object i){

		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Long){
				return (Long) get(toString(i));
			}
			try {
				return Long.parseLong(get(toString(i)) + "");
			} catch (Exception e){
				return 0L;
			}
		}
		return 0L;
	}
	
	public byte getByte(Object i){

		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Byte){
				return (Byte) get(toString(i));
			}
			try {
				return Byte.parseByte(get(toString(i)) + "");
			} catch (Exception e){
				return 0;
			}
		}
		return 0;
	}
	
	public float getFloat(Object i){
		
		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Float){
				return (Float) get(toString(i));
			}
			try {
				return Float.parseFloat(get(toString(i)) + "");
			} catch (Exception e){
				return 0F;
			}
		}
		return 0F;
	}
	
	public double getDouble(Object i){

		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Double){
				return (Double) get(toString(i));
			}
			try {
				return Double.parseDouble(get(toString(i)) + "");
			} catch (Exception e){
				return 0D;
			}
		}
		return 0D;
	}
	
	public boolean getBool(Object i){

		if (containsKey(toString(i))){
			if (get(toString(i)) instanceof Boolean){
				return (Boolean) get(toString(i));
			}
			try {
				return Boolean.valueOf(get(toString(i)) + "");
			} catch (Exception e){
				return false;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(Object i){
		
		if (containsKey(toString(i))){
			try {
				if (get(toString(i)) instanceof List){
					return (List<String>) get(toString(i));
				}
				set(i, new ArrayList<String>());
			} catch (Exception e){
				set(i, new ArrayList<String>());
			}
		} else {
			set(i, new ArrayList<String>());
		}
		
		return (List<String>) get(toString(i));
	}
	
	
	public String valuesToString(){
		
		String str = "";
		
		for (Object obj : values()){
			str = str.equals("") ? toString(obj) : str + ", " + toString(obj);
		}
		
		return str;
	}
	
	public String valuesToSortedString(){
		
		List<String> str = new ArrayList<String>();
		String newStr = "";
		
		for (Object obj : values()){
			str.add(toString(obj));
		}
		
		Collections.sort(str);
		
		for (String string : str){
			newStr = newStr.equals("") ? string : newStr + ", " + string;
		}
		
		return newStr;
	}
	
	public boolean editValue(Object oldValue, Object newValue){
		
		List<Object> oldKeys = new ArrayList<Object>(keySet());
	
		for (Object o : oldKeys){
			if (get(toString(o)).equals(oldValue)){
				put(toString(o), newValue);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean editKey(Object oldKey, Object newKey){
		
		if (containsKey(toString(oldKey)) && !containsKey(toString(newKey))){
			set(newKey, get(toString(oldKey)));
			remove(toString(oldKey));
			return true;
		}
		
		return false;
	}
	
	public String toString(Object i){
		
		if (i instanceof String){
			return (String) i;
		} else if (i instanceof Enum){
			try {
				return (String) i.getClass().getMethod("s").invoke(null);
			} catch (Exception e){
				return ((Enum) i).toString();
			}
		}
		return i.toString();
	}
	
	public boolean isEmpty(){
		return size() <= 0;
	}
	
	public boolean isSize(int size){
		return size() >= size;
	}
	
	public void set(Object i, Object infos){
		put(toString(i), infos);
	}
}