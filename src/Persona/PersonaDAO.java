package Persona;

import Factory.Conexion;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
	public boolean alta(PersonaModel persona){
		String query = "INSERT INTO item (descripcion) VALUES ('"+
			persona.getDescripcion()+"')";
		
		try {
			CallableStatement st = Conexion.getConexion().prepareCall(query);
			return st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean baja(PersonaModel persona){
		String query = "DELETE FROM item WHERE id = "+persona.getId();
		
		try {
			CallableStatement st = Conexion.getConexion().prepareCall(query);
			return st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean modificacion(PersonaModel persona){
		String query = "UPDATE item SET descripcion = '"+persona.getDescripcion()+
				"' WHERE id = " + persona.getId();
		
		try {
			CallableStatement st = Conexion.getConexion().prepareCall(query);
			return st.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	public PersonaModel obtenerPorId(Long id){
		String query = "SELECT descripcion FROM item WHERE id = " + id;
		PersonaModel p = new PersonaModel();
		
		try {
			CallableStatement st = Conexion.getConexion().prepareCall(query);
			ResultSet rs = st.executeQuery(query);
			
			String descripcion = "";
			while(rs.next()){
				descripcion = rs.getString(1);
			}

			p = new PersonaModel(id, descripcion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public List<PersonaModel> obtenerTodas(){
		String query = "SELECT * FROM item";
		List<PersonaModel> personas = new ArrayList<PersonaModel>();
		
		try {
			CallableStatement st = Conexion.getConexion().prepareCall(query);
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()){
				Long id = rs.getLong(1);
				String descripcion = rs.getString(2);
				
				PersonaModel p = new PersonaModel(id, descripcion);
				personas.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return personas;
	}
	
	public void cerrar(){
		Conexion.close();
	}
}
