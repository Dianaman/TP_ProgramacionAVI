package Principal;

import Anotaciones.Columna;
import Anotaciones.Id;
import Anotaciones.Tabla;

@Tabla(nombre="Domicilio")
public class Domicilio {
	@Id
	@Columna()
	private int id;

	@Columna(nombre="Calle")
	private String calle;
	
	@Columna(nombre="Altura")
	private int altura;

	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCalle() {
		return this.calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}
	
	public int getAltura() {
		return this.altura;
	}
	

	public void setAltura(int altura) {
		this.altura = altura;
	}
}
