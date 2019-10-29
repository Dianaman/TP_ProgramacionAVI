package Servicios;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Anotaciones.Id;
import Utilidades.UBean;
import Utilidades.UConexion;

public class Consulta {
	public static void guardar(Object o){
		Class c = o.getClass();
		String tabla = c.getSimpleName();// TODO usar annotation
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> fields = new ArrayList<String>();

		System.out.println(tabla);
		for(Field f : fieldsRaw){
			if(f.getAnnotation(Id.class) == null){
				System.out.println(f.getClass().getAnnotations().toString());
				fields.add(f.getName()); // TODO usar annotation
				String field = "";
				if(f.getType() == String.class){
					field += "'";
				}
				field += UBean.ejecutarGet(o, f.getName()).toString();
				if(f.getType() == String.class){
					field += "'";
				}
				
				values.add(field);
			}
		}
		
		String query = "INSERT INTO " + tabla + " (" + String.join(",", fields) + ") VALUES ("+
			String.join(",", values)	 +")";
			
		
		System.out.println(query);
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query);
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificar(Object o){
		Class c = o.getClass();
		String tabla = c.getSimpleName();
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> fields = new ArrayList<String>();
		String campoId = "";
		String campoValue = "";
		
		for(Field f : fieldsRaw){
			if(f.getAnnotation(Id.class) == null){
				fields.add(f.getName());
				StringBuilder valor = new StringBuilder();
				if(f.getType() == String.class){
					valor.append("'");
				}
				valor.append(UBean.ejecutarGet(o, f.getName()).toString());
				if(f.getType() == String.class){
					valor.append("'");
				}
				values.add(valor.toString());
			} else {
				campoId = f.getName();
				campoValue = UBean.ejecutarGet(o, f.getName()).toString();
			}
		}
		
		
		
		StringBuilder query = new StringBuilder("UPDATE " + tabla + " SET ");
		
		for(int i=0; i<fields.size(); i++){
			
			query.append(fields.get(i) + "=" + values.get(i) );
			if(i < fields.size() - 1){
				query.append(", ");
			}
		}
		
		query.append(" WHERE " + campoId + " = " + campoValue);
		
		System.out.println(query);
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query.toString());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void eliminar(Object o){
		Class c = o.getClass();
		String tabla = c.getSimpleName();
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		String campoId = "";
		String campoValue = "";
		
		for(Field f : fieldsRaw){
			if(f.getAnnotation(Id.class) != null){
				campoId = f.getName();
				campoValue = UBean.ejecutarGet(o, f.getName()).toString();
				break;
			}
		}
		
		StringBuilder query = new StringBuilder("DELETE FROM " + tabla + 
				" WHERE " + campoId + " = "+ campoValue);
		
		System.out.println(query);
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query.toString());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Object obtenerPorId(Class c, Object id){
		String tabla = c.getSimpleName();
		Field[] atributos = c.getDeclaredFields();
		
		Object objeto=null;
		
		String query = "SELECT * FROM " + tabla + " WHERE Id = " + id;
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query);
			ResultSet rs = st.executeQuery(query);
			
			objeto = c.newInstance();
			
			String descripcion = "";
			while(rs.next()){
				for(int i=1; i<=atributos.length; i++){
					Object valor = rs.getObject(i);
					UBean.ejecutarSet(objeto, atributos[i-1].getName(), valor);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return objeto;
	}
}
