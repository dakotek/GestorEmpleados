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

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
