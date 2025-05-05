/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;

import java.util.*;
import java.util.concurrent.*;

public class ModificarReserva {
    // ESTRUCTURAS DE DATOS
    private final List<HashMap<String, Object>> listaReservas = new ArrayList<>();
    private final List<String> asientosReservados = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> timersActivos = new ConcurrentHashMap<>();
    private final AsientosTeatro teatro;
    private final ModificarVenta ventas;
    private int contadorID = 1;

    public ModificarReserva(AsientosTeatro teatro, ModificarVenta ventas) {
        this.teatro = teatro;
        this.ventas = ventas;
    }

    // MÉTODOS DEL MENÚ PRINCIPAL
    public void modificarReservaMenu(Scanner scanner) {
        System.out.print("Ingrese ID de reserva: ");
        String reservaID = scanner.nextLine();
        
        HashMap<String, Object> reserva = buscarReservaPorID(reservaID);
        if (reserva == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }

        imprimirResumenReserva(reserva);

        int opcion;
        do {
            System.out.println("\n=== MENÚ DE RESERVA ===");
            System.out.println("1. Modificar asientos");
            System.out.println("2. Cancelar reserva");
            System.out.println("3. Confirmar compra");
            System.out.println("4. Volver al menú principal");
            opcion = ValidarInputUsuario.validarNumero(scanner, "Seleccione opción:", 1, 4);

            switch (opcion) {
                case 1 -> modificarAsientosSubmenu(scanner, reserva);
                case 2 -> cancelarReserva(reservaID);
                case 3 -> confirmarComoVenta(reservaID);
                case 4 -> System.out.println("Volviendo...");
            }
        } while (opcion != 4);
    }

    // MÉTODOS DE GESTIÓN DE RESERVAS
    public void agregarReserva(String nombreCliente, List<String> asientos, 
                              List<TipoCliente> clientes, double total) {
        
        String reservaID = generarIDUnico();
        HashMap<String, Object> reserva = new HashMap<>();
        reserva.put("reservaID", reservaID);
        reserva.put("nombre", nombreCliente);
        reserva.put("asientos", new ArrayList<>(asientos));
        reserva.put("clientes", new ArrayList<>(clientes));
        reserva.put("total", total);
        reserva.put("estado", "Pendiente");

        synchronized (this) {
            listaReservas.add(reserva);
            asientosReservados.addAll(asientos);
            teatro.ocuparAsientos(asientos); // Método nuevo en AsientosTeatro
        }

        programarCancelacion(reservaID);
        imprimirResumenReserva(reserva);
    }

    // SUBSISTEMA DE MODIFICACIÓN DE ASIENTOS
    private void modificarAsientosSubmenu(Scanner scanner, HashMap<String, Object> reserva) {
        System.out.println("\n=== MODIFICAR ASIENTOS ===");
        System.out.println("1. Agregar asiento");
        System.out.println("2. Quitar asiento");
        int opcion = ValidarInputUsuario.validarNumero(scanner, "Seleccione:", 1, 2);
        
        switch (opcion) {
            case 1 -> agregarAsiento(scanner, reserva);
            case 2 -> quitarAsiento(scanner, reserva);
        }
    }

    private void agregarAsiento(Scanner scanner, HashMap<String, Object> reserva) {
        // Obtener listas actualizadas del teatro
        List<String> reservados = teatro.getAsientosReservados();
        List<String> vendidos = teatro.getAsientosVendidos();        
        
        teatro.mostrarTeatro();
        // Obtener asientos válidos dinámicamente
        String[] asientosValidos = teatro.getTodosAsientos().toArray(new String[0]);        
        String codigo = ValidarInputUsuario.validarLetra(scanner, 
            "Ingrese código de asiento (ej: V3):", 
            asientosValidos);
        
        if (codigo == null || !teatro.verificarDisponibilidad(codigo, 
                                                    reservados, 
                                                    vendidos)) {
            System.out.println("Asiento no disponible!");
            return;
        }

        List<String> asientos = (List<String>) reserva.get("asientos");
        asientos.add(codigo);
        reserva.put("total", recalcularTotal(reserva));
        teatro.ocuparAsiento(codigo);
        System.out.println("Asiento agregado!");
    }

