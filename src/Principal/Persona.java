package Principal;
import Anotaciones.Columna;
import Anotaciones.Foraneo;
import Anotaciones.Id;
import Anotaciones.Tabla;

@Tabla(nombre="Persona")
public class Persona {
	@Id
	@Columna(nombre="Id")
	private int Id;
	@Columna(nombre="Nombre")
	private String Nombre;
	@Foraneo()
	@Columna(nombre="Domicilio")
	private Domicilio domicilio;
	
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
	
	public void setDomicilio(Domicilio dom) {
		this.domicilio = dom;
	}
	
	public Domicilio getDomicilio() {
		return this.domicilio;
	}
}
