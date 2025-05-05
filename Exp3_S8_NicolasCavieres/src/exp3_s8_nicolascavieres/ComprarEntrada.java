/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;

/**
 *
 * @author nikoc
 */
    
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class ComprarEntrada {
    private final AsientosTeatro teatro;
    private final Scanner scanner;
    private final ModificarReserva reservas;
    private final ModificarVenta ventas;

    // Constructor que recibe las dependencias necesarias
    public ComprarEntrada(AsientosTeatro teatro, Scanner scanner,
                      ModificarReserva reservas, ModificarVenta ventas) {
        this.teatro = teatro;
        this.scanner = scanner;
        this.reservas = reservas;
        this.ventas = ventas;
    }

    public void procesarCompra() {
        // Generar ID único para el cliente
        String clienteID = UUID.randomUUID().toString().substring(0, 8);
        
        // Solicitar nombre del cliente
        System.out.print("Ingrese su nombre: ");
        String nombreCliente = scanner.nextLine();

        // Validar cantidad de entradas
        int cantidad = ValidarInputUsuario.validarNumero(scanner,
                "¿Cuántas entradas desea comprar? (1-24):", 1, 24);
        if (cantidad == -1) return;

        ArrayList<String> asientosSeleccionados = new ArrayList<>();
        ArrayList<TipoCliente> clientes = new ArrayList<>();
        double total = 0;

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\n=== Entrada " + (i + 1) + " ===");
            if (!seleccionarAsiento(asientosSeleccionados, clientes)) {
                System.out.println("Operación cancelada.");
                return;
            }
            double precio = calcularPrecioEntrada(asientosSeleccionados.get(i), clientes.get(i));
            total += precio;
            mostrarResumenParcial(asientosSeleccionados, clientes, i, precio);
        }
        
        /*
        Formato de ResumenFinal
        */
        mostrarResumenFinal(nombreCliente, clienteID, asientosSeleccionados, clientes, total);

        // Opciones: Reservar/Comprar/Cancelar
        int opcion = ValidarInputUsuario.validarNumero(scanner,
                "\n¿Qué desea hacer?\n1. Reservar\n2. Comprar\n3. Cancelar", 1, 3);
        if (opcion == 3) return;

        String mensaje = (opcion == 1) 
            ? "Vamos a reservar sus asientos por 10 minutos. ¿Confirmar? (S/N):" 
            : "¿Confirmar compra? (S/N):";
        
        String confirmacion = ValidarInputUsuario.validarLetra(scanner, mensaje, new String[]{"S", "N"});
        if (!"S".equalsIgnoreCase(confirmacion)) {
            System.out.println("Operación cancelada.");
            return;
        }

        procesarTransaccion(opcion, nombreCliente, clienteID, asientosSeleccionados, clientes, total);
    }

    private boolean seleccionarAsiento(ArrayList<String> asientosSeleccionados, 
                                      ArrayList<TipoCliente> clientes) {
        teatro.mostrarTeatro();
        
        String tipoAsiento = ValidarInputUsuario.validarLetra(scanner,
                "Ingrese tipo de asiento (V: VIP, P: Platea, G: General):",
                new String[]{"V", "P", "G"});
        if (tipoAsiento == null) return false;

        int numeroAsiento = ValidarInputUsuario.validarNumero(scanner,
                "Ingrese el número del asiento (1-8):", 1, 8);
        if (numeroAsiento == -1) return false;

        String codigoAsiento = tipoAsiento + numeroAsiento;

        // Verificar disponibilidad
        if (!teatro.verificarDisponibilidad(codigoAsiento,
                reservas.getAsientosReservados(),
                ventas.getAsientosVendidos())) {
            System.out.println("¡Asiento no disponible! Seleccione otro.");
            return false;
        }

        String confirmacion = ValidarInputUsuario.validarLetra(scanner,
                "¿Confirmar asiento " + codigoAsiento + "? (S/N):", new String[]{"S", "N"});
        if (!"S".equalsIgnoreCase(confirmacion)) return false;

        int edad = ValidarInputUsuario.validarNumero(scanner, "Ingrese la edad del espectador:", 1, 120);
        if (edad == -1) return false;

        asientosSeleccionados.add(codigoAsiento);
        clientes.add(new TipoCliente(edad));
        return true;
    }

    public static double calcularPrecioEntrada(String codigoAsiento, TipoCliente cliente) {
        double precioBase = switch (codigoAsiento.charAt(0)) {
            case 'V' -> SaludoInicial.PRECIO_VIP;
            case 'P' -> SaludoInicial.PRECIO_PLATEA;
            case 'G' -> SaludoInicial.PRECIO_GENERAL;
            default -> 0;
        };
        return precioBase * (1 - cliente.getDescuento());
    }

    private void mostrarResumenParcial(ArrayList<String> asientos, ArrayList<TipoCliente> clientes,
                                      int index, double precio) {
        System.out.println("\n--- Resumen entrada " + (index + 1) + " ---");
        System.out.println("Asiento: " + asientos.get(index));
        System.out.println("Tipo cliente: " + clientes.get(index).getTipoCliente());
        System.out.printf("Precio con descuento: $%.0f\n", precio);
        System.out.println("----------------------------");
    }

    private void mostrarResumenFinal(String nombre, String clienteID, ArrayList<String> asientos,
                                   ArrayList<TipoCliente> clientes, double total) {
        System.out.println("\n========= RESUMEN FINAL =========");
        System.out.println("Cliente: " + nombre + " (ID: " + clienteID + ")");
        for (int i = 0; i < asientos.size(); i++) {
            System.out.printf("Entrada %d: Asiento %s - %s (Edad: %d)\n",
                    i + 1, asientos.get(i), clientes.get(i).getTipoCliente(), clientes.get(i).getEdadCliente());
        }
        System.out.printf("\nTOTAL A PAGAR: $%.0f\n", total);
        System.out.println("================================");
    }

    private void procesarTransaccion(int opcion, String nombreCliente, String clienteID,
                                    ArrayList<String> asientos, ArrayList<TipoCliente> clientes,
                                    double total) {
        if (opcion == 1) { // Reservar
            reservas.agregarReserva(nombreCliente, asientos, clientes, total);
            System.out.println("¡Reserva registrada exitosamente!");
        } else if (opcion == 2) { // Comprar
            ventas.agregarVenta(nombreCliente, clienteID, asientos, clientes, total);
            System.out.println("¡Compra realizada exitosamente!");
        }
    }
}