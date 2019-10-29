package Utilidades;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import Anotaciones.Id;

public class UBean {
	public static ArrayList<Field> obtenerAtributos(Object obj){
		System.out.println(obj);
		Class c = obj.getClass();
		Field[] fields = c.getDeclaredFields();
		
		ArrayList<Field> arrayListFields = new ArrayList<Field>();
		for(Field f : fields){
			arrayListFields.add(f);
		}
		
		return arrayListFields;
	}
	
	public static void ejecutarSet(Object o, String att, Object valor){
		Class c = o.getClass();
		Method[] dmet = c.getDeclaredMethods();
		
		for(Method m : dmet){
			Object[] param = new Object[1];
			String nombreMetodo = m.getName().toLowerCase();
			
			if(nombreMetodo.startsWith("set") && nombreMetodo.contains(att.toLowerCase()) && 
				m.getParameterCount() == 1){
				param[0] = valor;

				try {
					m.invoke(o, param);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				break;
			}
		}
	}

	public static Object ejecutarGet(Object o, String att){
		Class c = o.getClass();
		Method[] dmet = c.getDeclaredMethods();
		Object returnValue = "";
		
		for(Method m : dmet){			
			Object[] param = new Object[0];
			String nombreMetodo = m.getName().toLowerCase();
			if(nombreMetodo.startsWith("get") && m.getAnnotation(Id.class) == null
				&& nombreMetodo.contains(att.toLowerCase()) && 
				m.getParameterCount() == 0){

				try {
					returnValue = m.invoke(o, param);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				break;
			}
		}
		
		return returnValue;
	}
	
}
