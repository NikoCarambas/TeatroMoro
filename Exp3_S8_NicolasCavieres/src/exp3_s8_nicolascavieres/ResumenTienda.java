/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que permite mostrar estadísticas de ventas y reservas.
 */
public class ResumenTienda {

    private final ModificarReserva reservas;
    private final ModificarVenta ventas;

    public ResumenTienda(ModificarReserva reservas, ModificarVenta ventas) {
        this.reservas = reservas;
        this.ventas = ventas;
    }

    public void mostrarMenuResumen(Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n========= MENÚ RESUMEN TIENDA =========");
            System.out.println("1. Ver todas las boletas individuales");
            System.out.println("2. Ver resumen general de ventas");
            System.out.println("3. Volver al menú principal");
            opcion = ValidarInputUsuario.validarNumero(scanner, "Seleccione opción:", 1, 3);

            switch (opcion) {
                case 1 -> mostrarBoletasIndividuales();
                case 2 -> mostrarResumenGeneral();
                case 3 -> System.out.println("Volviendo al menú principal...");
            }
        } while (opcion != 3);
    }

    private void mostrarBoletasIndividuales() {
        List<HashMap<String, Object>> listaVentas = ventas.getListaVentas();
        System.out.println("\n========= BOLETAS INDIVIDUALES =========");
        if (listaVentas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        for (HashMap<String, Object> venta : listaVentas) {
            System.out.println("----------------------------------------");
            System.out.println("Cliente: " + venta.get("nombre"));
            System.out.println("ID Venta: " + venta.get("ventaID"));
            System.out.println("Asientos: " + venta.get("asientos"));
            System.out.println("Clientes: " + venta.get("clientes"));
            System.out.printf("Total pagado: $%.0f\n", venta.get("total"));

            Object descuento = venta.get("totalDescuento");
            if (descuento != null && (double) descuento > 0) {
                System.out.printf("Descuento aplicado: $%.0f\n", descuento);
            }
        }
        System.out.println("========================================\n");
    }

    private void mostrarResumenGeneral() {
        System.out.println("\n========= RESUMEN GENERAL DE VENTAS =========");

        int totalReservados = reservas.getAsientosReservados().size();
        int totalVendidos = ventas.getAsientosVendidos().size();

        double totalIngresos = 0;
        double totalDescuentos = 0;
        List<HashMap<String, Object>> listaVentas = ventas.getListaVentas();

        for (HashMap<String, Object> venta : listaVentas) {
            totalIngresos += (double) venta.get("total");

            Object descuento = venta.get("totalDescuento");
            if (descuento != null) {
                totalDescuentos += (double) descuento;
            }
        }

        System.out.println("Total asientos reservados : " + totalReservados);
        System.out.println("Total asientos vendidos    : " + totalVendidos);
        System.out.printf("Ingresos por ventas        : $%.0f\n", totalIngresos);
        System.out.printf("Total descuentos aplicados : $%.0f\n", totalDescuentos);
        System.out.println("=============================================\n");
    }
}
