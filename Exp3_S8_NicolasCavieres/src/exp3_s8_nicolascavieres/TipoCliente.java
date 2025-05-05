/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;

/**
 *
 * @author nikoc
 */
public class TipoCliente {
    private final String tipoCliente;
    private final double descuento;
    private final int edadCliente;

    public TipoCliente (int edadCliente) {
        this.edadCliente = edadCliente;
        this.tipoCliente = determinarTipoCliente();
        this.descuento = determinarDescuento();
    }        
            
    public String determinarTipoCliente() {
        if (edadCliente < 18) return "Estudiante";
        else if (edadCliente >= 60) return "Tercera Edad";
        else return "General";
    }

    public double determinarDescuento() {
        return switch (tipoCliente) {
            case "Estudiante" -> 0.20;
            case "Tercera Edad" -> 0.30;
            default -> 0.0;
        };
    }

    public String getTipoCliente() { return tipoCliente; }
    public double getDescuento() { return descuento; }
    public double getPorcentajeDescuento() {return descuento * 100;};
    public int getEdadCliente() { return edadCliente;}
}
