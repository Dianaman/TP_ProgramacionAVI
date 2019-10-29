package Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UConexion {
	private static Connection conexion;
	
	private static Connection abrirConexion(){
		// TODO utilizar framework.properties
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String con = "jdbc:mysql://localhost:3306/db";
			
			conexion = DriverManager.getConnection(con, "root", "");
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
