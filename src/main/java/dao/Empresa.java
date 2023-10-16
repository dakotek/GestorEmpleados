package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import model.Departamento;
import model.Empleado;

public class Empresa {
    Connection conn;
    String dsn = "jdbc:sqlite:empresa.sqlite";
    String sql;
    
    /**
	 * Constructor
	 */
    public Empresa() {
        try {
            conn = DriverManager.getConnection(dsn);
            // Crear las tablas "departamentos" y "empleados" si no existen
            crearTablaDepartamentos();
            crearTablaEmpleados();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Cierra la conexión a la BBDD
	 */
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea la tabla "departamentos" si no existe en la base de datos
     */
    private void crearTablaDepartamentos() {
        sql = "CREATE TABLE IF NOT EXISTS departamentos (" +
                "id STRING PRIMARY KEY," +
                "nombre STRING," +
                "jefe STRING)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea la tabla "empleados" si no existe en la base de datos
     */
    private void crearTablaEmpleados() {
        sql = "CREATE TABLE IF NOT EXISTS empleados (" +
                "id STRING PRIMARY KEY," +
                "nombre STRING," +
                "salario INTEGER," +
                "fecha STRING," +
                "departamento STRING)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo departamento a la BBDD
     * 
     * @param departamento Departamento a agregar
     * @return true si se agregó con éxito, false si no se pudo agregar
     */
    public boolean agregarDepartamento(Departamento departamento) {
    	sql = "INSERT INTO departamentos (id, nombre, jefe) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, departamento.getId().toString());
            ps.setString(2, departamento.getNombre());
            ps.setString(3, departamento.getJefe() != null ? departamento.getJefe().toString() : null);
            
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
    public boolean eliminarDepartamento(String id) {
    	sql = "DELETE FROM departamentos WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
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
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                UUID jefe = null;
                if (rs.getString("jefe") != null) {
                    jefe = UUID.fromString(rs.getString("jefe"));
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
        String sql = "SELECT * FROM departamentos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UUID departamentoId = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                UUID jefeId = rs.getString("jefe") != null ? UUID.fromString(rs.getString("jefe")) : null;
                return new Departamento(departamentoId, nombre, jefeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza un departamento en la BBDD
     * 
     * @param departamento Departamento a actualizar
     * @return true si se actualizó con éxito, false si no se pudo actualizar
     */
    public boolean actualizarDepartamento(Departamento departamento) {
        String sql = "UPDATE departamentos SET nombre = ?, jefe = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, departamento.getNombre());
            ps.setString(2, departamento.getJefe() != null ? departamento.getJefe().toString() : null);
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
    	sql = "INSERT INTO empleados (id, nombre, salario, fecha, departamento) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getId().toString());
            ps.setString(2, empleado.getNombre());
            ps.setInt(3, empleado.getSalario());
            ps.setString(4, empleado.getFecha());
            ps.setString(5, empleado.getDepartamento() != null ? empleado.getDepartamento().toString() : null);
            
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
    public boolean eliminarEmpleado(String id) {
    	sql = "DELETE FROM empleados WHERE id = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
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
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                int salario = rs.getInt("salario");
                String fecha = rs.getString("fecha");
                UUID departamento = null;
                if (rs.getString("departamento") != null) {
                    departamento = UUID.fromString(rs.getString("departamento"));
                }
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
        String sql = "SELECT * FROM empleados WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UUID empleadoId = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                int salario = rs.getInt("salario");
                String fecha = rs.getString("fecha");
                UUID departamentoId = rs.getString("departamento") != null ? UUID.fromString(rs.getString("departamento")) : null;
                return new Empleado(empleadoId, nombre, salario, fecha, departamentoId);
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setInt(2, empleado.getSalario());
            ps.setString(3, empleado.getFecha());
            ps.setString(4, empleado.getDepartamento() != null ? empleado.getDepartamento().toString() : null);
            ps.setString(5, empleado.getId().toString());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }    
}
