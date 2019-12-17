package Principal;
import Anotaciones.Columna;
import Anotaciones.Id;
import Anotaciones.Tabla;

@Tabla(nombre="Persona")
public class Persona {
	@Id
	@Columna(nombre="Id")
	private int Id;
	@Columna(nombre="Nombre")
	private String Nombre;
	
	public void setNombre(String nombre){
		this.Nombre = nombre;
	}
	
	public String getNombre(){
		return this.Nombre;
	}
	
	public void setId(int id){
		this.Id=id;
	}
	
	public int getId(){
		return this.Id;
	}
}
