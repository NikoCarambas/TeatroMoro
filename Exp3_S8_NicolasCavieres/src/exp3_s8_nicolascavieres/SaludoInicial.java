/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exp3_s8_nicolascavieres;
/**
 *
 * @author nikoc
 */
public class SaludoInicial {
    // Precio entradas
    public static final int PRECIO_VIP = 20000;
    public static final int PRECIO_PLATEA = 15000;
    public static final int PRECIO_GENERAL = 10000;
    // Descuentos
    public static final double DESCUENTO_ESTUDIANTE = 0.20;
    public static final double DESCUENTO_TERCERA_EDAD = 0.30;
    
    public static void mostrarSaludo() {
        System.out.println("""
            \n============================================
                     Bienvenido al Teatro Moro ^o^
                Parte del plan de Cultura de Santiago.
            ==============================================
            
              Precios de entradas:
                - VIP:     $%d
                - Platea:  $%d
                - General: $%d
                        
              Descuentos disponibles:
                - Estudiantes:  %d%%
                - Tercera edad: %d%%
            
             Selecciona una opcion del menu para comenzar
            ==============================================
            """.formatted(
                PRECIO_VIP, PRECIO_PLATEA, PRECIO_GENERAL,
                (int)(DESCUENTO_ESTUDIANTE * 100), 
                (int)(DESCUENTO_TERCERA_EDAD * 100)
            ));
    }
}
