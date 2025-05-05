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
import java.util.Collections;
import java.util.List;

public class AsientosTeatro {
    private final String[][] planoTeatro = {
        {"V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8"},
        {"P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8"},
        {"G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8"}
    };

    private final List<String> asientosReservados = new ArrayList<>();
    private final List<String> asientosVendidos = new ArrayList<>();

    // ==================== MÉTODOS DE VISUALIZACIÓN ====================
    public void mostrarTeatro()  {
        System.out.println("\n----------- Plano del Teatro -----------");
        String[] secciones = {" VIP    : ", " Platea : ", " General: "};

        for (int fila = 0; fila < planoTeatro.length; fila++) {
            System.out.print(secciones[fila]);
            for (String asiento : planoTeatro[fila]) {
                if (asientosVendidos.contains(asiento)) {
                    System.out.print("[X] ");
                } else if (asientosReservados.contains(asiento)) {
                    System.out.print("[R] ");
                } else {
                    System.out.print("[" + asiento + "] ");
                }
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
        System.out.println("Leyenda: [X] Ocupado | [R] Reservado | [ ] Disponible\n");
    }
    // ==================== MÉTODOS DE GESTIÓN DE ASIENTOS ====================
    
    public void ocuparAsientos(List<String> asientos) {
        asientosReservados.addAll(asientos);
    }

    public void liberarAsientos(List<String> asientos) {
        asientosReservados.removeAll(asientos);
    }

    public void ocuparAsiento(String codigoAsiento) {
        if (!asientosReservados.contains(codigoAsiento)) {
            asientosReservados.add(codigoAsiento);
        }
    }

    public void liberarAsiento(String codigoAsiento) {
        asientosReservados.remove(codigoAsiento);
    }

    public void marcarComoVendido(String codigoAsiento) {
        asientosVendidos.add(codigoAsiento);
        asientosReservados.remove(codigoAsiento);
    }    
    
    public void liberarAsientosVendidos(List<String> asientos) {
        asientosVendidos.removeAll(asientos);
    }
    
    // ==================== MÉTODOS DE VALIDACIÓN ====================
    public boolean verificarDisponibilidad(String codigoAsiento,
                                           List<String> asientosReservados,
                                           List<String> asientosVendidos) {
        return !asientosReservados.contains(codigoAsiento) 
            && !asientosVendidos.contains(codigoAsiento);
    }

    // ==================== GETTERS SEGUROS ====================
    public List<String> getAsientosReservados() {
        return Collections.unmodifiableList(asientosReservados);
    }

    public List<String> getAsientosVendidos() {
        return Collections.unmodifiableList(asientosVendidos);
    }

    public List<String> getTodosAsientos() {
        List<String> todos = new ArrayList<>();
        for (String[] fila : planoTeatro) {
            Collections.addAll(todos, fila);
        }
        return todos;
    }
}