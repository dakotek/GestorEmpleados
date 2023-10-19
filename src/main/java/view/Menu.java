package view;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import dao.Empresa;
import io.IO;
import model.Departamento;
import model.Empleado;
import dao.SingletonEmpresa;

public class Menu {
	public static Connection conexion = SingletonEmpresa.getConnection();
	
	public static void main(String[] args) {
		Empresa empresa = new Empresa();
		
		while (true) {
			System.out.println("Selecciona una opción (1 a 9):\n" + "1. Mostrar departamentos\n"
					+ "2. Agregar departamentos\n" + "3. Eliminar departamentos\n" + "4. Mostrar empleados\n"
					+ "5. Agregar empleados\n" + "6. Eliminar empleados\n" + "7. Cambiar jefe\n"
					+ "8. Cambiar departamento\n" + "9. Cerrar programa");

			switch (IO.readInt()) {
			case 1:
				mostrarDepartamentos(empresa);
				break;
			case 2:
				agregarDepartamento(empresa);
				break;
			case 3:
				eliminarDepartamento(empresa);
				break;
			case 4:
				mostrarEmpleados(empresa);
				break;
			case 5:
				agregarEmpleado(empresa);
				break;
			case 6:
				eliminarEmpleado(empresa);
				break;
			case 7:
				cambiarJefeDepartamento(empresa);
				break;
			case 8:
				cambiarDepartamentoEmpleado(empresa);
				break;
			case 9:
				cerrarEmpresa(empresa);
				return;
			default:
				System.out.println("Opción no válida. Inténtalo de nuevo.");
			}
		}
	}

	/*
	 * Opcion 1 Mostrar la lista de departamentos
	 */
	private static void mostrarDepartamentos(Empresa empresa) {
		String departamentos = empresa.mostrarDepartamentos();
		System.out.println(departamentos);
	}

	/*
	 * Opcion 2 Agregar nuevo departamento
	 */
	private static void agregarDepartamento(Empresa empresa) {
		System.out.print("Nombre del departamento: ");
		String nombre = IO.readString();

		if (nombre != null && !nombre.isEmpty()) {
			System.out.print("ID del jefe (ENTER para no añadir): ");
			String jefeIdInput = IO.readString();
			UUID jefeId = null;

			if (!jefeIdInput.isEmpty()) {
				try {
					jefeId = UUID.fromString(jefeIdInput);
				} catch (IllegalArgumentException e) {
					System.out.println("ID de jefe no válido. Se establecerá sin jefe.");
				}
			}

			Empleado jefe = null;
			if (jefeId != null) {
				jefe = empresa.obtenerEmpleadoPorID(jefeId);
				if (jefe == null) {
					System.out.println(
							"El ID de jefe especificado no corresponde a un empleado existente. Se establecerá sin jefe.");
					jefeId = null;
				}
			}

			Departamento departamento = new Departamento(nombre, jefe);

			boolean agregado = empresa.agregarDepartamento(departamento);

			if (agregado) {
				System.out.println("Departamento agregado con éxito.");
			} else {
				System.out.println("No se pudo agregar el departamento.");
			}
		} else {
			System.out.println("El nombre del departamento no puede ser nulo o vacío.");
		}
	}

	/*
	 * Opcion 3 Eliminar un departamento existente
	 */
	private static void eliminarDepartamento(Empresa empresa) {
		System.out.print("ID del departamento a eliminar (en formato UUID): ");
		String idInput = IO.readString();
		UUID id = null;

		try {
			id = UUID.fromString(idInput); // Convertir a UUID
		} catch (IllegalArgumentException e) {
			System.out.println("ID de departamento no válido.");
			return;
		}

		boolean eliminado = empresa.eliminarDepartamento(id);

		if (eliminado) {
			System.out.println("Departamento eliminado con éxito.");
		} else {
			System.out.println("No se pudo eliminar el departamento.");
		}
	}

	/*
	 * Opcion 4 Mostrar la lista de empleados
	 */
	private static void mostrarEmpleados(Empresa empresa) {
		String empleados = empresa.mostrarEmpleados();
		System.out.println(empleados);
	}

	/*
	 * Opcion 5 Agregar nuevo empleado
	 */
	private static void agregarEmpleado(Empresa empresa) {
		System.out.print("Nombre del empleado: ");
		String nombre = IO.readString();

		if (nombre != null && !nombre.isEmpty()) {
			System.out.print("Salario: ");
			double salario = IO.readDouble();
			LocalDate fecha = obtenerFechaValida();

			System.out.print("ID del departamento (ENTER para no añadir): ");
			String departamentoIdInput = IO.readString();
			UUID departamentoId = null; // Valor predeterminado es null

			if (!departamentoIdInput.isEmpty()) {
				try {
					departamentoId = UUID.fromString(departamentoIdInput);
				} catch (IllegalArgumentException e) {
					System.out.println("ID de departamento no válido. No se establecerá el departamento.");
				}
			}

			Departamento departamento = null;
			if (departamentoId != null) {
				departamento = empresa.obtenerDepartamentoPorID(departamentoId);
				if (departamento == null) {
					System.out.println(
							"El ID de departamento especificado no corresponde a un departamento existente. No se establecerá el departamento.");
				}
			}

			Empleado empleado = new Empleado(nombre, salario, fecha, departamento);

			boolean agregado = empresa.agregarEmpleado(empleado);

			if (agregado) {
				System.out.println("Empleado agregado con éxito.");
			} else {
				System.out.println("No se pudo agregar el empleado.");
			}
		} else {
			System.out.println("El nombre del departamento no puede ser nulo o vacío.");
		}
	}

