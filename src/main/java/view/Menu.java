package view;

import java.util.UUID;

import dao.Empresa;
import io.IO;
import model.Departamento;
import model.Empleado;

public class Menu {
	public static void main(String[] args) {
		Empresa empresa = new Empresa();

		while (true) {
			System.out.println("Selecciona una opción (1 a 9):\n" + "1. Mostrar departamentos\n"
					+ "2. Agregar departamentos\n" + "3. Eliminar departamentos\n" + "4. Mostrar empleados\n"
					+ "5. Agregar empleados\n" + "6. Eliminar empleados\n" + "7. Cambiar jefe al departamento\n"
					+ "8. Cambiar departamento al empleado\n" + "9. Cerrar programa");

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

		System.out.print("ID del jefe (ENTER para no añadir - NULL): ");
		String jefeInput = IO.readString();
		UUID jefe = null;

		if (!jefeInput.isEmpty()) {
			jefe = UUID.fromString(jefeInput);
		}

		Departamento departamento = new Departamento(nombre, jefe);

		boolean agregado = empresa.agregarDepartamento(departamento);

		if (agregado) {
			System.out.println("Departamento agregado con éxito.");
		} else {
			System.out.println("No se pudo agregar el departamento.");
		}
	}

	/*
	 * Opcion 3 Eliminar un departamento existente
	 */
	private static void eliminarDepartamento(Empresa empresa) {
		System.out.print("ID del departamento a eliminar: ");
		String id = IO.readString();
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
		System.out.print("Salario: ");
		int salario = IO.readInt();
		System.out.print("Fecha de ingreso: ");
		String fecha = IO.readString();

		System.out.print("ID del departamento (ENTER para no añadir - NULL): ");
		String departamentoInput = IO.readString();
		UUID departamento = null;

		if (!departamentoInput.isEmpty()) {
			departamento = UUID.fromString(departamentoInput);
		}

		Empleado empleado = new Empleado(nombre, salario, fecha, departamento);

		boolean agregado = empresa.agregarEmpleado(empleado);

		if (agregado) {
			System.out.println("Empleado agregado con éxito.");
		} else {
			System.out.println("No se pudo agregar el empleado.");
		}
	}

	/*
	 * Opcion 6 Eliminar un empleado existente
	 */
	private static void eliminarEmpleado(Empresa empresa) {
		System.out.print("ID del empleado a eliminar: ");
		String id = IO.readString();
		boolean eliminado = empresa.eliminarEmpleado(id);
		if (eliminado) {
			System.out.println("Empleado eliminado con éxito.");
		} else {
			System.out.println("No se pudo eliminar el empleado.");
		}
	}

	/*
	 * Opcion 7 Cambiar el jefe de un departamento
	 */
	private static void cambiarJefeDepartamento(Empresa empresa) {
		System.out.print("ID del departamento al que deseas cambiar el jefe: ");
		String idDepartamento = IO.readString();

		System.out.print("Nuevo ID del jefe: ");
		String idJefe = IO.readString();

		try {
			UUID departamentoID = UUID.fromString(idDepartamento);
			UUID jefeID = UUID.fromString(idJefe);

			Departamento departamento = empresa.obtenerDepartamentoPorID(departamentoID);

			if (departamento != null) {
				// Cambiar el jefe del departamento al ID del nuevo jefe
				departamento.cambiarJefe(jefeID);
				// Actualizar el departamento con el nuevo jefe en la base de datos
				boolean actualizadoDepartamento = empresa.actualizarDepartamento(departamento);

				if (actualizadoDepartamento) {
					System.out.println("Jefe del departamento cambiado con éxito.");

					// Obtener información del nuevo jefe a partir de su ID
					Empleado nuevoJefe = empresa.obtenerEmpleadoPorID(jefeID);

					if (nuevoJefe != null) {
						// Cambiar el departamento del nuevo jefe al departamento especificado
						nuevoJefe.cambiarDepartamento(departamentoID);
						// Actualizar el registro del nuevo jefe en la base de datos
						boolean actualizadoNuevoJefe = empresa.actualizarEmpleado(nuevoJefe);

						if (actualizadoNuevoJefe) {
							System.out.println("Departamento del nuevo jefe actualizado.");
						} else {
							System.out.println("No se pudo actualizar el departamento del nuevo jefe.");
						}
					} else {
						System.out.println("El nuevo jefe especificado no existe.");
					}
				} else {
					System.out.println("No se pudo cambiar el jefe del departamento.");
				}
			} else {
				System.out.println("El departamento especificado no existe.");
			}
		} catch (Exception e) {
			System.out.println("Los IDs introducios no son válidos");
			;
		}
	}

	/*
	 * Opcion 8 Cambiar el departamento de un empleado
	 */
	private static void cambiarDepartamentoEmpleado(Empresa empresa) {
		System.out.print("ID del empleado al que deseas cambiar el departamento: ");
		String idEmpleado = IO.readString();

		System.out.print("Nuevo ID del departamento: ");
		String idDepartamento = IO.readString();

		try {
			UUID empleadoID = UUID.fromString(idEmpleado);
			UUID departamentoID = UUID.fromString(idDepartamento);

			Empleado empleado = empresa.obtenerEmpleadoPorID(empleadoID);
			if (empleado != null) {
				// Cambiar el departamento del empleado al ID del nuevo departamento
				empleado.cambiarDepartamento(departamentoID);
				// Actualizar el registro del empleado en la base de datos
				boolean actualizado = empresa.actualizarEmpleado(empleado);

				if (actualizado) {
					System.out.println("Departamento del empleado cambiado con éxito.");
				} else {
					System.out.println("No se pudo cambiar el departamento del empleado.");
				}
			} else {
				System.out.println("El empleado especificado no existe.");
			}
		} catch (Exception e) {
			System.out.println("Los IDs introducios no son válidos");
			;
		}
	}

	/*
	 * Opcion 9 Cerrar programa
	 */
	private static void cerrarEmpresa(Empresa empresa) {
		empresa.close();
		System.out.println("Empresa cerrada");
	}
}
