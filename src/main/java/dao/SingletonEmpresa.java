package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SingletonEmpresa {
	private String dsn;
	private String dbUser;
	private String dbPassword;
	private String dbDriver;
	private String dbName;
	private static Connection conn;
	private String sql;

	/**
	 * Constructor
	 */
	public SingletonEmpresa() {
		try {
			Properties props = new Properties();
			try (FileInputStream input = new FileInputStream("empresa.properties")) {
				props.load(input);
			} catch (IOException e) {
				System.err.println("Error al cargar archivo properties: " + e.getMessage());
			}
			
			dsn = props.getProperty("db.url");
			dbUser = props.getProperty("db.user");
			dbPassword = props.getProperty("db.password");
			dbDriver = props.getProperty("db.driver");
			dbName = props.getProperty("db.name");
			
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dsn, dbUser, dbPassword);
			crearTablaDepartamentos();
			crearTablaEmpleados();
		} catch (SQLException e) {
			System.err.println("Error al conectar a la base de datos: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("Error al cargar el controlador: " + e.getMessage());
		}
	}

	public static synchronized Connection getConnection() {
		if (conn == null) {
			new SingletonEmpresa();
		}
		return conn;
	}

	/**
	 * Cierra la conexión a la BBDD
	 */
	public static void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar la conexión: " + e.getMessage());
		}
	}

	/**
	 * Crea la tabla "departamentos" si no existe en la base de datos
	 */
	private void crearTablaDepartamentos() {
		
		String tipo = (dbName.equals("sqlite")) ? "TEXT" : "VARCHAR(50)";
		
		sql = "CREATE TABLE IF NOT EXISTS departamentos (" + "id " + tipo + " PRIMARY KEY," + "nombre " + tipo + ","
				+ "jefe " + tipo + ")";
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
		
		String tipo = (dbName.equals("sqlite")) ? "TEXT" : "VARCHAR(50)";
		
		sql = "CREATE TABLE IF NOT EXISTS empleados (" + "id " + tipo + " PRIMARY KEY," + "nombre " + tipo + "," + "salario DOUBLE,"
				+ "fecha " + tipo + "," + "departamento " + tipo + ")";
		try (Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
