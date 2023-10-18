package model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {
	
	private UUID id; 
	private String nombre; 
	private Empleado jefe;

	public Departamento(String nombre, Empleado jefe) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setJefe(jefe);
	}
	
	public void cambiarJefe(Empleado nuevoJefe) {
	    this.jefe = nuevoJefe;
	}
	
	public String toString() {
		String jefeStr = (jefe != null) ? jefe.getId().toString() : "Sin jefe";
	    return String.format("ID: %s, Nombre: %s, Jefe: %s", id.toString(), nombre, jefeStr);
		
	}
}
