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

	public Departamento(String nombre, UUID jefe) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setJefe(jefe);
	}
	
	public void cambiarJefe(UUID nuevoJefe) {
	    this.jefe = nuevoJefe;
	}
	
	public String toString() {
		return String.format("%s | %s | %s ", id, nombre, jefe);
		
	}
}
