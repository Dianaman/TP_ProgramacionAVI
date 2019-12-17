package Principal;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import Servicios.Consulta;
import Utilidades.UBean;

public class Principal {

	public static void main(String[] args) {

		/*Persona persona = new Persona();
		persona.setNombre("Gerardo");
		long id = Consulta.guardar(persona);
		
		persona = (Persona) obtenerYMostrar(id);
		
		persona.setNombre("Roberto");
		
		Consulta.modificar(persona);
		
		persona = (Persona) obtenerYMostrar(id);
		
		Consulta.eliminar(persona);*/

		Persona persona = new Persona();
		persona.setNombre("Delfina");
		persona.setId(1);
		long idModificado = Consulta.guardarModificar(persona);
		System.out.println("id modificado: " + idModificado);
		persona = (Persona) obtenerYMostrar(idModificado);
		
		List<Object> personasObj = Consulta.obtenerTodos(Persona.class);
		for(Object p : personasObj) {
			for(Field f : UBean.obtenerAtributos(p)){
				System.out.println(UBean.ejecutarGet(p, f.getName()));
			}
		}
	}
	
	public static Object obtenerYMostrar(long id){
		Object o = Consulta.obtenerPorId(Persona.class, id);
		
		
		for(Field f : UBean.obtenerAtributos(o)){
			System.out.println(UBean.ejecutarGet(o, f.getName()));
		}
		
		return o;
	}

}