    private void quitarAsiento(Scanner scanner, HashMap<String, Object> reserva) {
        List<String> asientos = (List<String>) reserva.get("asientos");
        System.out.println("Asientos actuales: " + String.join(", ", asientos));
        
        String codigo = ValidarInputUsuario.validarLetra(scanner, 
            "Ingrese código a quitar:", 
            asientos.toArray(new String[0]));
        
        if (codigo != null) {
            asientos.remove(codigo);
            reserva.put("total", recalcularTotal(reserva));
            teatro.liberarAsiento(codigo);
            System.out.println("Asiento removido!");
        }
    }

    // MÉTODOS AUXILIARES
    private String generarIDUnico() {
      return String.format("%04d", contadorID++);
    }

    private double recalcularTotal(HashMap<String, Object> reserva) {
        List<String> asientos = (List<String>) reserva.get("asientos");
        List<TipoCliente> clientes = (List<TipoCliente>) reserva.get("clientes");
        double total = 0;

        for (int i = 0; i < asientos.size(); i++) {
            // Llamada directa al método estático
            total += ComprarEntrada.calcularPrecioEntrada(asientos.get(i), clientes.get(i));
        }
        return total;
    }

    private void programarCancelacion(String reservaID) {
        ScheduledFuture<?> timer = scheduler.schedule(() -> {
            cancelarReserva(reservaID);
            timersActivos.remove(reservaID);
        }, 10, TimeUnit.MINUTES);
        timersActivos.put(reservaID, timer);
    }

    // MÉTODOS OBLIGATORIOS DEL DIAGRAMA
    public void cancelarReserva(String reservaID) {
        synchronized (this) {
            HashMap<String, Object> reserva = buscarReservaPorID(reservaID);
            if (reserva != null) {
                List<String> asientos = (List<String>) reserva.get("asientos");
                teatro.liberarAsientos(asientos);
                listaReservas.remove(reserva);
                System.out.println("Reserva cancelada!");
            }
        }
    }

    public void confirmarComoVenta(String reservaID) {
        HashMap<String, Object> reserva = buscarReservaPorID(reservaID);
        if (reserva != null) {
            ModificarVenta ventas = new ModificarVenta(teatro);
            ventas.agregarVenta(
                (String) reserva.get("nombre"),
                reservaID,
                (List<String>) reserva.get("asientos"),
                (List<TipoCliente>) reserva.get("clientes"),
                (double) reserva.get("total")
            );
            cancelarTimer(reservaID);
            System.out.println("¡Compra confirmada!");
        }
    }

    // MÉTODOS DE BÚSQUEDA Y VISUALIZACIÓN
    public HashMap<String, Object> buscarReservaPorID(String reservaID) {
        synchronized (this) {
            for (HashMap<String, Object> reserva : listaReservas) {
                if (reserva.get("reservaID").equals(reservaID)) {
                    return reserva;
                }
            }
            return null;
        }
    }

    public void imprimirResumenReserva(HashMap<String, Object> reserva) {
        System.out.println("\n=== RESUMEN DE RESERVA ===");
        System.out.println("ID: " + reserva.get("reservaID"));
        System.out.println("Cliente: " + reserva.get("nombre"));
        System.out.println("Asientos: " + String.join(", ", (List<String>) reserva.get("asientos")));
        System.out.printf("Total: $%.0f\n", reserva.get("total"));
        System.out.println("Estado: " + reserva.get("estado"));
        System.out.println("===========================");
    }

    // MÉTODOS DE GESTIÓN DE TIMERS
    private void cancelarTimer(String reservaID) {
        if (timersActivos.containsKey(reservaID)) {
            timersActivos.get(reservaID).cancel(true);
            timersActivos.remove(reservaID);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    // MÉTODOS DE ACCESO A DATOS
    public List<String> getAsientosReservados() {
        return Collections.unmodifiableList(asientosReservados);
    }

    public List<HashMap<String, Object>> getReservasPendientes() {
        synchronized (this) {
            List<HashMap<String, Object>> pendientes = new ArrayList<>();
            for (HashMap<String, Object> reserva : listaReservas) {
                if ("Pendiente".equals(reserva.get("estado"))) {
                    pendientes.add(new HashMap<>(reserva));
                }
            }
            return pendientes;
        }
    }

    // MÉTODO PARA LIBERAR RECURSOS
    public void liberarRecursos() {
        shutdown();
        listaReservas.clear();
        asientosReservados.clear();
        timersActivos.clear();
    }
}
