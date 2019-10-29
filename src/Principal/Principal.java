package Principal;
import java.lang.reflect.Field;
import java.util.ArrayList;

import Persona.PersonaModel;
import Servicios.Consulta;
import Utilidades.UBean;

public class Principal {

	public static void main(String[] args) {

		PersonaModel p = new PersonaModel();
		
//		ArrayList<Field> fields = UBean.obtenerAtributos(p);
//		for(Field f : fields){
//			System.out.println(f.getName());
//		}
//		
//		UBean.ejecutarSet(p, "descripcion", "Pared");
//		String descripcion = (String) UBean.ejecutarGet(p, "descripcion");
//		
//		System.out.println("Descripcion: " + descripcion);
//		
		
		Persona persona = new Persona();
		persona.setNombre("Gerardo");
		Consulta.guardar(persona);
		
		persona = (Persona) obtenerYMostrar();
		
		persona.setNombre("Roberto");
		
		Consulta.modificar(persona);
		
		persona = (Persona) obtenerYMostrar();
		
		Consulta.eliminar(persona);
		
	}
	
	public static Object obtenerYMostrar(){
		Object o = Consulta.obtenerPorId(Persona.class, 1);
		
		
		for(Field f : UBean.obtenerAtributos(o)){
			System.out.println(UBean.ejecutarGet(o, f.getName()));
		}
		
		return o;
	}

}
