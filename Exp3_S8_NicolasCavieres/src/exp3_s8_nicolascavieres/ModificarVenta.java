/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;

import java.time.LocalDateTime;
import java.util.*;

public class ModificarVenta {
    private final List<HashMap<String, Object>> listaVentas = new ArrayList<>();
    private final AsientosTeatro teatro;

    public ModificarVenta(AsientosTeatro teatro) {
        this.teatro = teatro;
    }

    // MÉTODO PARA AGREGAR VENTA (ACTUALIZA EL TEATRO)
    public void agregarVenta(String nombreCliente, String clienteID, List<String> asientos,
                            List<TipoCliente> clientes, double total) {
        
        // Marcar asientos como vendidos
        for (String asiento : asientos) {
            teatro.marcarComoVendido(asiento);
        }
        double totalDescuento = 0;
        for (int i = 0; i < asientos.size(); i++) {
            double precioBase = calcularPrecioBase(asientos.get(i));
            totalDescuento += precioBase * clientes.get(i).getDescuento();
        }
        
        HashMap<String, Object> venta = new HashMap<>();
        int idAleatorio = 1000 + new Random().nextInt(9000); // Genera un número de 4 dígitos
        venta.put("ventaID", String.valueOf(idAleatorio));
        venta.put("nombreCliente", nombreCliente);
        venta.put("clienteID", clienteID);
        venta.put("asientos", new ArrayList<>(asientos));
        venta.put("clientes", new ArrayList<>(clientes));
        venta.put("total", total);
        venta.put("totalDescuento", totalDescuento);
        venta.put("fecha", LocalDateTime.now());
        
        listaVentas.add(venta);
        imprimirBoleta(venta);
    }
    
    private double calcularPrecioBase(String codigoAsiento) {
        return switch (codigoAsiento.charAt(0)) {
            case 'V' -> SaludoInicial.PRECIO_VIP;
            case 'P' -> SaludoInicial.PRECIO_PLATEA;
            case 'G' -> SaludoInicial.PRECIO_GENERAL;
            default -> 0;
        };
    }
    
    // MÉTODO DE MODIFICACIÓN SIMPLIFICADO
    public void modificarVenta(String ventaID, Scanner scanner) {
        HashMap<String, Object> venta = buscarVentaPorID(ventaID);
        if (venta == null) {
            System.out.println("Venta no encontrada.");
            return;
        }

        int opcion;
        do {
            System.out.println("\n=== MODIFICAR VENTA " + ventaID + " ===");
            System.out.println("1. Agregar asiento");
            System.out.println("2. Quitar asiento");
            System.out.println("3. Cancelar venta");
            System.out.println("4. Salir");
            opcion = ValidarInputUsuario.validarNumero(scanner, "Seleccione:", 1, 4);

            switch (opcion) {
                case 1 -> agregarAsientoAVenta(venta, scanner);
                case 2 -> quitarAsientoDeVenta(venta, scanner);
                case 3 -> {
                    cancelarVenta(venta);
                    return;
                }
                case 4 -> System.out.println("Volviendo...");
            }
        } while (opcion != 4);
    }

    // MÉTODOS DE MODIFICACIÓN BÁSICOS
    private void agregarAsientoAVenta(HashMap<String, Object> venta, Scanner scanner) {
        teatro.mostrarTeatro();
        
        String codigo = ValidarInputUsuario.validarLetra(scanner, 
            "Ingrese código de asiento disponible:", 
            teatro.getTodosAsientos().toArray(new String[0]));
        
        if (codigo != null && teatro.verificarDisponibilidad(codigo, 
                teatro.getAsientosReservados(), 
                teatro.getAsientosVendidos())) {
            
            List<String> asientos = (List<String>) venta.get("asientos");
            asientos.add(codigo);
            teatro.marcarComoVendido(codigo);
            venta.put("total", recalcularTotal(venta));
            System.out.println("Asiento agregado!");
        } else {
            System.out.println("No se pudo agregar el asiento");
        }
    }

    private void quitarAsientoDeVenta(HashMap<String, Object> venta, Scanner scanner) {
        List<String> asientos = (List<String>) venta.get("asientos");
        System.out.println("Asientos actuales: " + String.join(", ", asientos));
        
        String codigo = ValidarInputUsuario.validarLetra(scanner, 
            "Ingrese código a quitar:", 
            asientos.toArray(new String[0]));
        
        if (codigo != null) {
            asientos.remove(codigo);
            teatro.liberarAsiento(codigo);
            venta.put("total", recalcularTotal(venta));
            System.out.println("Asiento removido!");
        }
    }

    private void cancelarVenta(HashMap<String, Object> venta) {
        List<String> asientos = (List<String>) venta.get("asientos");
        teatro.liberarAsientosVendidos(asientos);
        listaVentas.remove(venta);
        System.out.println("Venta cancelada!");
    }

    // MÉTODOS AUXILIARES
    private double recalcularTotal(HashMap<String, Object> venta) {
        List<String> asientos = (List<String>) venta.get("asientos");
        List<TipoCliente> clientes = (List<TipoCliente>) venta.get("clientes");
        double total = 0;
        
        for (int i = 0; i < asientos.size(); i++) {
            total += ComprarEntrada.calcularPrecioEntrada(asientos.get(i), clientes.get(i));
        }
        return total;
    }

    // MÉTODOS COMUNES
    public HashMap<String, Object> buscarVentaPorID(String ventaID) {
        for (HashMap<String, Object> venta : listaVentas) {
            if (venta.get("ventaID").equals(ventaID)) {
                return venta;
            }
        }
        return null;
    }

    public void imprimirBoleta(HashMap<String, Object> venta) {
        System.out.println("\n========= BOLETA =========");
        System.out.println("ID: " + venta.get("ventaID"));
        System.out.println("Cliente: " + venta.get("nombreCliente"));
        System.out.println("Fecha: " + venta.get("fecha"));
        System.out.println("Asientos: " + String.join(", ", (List<String>) venta.get("asientos")));
        System.out.printf("Total: $%.0f\n", venta.get("total"));
        System.out.println("==========================");
    }

    public List<HashMap<String, Object>> getListaVentas() {
        return Collections.unmodifiableList(listaVentas);
    }
    
    // MÉTODO PARA OBTENER ASIENTOS VENDIDOS (desde el teatro)
    public List<String> getAsientosVendidos() {
        return teatro.getAsientosVendidos();
    }    
}