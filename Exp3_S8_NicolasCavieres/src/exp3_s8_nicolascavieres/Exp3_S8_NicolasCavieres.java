/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package exp3_s8_nicolascavieres;

/**
 *
 * @author nikoc
 */

import java.util.Scanner;

public class Exp3_S8_NicolasCavieres {

    // INSTANCIAS
    private final Scanner scanner;
    private final AsientosTeatro teatro = new AsientosTeatro();
    private final ModificarVenta ventas = new ModificarVenta(teatro);
    private final ModificarReserva reservas = new ModificarReserva(teatro, ventas);
    ResumenTienda resumenTienda = new ResumenTienda(reservas, ventas);
    
    // CONSTRUCTOR
    public Exp3_S8_NicolasCavieres() {
        scanner = new Scanner(System.in);
    }
    
    // MÉTODOS   
    public void iniciar() {
        
        SaludoInicial.mostrarSaludo();    
        int opcion;
        do {
            mostrarMenu();
            // Se utiliza el método validarNumero de la clase ValidarInputUsuario para validar la opción ingresada.
            opcion = ValidarInputUsuario.validarNumero(scanner, "Seleccione una opción:", 1, 6);

            // Si se superan los intentos la función retorna -1 y se repite el ciclo.
            if (opcion == -1) {
                continue;
            }

            switch (opcion) {
                case 1 -> {
                    ComprarEntrada compra = new ComprarEntrada(teatro, scanner, reservas, ventas);
                    compra.procesarCompra();
                }
                case 2 -> {
                    System.out.println("\n----- MODIFICAR RESERVA -----");
                    reservas.modificarReservaMenu(scanner); // Método que contiene toda la lógica
                }
                case 3 -> {
                    System.out.println("\n------ MODIFICAR VENTA ------");
                    System.out.print("Ingrese ID de venta: ");
                    String ventaID = scanner.nextLine();
                    ventas.modificarVenta(ventaID, scanner);
                }
                case 4 -> {
                    teatro.mostrarTeatro();
                }
                case 5 -> {
                    resumenTienda.mostrarMenuResumen(scanner);
                }
                case 6 -> System.out.println("Saliendo del sistema...");
            }
        } while (opcion != 6);
    }

    private void mostrarMenu() {
        System.out.println("\n--- Menu Principal ---");
        System.out.println("1. Comprar entradas");
        System.out.println("2. Modificar una reserva");
        System.out.println("3. Modificar una venta");
        System.out.println("4. Ver asientos disponibles");
        System.out.println("5. Ver Resumen de Tienda");
        System.out.println("6. Salir");
    }


    public static void main(String[] args) {
        Exp3_S8_NicolasCavieres menu = new Exp3_S8_NicolasCavieres();
        menu.iniciar();
    }
}
