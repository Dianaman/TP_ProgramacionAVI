package Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UConexion {
	private static Connection conexion;
	
	private static Connection abrirConexion(){
		ResourceBundle rb = ResourceBundle.getBundle("framework");
		
		
		try {
			Class.forName(rb.getString("driver"));

			String con = rb.getString("db.name");
			
			conexion = DriverManager.getConnection(con, rb.getString("db.user"), rb.getString("db.pass"));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return conexion;
	}
	
	public static Connection obtenerConexion(){
		try {
			if(conexion == null || conexion.isClosed()){
				conexion = abrirConexion();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
				
		return conexion;
	}
	
	public static void close(){
		try {
			conexion.close();
			conexion = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