	/*
	 * Opcion 6 Eliminar un empleado existente
	 */
	private static void eliminarEmpleado(Empresa empresa) {
		System.out.print("ID del empleado a eliminar: ");
		String idInput = IO.readString();

		try {
			UUID id = UUID.fromString(idInput);
			boolean eliminado = empresa.eliminarEmpleado(id);
			if (eliminado) {
				System.out.println("Empleado eliminado con éxito.");
			} else {
				System.out.println("No se pudo eliminar el empleado.");
			}
		} catch (IllegalArgumentException e) {
			System.out.println("ID de empleado no válido. No se pudo eliminar el empleado.");
		}
	}

	/*
	 * Opcion 7 cambiar jefe al departamento
	 */
	private static void cambiarJefeDepartamento(Empresa empresa) {
		System.out.print("ID del departamento al que deseas cambiar el jefe: ");
		String departamentoIdInput = IO.readString();
		UUID departamentoId = null;

		try {
			departamentoId = UUID.fromString(departamentoIdInput);
		} catch (IllegalArgumentException e) {
			System.out.println("ID de departamento no válido.");
			return;
		}

		System.out.print("ID del nuevo jefe (ENTER para eliminar el jefe actual): ");
		String nuevoJefeIdInput = IO.readString();
		UUID nuevoJefeId = null;

		if (!nuevoJefeIdInput.isEmpty()) {
			try {
				nuevoJefeId = UUID.fromString(nuevoJefeIdInput);
			} catch (IllegalArgumentException e) {
				System.out.println("ID de jefe no válido. No se cambiará el jefe.");
			}
		}

		Empleado nuevoJefe = null;
		if (nuevoJefeId != null) {
			nuevoJefe = empresa.obtenerEmpleadoPorID(nuevoJefeId);
			if (nuevoJefe == null) {
				System.out.println(
						"El ID de jefe especificado no corresponde a un empleado existente. No se cambiará el jefe.");
				nuevoJefeId = null;
			}
		}

		boolean cambiado = empresa.cambiarJefeDepartamento(departamentoId, nuevoJefe);

		if (cambiado) {
			System.out.println("Jefe del departamento cambiado con éxito.");
		} else {
			System.out.println("No se pudo cambiar el jefe del departamento.");
		}
	}

	/*
	 * Opcion 8 cambiar departamento al empleado
	 */
	private static void cambiarDepartamentoEmpleado(Empresa empresa) {
		try {
			System.out.print("ID del empleado al que deseas cambiar el departamento: ");
			String empleadoIdInput = IO.readString();
			UUID empleadoId = null;

			try {
				empleadoId = UUID.fromString(empleadoIdInput);
			} catch (IllegalArgumentException e) {
				System.out.println("ID de empleado no válido.");
				return;
			}

			System.out.print("ID del nuevo departamento (ENTER para eliminar el departamento actual): ");
			String nuevoDepartamentoIdInput = IO.readString();
			UUID nuevoDepartamentoId = null;

			if (!nuevoDepartamentoIdInput.isEmpty()) {
				try {
					nuevoDepartamentoId = UUID.fromString(nuevoDepartamentoIdInput);
				} catch (IllegalArgumentException e) {
					System.out.println("ID de departamento no válido. No se cambiará el departamento.");
				}
			}

			Departamento nuevoDepartamento = null;
			if (nuevoDepartamentoId != null) {
				nuevoDepartamento = empresa.obtenerDepartamentoPorID(nuevoDepartamentoId);
				if (nuevoDepartamento == null) {
					System.out.println(
							"El ID de departamento especificado no corresponde a un departamento existente. No se cambiará el departamento.");
					nuevoDepartamentoId = null;
				}
			}

			boolean cambiado = empresa.cambiarDepartamentoEmpleado(empleadoId, nuevoDepartamento);

			if (cambiado) {
				System.out.println("Departamento del empleado cambiado con éxito.");
			} else {
				System.out.println("No se pudo cambiar el departamento del empleado.");
			}
		} catch (Exception e) {
			System.out.println("Para asignar este departamento al empleado, primero debe de tener un jefe");
		}
	}

	/*
	 * Opcion 9 Cerrar programa
	 */
	private static void cerrarEmpresa(Empresa empresa) {
		SingletonEmpresa.close();
		System.out.println("Empresa cerrada");
	}

	/*
	 * Controla que la fecha introducida sea válida
	 */
	public static LocalDate obtenerFechaValida() {
		while (true) {
			System.out.print("Fecha de ingreso (yyyy-MM-dd): ");
			String fechaStr = IO.readString();

			try {
				LocalDate fecha = LocalDate.parse(fechaStr);
				return fecha;
			} catch (DateTimeParseException e) {
				System.out.println("Fecha no válida. Ingresa una fecha en el formato correcto (yyyy-MM-dd).");
			}
		}
	}
}
