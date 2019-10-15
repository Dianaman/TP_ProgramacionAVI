import java.lang.reflect.Field;
import java.util.ArrayList;

import Persona.PersonaModel;
import Utilidades.UBean;

public class Principal {

	public static void main(String[] args) {

		PersonaModel p = new PersonaModel();
		
		ArrayList<Field> fields = UBean.obtenerAtributos(p);
		for(Field f : fields){
			System.out.println(f.getName());
		}
		
		UBean.ejecutarSet(p, "descripcion", "Pared");
		String descripcion = (String) UBean.ejecutarGet(p, "descripcion");
		
		System.out.println("Descripcion: " + descripcion);
		
		
	}

}
