package model;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
	
	UUID id; 
	String nombre; 
	int salario; 
	String fecha;
	UUID departamento;

	public Empleado(String nombre, int salario, String fecha, UUID departamento) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setSalario(salario);
		setFecha(fecha);
		setDepartamento(departamento);
	}
	
	public void cambiarDepartamento(UUID nuevoDepartamento) {
	    this.departamento = nuevoDepartamento;
	}
	
	public String toString() {
		return String.format("%s | %s | %d | %s | %s", id, nombre, salario, fecha, departamento);
		
	}
}
