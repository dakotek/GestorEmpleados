package view;

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
            }
        }
    }

    private static void mostrarDepartamentos(Empresa empresa) {
        
    }

    private static void agregarDepartamento(Empresa empresa) {
        
    }

    private static void eliminarDepartamento(Empresa empresa) {
        
    }

    private static void mostrarEmpleados(Empresa empresa) {
        
    }

    private static void agregarEmpleado(Empresa empresa) {
        
    }

    private static void eliminarEmpleado(Empresa empresa) {
        
    }

    private static void cerrarEmpresa(Empresa empresa) {
        empresa.close();
        System.out.println("Empresa cerrada");
    }
}
