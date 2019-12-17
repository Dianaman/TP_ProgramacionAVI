package Servicios;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Anotaciones.Columna;
import Anotaciones.Foraneo;
import Anotaciones.Id;
import Anotaciones.Tabla;
import Utilidades.UBean;
import Utilidades.UConexion;

public class Consulta {
	public static long guardar(Object o){
		long nuevoId = 0;
		Class c = o.getClass();
		String tabla = getTableName(c);
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> fields = new ArrayList<String>();
		
		long idForaneo = guardarEnCascada(o);

		for(Field f : fieldsRaw){
			if(f.getAnnotation(Id.class) == null){
				if(f.getAnnotation(Foraneo.class) == null || idForaneo != 0) {
					String columna = getColumnName(f);
					fields.add(columna);
					String field = "";
					if(f.getType() == String.class){
						field += "'";
					}
					
					if (f.getAnnotation(Foraneo.class) != null) {
						field += idForaneo;
					} else {
						field += UBean.ejecutarGet(o, columna).toString();
					}
					
					if (f.getType() == String.class) {
						field += "'";
					}
					
					values.add(field);
				}
			}
		}
		
		String query = "INSERT INTO " + tabla + " (" + String.join(",", fields) + ") VALUES ("+
			String.join(",", values)	 +")";
			
		
		System.out.println(query);
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query);
			st.execute();
			st.getGeneratedKeys();
			ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                nuevoId = generatedKeys.getLong(1);
            }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nuevoId;
	}
	
	public static long modificar(Object o){
		long idModificado = 0;
		Class c = o.getClass();
		String tabla = getTableName(c);
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> fields = new ArrayList<String>();
		String campoId = "";
		String campoValue = "";
		
		for(Field f : fieldsRaw){
			if(f.getAnnotation(Id.class) == null){
				if(f.getAnnotation(Foraneo.class) == null) {
				String columna = getColumnName(f);
				fields.add(columna);
				StringBuilder valor = new StringBuilder();
				if(f.getType() == String.class){
					valor.append("'");
				}
				valor.append(UBean.ejecutarGet(o, columna).toString());
				if(f.getType() == String.class){
					valor.append("'");
				}
				values.add(valor.toString());
				}
			} else {
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
		
		query.append(" WHERE Id = " + campoValue);
		
		System.out.println(query);
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query.toString());
			st.execute();
			st.getGeneratedKeys();
			ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                idModificado = generatedKeys.getLong(1);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idModificado;
	}
	
	public static void eliminar(Object o){
		Class c = o.getClass();
		String tabla = getTableName(c);
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		String campoValue = "";
		
		for(Field f : fieldsRaw){
			if(f.getAnnotation(Id.class) != null){
				campoValue = UBean.ejecutarGet(o, f.getName()).toString();
				break;
			}
		}
		
		StringBuilder query = new StringBuilder("DELETE FROM " + tabla + 
				" WHERE Id = "+ campoValue);
		
		System.out.println(query);
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query.toString());
			st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Object obtenerPorId(Class c, Object id){
		String tabla = getTableName(c);
		Field[] atributos = c.getDeclaredFields();
		
		Object objeto = null;
		
		String query = "SELECT * FROM " + tabla + " WHERE Id = " + id;
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query);
			ResultSet rs = st.executeQuery(query);
			
			objeto = c.newInstance();
			
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
	
	public static long guardarModificar(Object o) {
		long idUpsertado = modificar(o);
		if(idUpsertado == 0) {
			idUpsertado = guardar(o);
		}
		return idUpsertado;
	}
	
	public static List<Object> obtenerTodos(Class c) {
		List<Object> objs = new ArrayList<Object>();
		
		String tabla = getTableName(c);
		Field[] atributos = c.getDeclaredFields();
		
		
		String query = "SELECT * FROM " + tabla;
		
		try {
			CallableStatement st = UConexion.obtenerConexion().prepareCall(query);
			ResultSet rs = st.executeQuery(query);
			
			
			while(rs.next()){
				Object objeto = c.newInstance();
				
				for(int i=1; i<=atributos.length; i++){
					Object valor = rs.getObject(i);

					UBean.ejecutarSet(objeto, atributos[i-1].getName(), valor);
				}
				
				objs.add(objeto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return objs;
	}

	public static long guardarEnCascada(Object o) {
		ArrayList<Field> fieldsRaw = UBean.obtenerAtributos(o);
		boolean tieneObjetoAnidado = false;
		long id = 0;

		for(Field f : fieldsRaw){
			if(f.getAnnotation(Foraneo.class) != null){
				Object anidado = null;
				try {
					anidado = UBean.ejecutarGet(o, f.getName());
					if(anidado != null) {
						id = guardar(anidado);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		return id;
	}
	
	private static String getTableName(Class c) {
		Tabla claseTabla = (Tabla) c.getAnnotation(Tabla.class);
		String tabla = claseTabla.nombre();
		if(tabla == "") {
			tabla = c.getSimpleName();
		}
		return tabla;
	}
	
	private static String getColumnName(Field f) {
		Columna col = f.getAnnotation(Columna.class);
		String columna = col.nombre();
		if(columna == "") {
			columna = f.getName();
		}
		return columna;
	}
}
