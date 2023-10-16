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
            System.out.println("Selecciona una opción (1 a 7):\n" + 
                    "1. Mostrar departamentos\n" + 
                    "2. Agregar departamentos\n" + 
                    "3. Eliminar departamentos\n" + 
                    "4. Mostrar empleados\n" + 
                    "5. Agregar empleados\n" + 
                    "6. Eliminar empleados\n" + 
                    "7. Cerrar programa");

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
                    cerrarEmpresa(empresa);
                    return;
                default:
                	System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
    }

    private static void mostrarDepartamentos(Empresa empresa) {
    	String departamentos = empresa.mostrarDepartamentos();
        System.out.println(departamentos);
    }

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

    private static void mostrarEmpleados(Empresa empresa) {
    	String empleados = empresa.mostrarEmpleados();
        System.out.println(empleados);
    }

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

    private static void cerrarEmpresa(Empresa empresa) {
        empresa.close();
        System.out.println("Empresa cerrada");
    }
}
