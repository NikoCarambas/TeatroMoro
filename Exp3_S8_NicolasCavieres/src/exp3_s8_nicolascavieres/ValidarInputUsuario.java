/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;

/**
 *
 * @author nikoc
 */
import java.util.Scanner;

class ValidarInputUsuario {
    public static final int cantidadDeErrores = 5;

    public static void mensajeIntentosSuperados() {
        System.out.println("Has superado el número de intentos permitidos.\nVolviendo al Menú Principal...\n");
    }

    public static int validarNumero(Scanner scanner, String mensaje, int min, int max) {
        int contadorIntentos = cantidadDeErrores;
        while (contadorIntentos > 0) {
            System.out.print(mensaje + "\n-> ");
            String entrada = scanner.nextLine().trim();
            try {
                int numero = Integer.parseInt(entrada);
                if (numero >= min && numero <= max) {
                    return numero;
                } else {
                    System.out.println("Debe ingresar un número entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número entero.");
            }
            contadorIntentos--;
        }
        mensajeIntentosSuperados();
        return -1;
    }

    public static String validarLetra(Scanner scanner, String mensaje, String[] opcionesValidas) {
        int contadorIntentos = cantidadDeErrores;
        while (contadorIntentos > 0) {
            System.out.print(mensaje + "\n-> ");
            String opcion = scanner.nextLine().trim().toUpperCase();
            for (String opcionValida : opcionesValidas) {
                if (opcion.equals(opcionValida)) {
                    return opcion;
                }
            }
            System.out.println("Entrada inválida. Intente nuevamente.");
            contadorIntentos--;
        }
        mensajeIntentosSuperados();
        return null;
    }
}