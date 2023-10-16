package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    // Método para crear la tabla de departamentos
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

    // Método para crear la tabla de empleados
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
    	
        return false;
    }

    public boolean eliminarDepartamento(String id) {
    	
        return false;
    }

    public String mostrarDepartamentos() {
        
        return "";
    }

    public boolean agregarEmpleado(Empleado empleado) {
        
        return false;
    }

    public boolean eliminarEmpleado(String id) {
        
        return false;
    }

    public String mostrarEmpleados() {
        
        return "";
    }
}
