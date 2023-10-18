package model;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
	
	private UUID id; 
	private String nombre; 
	private Double salario; 
	private LocalDate fecha;
	private Departamento departamento;

	public Empleado(String nombre, Double salario, LocalDate fecha, Departamento departamento) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setSalario(salario);
		setFecha(fecha);
		setDepartamento(departamento);
	}
	
	public void cambiarDepartamento(Departamento nuevoDepartamento) {
	    this.departamento = nuevoDepartamento;
	}
	
	public String toString() {
		String departamentoStr = (departamento != null) ? departamento.getNombre() : "Sin departamento";
	    return String.format("ID: %s, Nombre: %s, Salario: %.2f, Fecha: %s, Departamento: %s", id.toString(), nombre, salario, fecha.toString(), departamentoStr);
		
	}
}
