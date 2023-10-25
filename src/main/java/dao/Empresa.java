package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import model.Departamento;
import model.Empleado;
import view.Menu;

public class Empresa {

	private String sql;

	/**
	 * Agrega un nuevo departamento a la BBDD
	 * 
	 * @param departamento Departamento a agregar
	 * @return true si se agregó con éxito, false si no se pudo agregar
	 */
	public boolean agregarDepartamento(Departamento departamento) {
		sql = "INSERT INTO departamentos (id, nombre, jefe) VALUES (?, ?, ?)";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ps.setString(1, departamento.getId().toString());
			ps.setString(2, departamento.getNombre());

			UUID jefeId = (departamento.getJefe() != null) ? departamento.getJefe().getId() : null;
			ps.setObject(3, jefeId, java.sql.Types.VARCHAR);

			int filasAfectadas = ps.executeUpdate();

			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Elimina un departamento de la BBDD por su ID
	 * 
	 * @param id ID del departamento a eliminar
	 * @return true si se eliminó con éxito, false si no se pudo eliminar
	 */
	public boolean eliminarDepartamento(UUID id) {
		sql = "DELETE FROM departamentos WHERE id = ?";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ps.setString(1, id.toString());
			int filasAfectadas = ps.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Muestra todos los departamentos
	 * 
	 * @return Una cadena con la lista de departamentos
	 */
	public String mostrarDepartamentos() {
		StringBuilder sb = new StringBuilder();
		sql = "SELECT * FROM departamentos";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				UUID id = UUID.fromString(rs.getString("id"));
				String nombre = rs.getString("nombre");

				String jefeId = rs.getString("jefe");
				UUID jefeUUID = null;
				if (jefeId != null && !jefeId.isEmpty()) {
					jefeUUID = UUID.fromString(jefeId);
				}

				Empleado jefe = null;
				if (jefeUUID != null) {
					jefe = obtenerEmpleadoPorID(jefeUUID);
				}

				Departamento departamento = new Departamento(id, nombre, jefe);
				sb.append(departamento.toString());
				sb.append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Busca un departamento por su ID
	 * 
	 * @param id ID del departamento a buscar
	 * @return Departamento encontrado o null si no se encuentra
	 */
	public Departamento obtenerDepartamentoPorID(UUID id) {
		try (PreparedStatement ps = Menu.conexion.prepareStatement(
				"SELECT d.id, d.nombre, e.id AS jefe_id, e.nombre AS jefe_nombre, e.salario AS jefe_salario, e.fecha AS jefe_fecha FROM departamentos d LEFT JOIN empleados e ON d.jefe = e.id WHERE d.id = ?")) {
			ps.setObject(1, id.toString(), java.sql.Types.VARCHAR);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				UUID departamentoId = UUID.fromString(rs.getString("id"));
				String nombre = rs.getString("nombre");
				UUID jefeId = UUID.fromString(rs.getString("jefe_id"));
				String jefeNombre = rs.getString("jefe_nombre");
				double jefeSalario = rs.getDouble("jefe_salario");
				LocalDate jefeFecha = LocalDate.parse(rs.getString("jefe_fecha"));

				Empleado jefe = new Empleado(jefeId, jefeNombre, jefeSalario, jefeFecha, null);

				return new Departamento(departamentoId, nombre, jefe);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // Retorna null si no se encontró el departamento
	}

	/**
	 * Actualiza un departamento en la BBDD
	 * 
	 * @param departamento Departamento a actualizar
	 * @return true si se actualizó con éxito, false si no se pudo actualizar
	 */
	public boolean actualizarDepartamento(Departamento departamento) {
		String sql = "UPDATE departamentos SET nombre = ?, jefe = ? WHERE id = ?";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ps.setString(1, departamento.getNombre());

			// Verifica si el jefe no es nulo antes de asignarlo
			String jefeId = (departamento.getJefe() != null) ? departamento.getJefe().getId().toString() : null;

			if (jefeId != null) {
				ps.setString(2, jefeId);
			} else {
				ps.setNull(2, java.sql.Types.VARCHAR);
			}

			ps.setString(3, departamento.getId().toString());

			int filasAfectadas = ps.executeUpdate();

			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Agrega un nuevo empleado a la BBDD
	 * 
	 * @param empleado Empleado a agregar
	 * @return true si se agregó con éxito, false si no se pudo agregar
	 */
	public boolean agregarEmpleado(Empleado empleado) {
		String sql = "INSERT INTO empleados (id, nombre, salario, fecha, departamento) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ps.setString(1, empleado.getId().toString());
			ps.setString(2, empleado.getNombre());
			ps.setDouble(3, empleado.getSalario());
			ps.setString(4, empleado.getFecha().toString());

			// Verifica si el empleado tiene un departamento antes de asignarlo
			String departamentoId = (empleado.getDepartamento() != null) ? empleado.getDepartamento().getId().toString()
					: null;

			if (departamentoId != null) {
				ps.setString(5, departamentoId);
			} else {
				ps.setNull(5, java.sql.Types.VARCHAR);
			}

			int filasAfectadas = ps.executeUpdate();

			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Elimina un empleado de la BBDD por su ID
	 * 
	 * @param id ID del empleado a eliminar
	 * @return true si se eliminó con éxito, false si no se pudo eliminar
	 */
	public boolean eliminarEmpleado(UUID id) {
		sql = "DELETE FROM empleados WHERE id = ?";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ps.setString(1, id.toString());
			int filasAfectadas = ps.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Muestra todos los empleados
	 * 
	 * @return Una cadena con la lista de empleados
	 */
	public String mostrarEmpleados() {
		StringBuilder sb = new StringBuilder();
		sql = "SELECT * FROM empleados";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String idString = rs.getString("id");
				UUID id = UUID.fromString(idString);

				String nombre = rs.getString("nombre");
				double salario = rs.getDouble("salario");
				LocalDate fecha = LocalDate.parse(rs.getString("fecha"));

				String departamentoIdString = rs.getString("departamento");
				UUID departamentoId = null;
				if (departamentoIdString != null) {
					departamentoId = UUID.fromString(departamentoIdString);
				}

				Departamento departamento = (departamentoId != null) ? obtenerDepartamentoPorID(departamentoId) : null;

				Empleado empleado = new Empleado(id, nombre, salario, fecha, departamento);
				sb.append(empleado.toString());
				sb.append("\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * Busca un empleado por su ID
	 * 
	 * @param id ID del empleado a buscar
	 * @return Empleado encontrado o null si no se encuentra
	 */
	public Empleado obtenerEmpleadoPorID(UUID id) {
		try (PreparedStatement ps = Menu.conexion.prepareStatement(
				"SELECT e.id, e.nombre, e.salario, e.fecha, e.departamento, d.nombre AS nombre_departamento FROM empleados e LEFT JOIN departamentos d ON e.departamento = d.id WHERE e.id = ?")) {
			ps.setObject(1, id.toString(), java.sql.Types.VARCHAR);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				UUID empleadoId = UUID.fromString(rs.getString("id"));
				String nombre = rs.getString("nombre");
				double salario = rs.getDouble("salario");
				LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
				String departamentoIdString = rs.getString("departamento");
				UUID departamentoId = null;

				if (departamentoIdString != null && !departamentoIdString.isEmpty()) {
					departamentoId = UUID.fromString(departamentoIdString);
				}

				String nombreDepartamento = rs.getString("nombre_departamento");

				Departamento departamento = (departamentoId != null)
						? new Departamento(departamentoId, nombreDepartamento, null)
						: null;

				return new Empleado(empleadoId, nombre, salario, fecha, departamento);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Actualiza un empleado en la BBDD
	 * 
	 * @param empleado Empleado a actualizar
	 * @return true si se actualizó con éxito, false si no se pudo actualizar
	 */
	public boolean actualizarEmpleado(Empleado empleado) {
		String sql = "UPDATE empleados SET nombre = ?, salario = ?, fecha = ?, departamento = ? WHERE id = ?";
		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			ps.setString(1, empleado.getNombre());
			ps.setDouble(2, empleado.getSalario());
			ps.setObject(3, empleado.getFecha(), java.sql.Types.DATE);

			UUID departamentoId = (empleado.getDepartamento() != null) ? empleado.getDepartamento().getId() : null;
			if (departamentoId != null) {
				ps.setObject(4, departamentoId.toString(), java.sql.Types.VARCHAR);
			} else {
				ps.setNull(4, java.sql.Types.VARCHAR);
			}

			ps.setObject(5, empleado.getId().toString(), java.sql.Types.VARCHAR);

			int filasAfectadas = ps.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Cambia el jefe al departamento
	 * 
	 * @param departamentoId El ID del departamento al que se le cambiará el jefe.
	 * @param nuevoJefe El nuevo jefe que se asignará al departamento (puede ser nulo para eliminar el jefe actual).
	 * @return true si se cambió el jefe exitosamente, false en caso contrario.
	 */
	public boolean cambiarJefeDepartamento(UUID departamentoId, Empleado nuevoJefe) {
		String sql = "UPDATE departamentos SET jefe = ? WHERE id = ?";
	    
	    // Obtén el departamento actual del jefe anterior
	    Departamento departamentoAnterior = obtenerDepartamentoPorID(nuevoJefe.getDepartamento().getId());

	    try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
	        // Establece el ID del nuevo jefe
	        if (nuevoJefe != null) {
	            ps.setString(1, nuevoJefe.getId().toString());
	        }
	        // Establece el ID del departamento
	        ps.setString(2, departamentoId.toString());
	        
	        int filasAfectadas = ps.executeUpdate();
	        
	        if (filasAfectadas > 0) {
	            // Si se cambió el jefe del departamento, actualiza el departamento anterior del jefe y el nuevo departamento del jefe
	            if (departamentoAnterior != null) {
	                departamentoAnterior.setJefe(null); // Quita al jefe del departamento anterior
	                actualizarDepartamento(departamentoAnterior); // Actualiza el departamento anterior
	            }
	            if (nuevoJefe != null) {
	                nuevoJefe.setDepartamento(obtenerDepartamentoPorID(departamentoId)); // Establece el nuevo departamento al jefe
	                actualizarEmpleado(nuevoJefe); // Actualiza el nuevo jefe
	            }
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}

	/**
	 * Cambia departamento al empleado
	 * 
	 * @param empleadoId El ID del empleado al que se le cambiará el departamento.
	 * @param nuevoDepartamento El nuevo departamento al que se asignará el empleado.
	 * @return true si se cambió el departamento exitosamente, false en caso contrario.
	 */
	public boolean cambiarDepartamentoEmpleado(UUID empleadoId, Departamento nuevoDepartamento) {
		String sql = "UPDATE empleados SET departamento = ? WHERE id = ?";

		try (PreparedStatement ps = Menu.conexion.prepareStatement(sql)) {
			// Establece el ID del nuevo departamento
			if (nuevoDepartamento != null) {
				ps.setString(1, nuevoDepartamento.getId().toString());
			} else {
				ps.setNull(1, java.sql.Types.VARCHAR);
			}

			// Establece el ID del empleado
			ps.setString(2, empleadoId.toString());

			int filasAfectadas = ps.executeUpdate();

			return filasAfectadas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
