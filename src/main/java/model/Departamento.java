package model;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {
	
	UUID id; 
	String nombre; 
	UUID jefe;

	public Departamento(String nombre) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setJefe(null);
	}
	
	public String toString() {
		return String.format("%s | %s | %s ", id, nombre, jefe);
		
	}
}
