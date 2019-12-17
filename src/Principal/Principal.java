package Principal;
import java.lang.reflect.Field;
import java.util.ArrayList;
import Servicios.Consulta;
import Utilidades.UBean;

public class Principal {

	public static void main(String[] args) {

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
		Object o = Consulta.obtenerPorId(Persona.class, 14);
		
		
		for(Field f : UBean.obtenerAtributos(o)){
			System.out.println(UBean.ejecutarGet(o, f.getName()));
		}
		
		return o;
	}

}
