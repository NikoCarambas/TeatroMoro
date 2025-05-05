# Sistema de Gestión de Entradas de Teatro
Por Nicolás Cavieres

Este programa gestiona la compra, reserva, modificación y visualización de 
entradas para funciones teatrales. Los usuarios pueden acceder a promociones, 
visualizar disponibilidad, y realizar operaciones según el tipo de cliente.

---
Menu Principal
-> Saludo Inicial -> Mostrar precios y descuentos
   |
   |-> 1. Comprar entradas -> clase ComprarEntrada
   |     |-> Pedir nombre del cliente -> nombreCliente
   |     |-> Crear identificador del cliente -> clienteID
   |     |-> Pedir cantidad de entradas a comprar -> cantidadEntradas
   |     |-> Inicio loop while
   |         |-> Mostrar plano teatro actualizado
   |         |-> Seleccionar el asiento
   |         |-> Ingresar edad del espectador
   |         |-> Mostrar resumen por entrada.
   |     // Fin loop while
   |     |-> Mostral el resumen del total de entradas del loop while
   |     |-> ¿Desea reservar o comprar? 
   |         |-> 1. Reservar
   |            |-> Confirmar Reserva (S/N)
   |                |-> Actualizar variables en ModificarReserva -> listaReservas{reservaID, fechaReserva, nombreCliente, clienteID, asientosReservados[], totalAPagarReserva, totalDescuentoReserva}.
   |                |-> Iniciar TimerTask // pasados 10 minutos se borra la reserva
   |                |-> Mostrar resumen de la reserva 
   |         |-> 2. Comprar
   |            |-> Confirmar compra (S/N)
   |                |-> Actualizar variables en ModificarVenta -> listaVendidos{ventaID, fechaVenta, nombreCliente, clienteID, asientosVendidos[], totalAPagarVenta, totalDescuentoVenta}.
   |                |-> Imprimir boleta
   |
   |-> 2. Modificar una reserva -> clase ModificarReserva
   |     |-> Pedir reservaID
   |            |-> Submenú para modificar venta
   |
   |-> 3. Modificar una venta -> clase ModificarVenta
   |     |-> Pedir ventaID
   |            |-> Submenú para modificar venta
   |
   |-> 4. Ver asientos disponibles -> clase AsientosTeatro
   |     |-> Mostrar disponibilidad rápida desde PlanoTeatro
   |
   |-> 5. Ver Resumen de Tienda -> clase ResumenTienda
   |     |-> Submenú para ver Resumen de Ventas, Reservas e Interacciones.
   |
   |-> 6. Salir.


---

+-------------------------+
|   AsientosTeatro        |
+-------------------------+
| - teatro                |
| - ocupados              |
+-------------------------+
| + mostrarTeatro()       |
| + ocuparAsiento()       |
| + liberarAsiento()      |
+-------------------------+

+----------------------------------+
|   TipoCliente                    |
+----------------------------------+
| - tipoCliente                    |
| - descuento                      |
| - edadCliente                    |
| - tipo                           |
+----------------------------------+
| + determinarTipoCliente()        |
| + determinarDescuento()          |
+----------------------------------+

+-----------------------------+
|      VerPromociones         |
+-----------------------------+
| - precioEntradaVIP          |
| - precioEntradaPlatea       |
| - precioEntradaGeneral      |
| - descuentoEstudiante       |
| - descuentoTerceraEdad      |
+-----------------------------+
| + mostrar()                 |
+-----------------------------+

+------------------------------+
|   ValidarInputUsuario        |
+------------------------------+
| - cantidadDeErrores          |
+------------------------------+
| + validarNumero()            |
| + validarLetra()             |
| + mensajeIntentosSuperados() |
+------------------------------+

+---------------------------+
|  ModificarReserva         |
+---------------------------+
| - listaReservas           |
| - reservaID               |
+---------------------------+
| + agregarReserva()        |
| + buscarReserva()         |
| + confirmarCompra()       |
| + cancelarReserva()       |
| + imprimirReserva()       |
| + agregarAsiento()        |
| + quitarAsiento()         |
| + calcularTotalReserva()  |
| + mostrarResumenReserva() |
+---------------------------+

+---------------------------+
|  ModificarVenta           |
+---------------------------+
| - listaVentas             |
| - ventaID                 |
+---------------------------+
| + registrarVenta()        |
| + buscarVenta()           |
| + imprimirBoleta()        |
| + agregarAsiento()        |
| + quitarAsiento()         |
| + cancelarVenta()         |
| + calcularTotalVenta()    |
+---------------------------+

+-------------------------------+
|   ComprarEntrada              |
+-------------------------------+
| - PRECIO_VIP                  |
| - PRECIO_PLATEA               |
| - PRECIO_GENERAL              |
+-------------------------------+
| + procesarVenta()             |
| + seleccionarAsiento()        |
| + calcularPrecioEntrada()     |
| + mostrarResumenParcial()     |
| + aplicarDescuentoCantidad()  |
| + mostrarResumenFinal()       |
| + procesarTransaccion()       |
+-------------------------------+

+------------------------------------+
|         ResumenTienda              |
+------------------------------------+
| + cantidadDeInteracciones          |
| + totalReservasCompradas           |
| + totalVentasReservasCanceladas    |
| + totalDineroVentas                |
| + totalDineroVentasVIP             |
| + totalDineroVentasPlatea          |
| + totalDineroVentasGeneral         |
| + totalDineroReservas              |
| + totalDineroReservasVIP           |
| + totalDineroReservasPlatea        |
| + totalDineroReservasGeneral       |
+------------------------------------+
| + resumenInteracciones()           |
| + resumenVentas()                  |
| + mostrarReservas()                |
+------------------------------------+
