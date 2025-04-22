package alertas;

import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import usuario.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AlertaVencimiento {

    private final GestorPrestamo gestorPrestamo;
    private final GestorNotificaciones gestorNotificaciones;

    public AlertaVencimiento(GestorPrestamo gestorPrestamo, GestorNotificaciones gestorNotificaciones) {
        this.gestorPrestamo = gestorPrestamo;
        this.gestorNotificaciones = gestorNotificaciones;
    }

    public void verificarYNotificarVencimientos() {
        List<Prestamo> prestamosActivos = gestorPrestamo.listar().stream()
                .filter(Prestamo::estaActivo)
                .toList();

        LocalDate hoy = LocalDate.now();
        System.out.println("\n📢 === ALERTAS DE VENCIMIENTO ===");

        boolean huboAlertas = false;

        for (Prestamo prestamo : prestamosActivos) {
            LocalDate fechaDevolucion = prestamo.getFechaDevolucion();
            Usuario usuario = prestamo.getUsuario();
            System.out.printf(
                    "🔍 DEBUG | Préstamo #%d | Hoy: %s | Fecha devolución: %s | Días restantes: %d | Usuario: %s%n",
                    prestamo.getId(),
                    hoy,
                    fechaDevolucion,
                    ChronoUnit.DAYS.between(hoy, fechaDevolucion),
                    usuario.getEmail()
            );

            if (fechaDevolucion != null) {
                long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaDevolucion);

                String mensaje = null;
                if (fechaDevolucion.equals(hoy.plusDays(1))) {
                    // Vence mañana
                } else if (fechaDevolucion.equals(hoy)) {
                    // Vence hoy
                } else if (fechaDevolucion.isBefore(hoy)) {
                    // Ya venció
                }

                if (mensaje != null) {
                    gestorNotificaciones.enviar(usuario.getEmail(), mensaje);
                    huboAlertas = true;
                }
            }
        }

        if (!huboAlertas) {
            System.out.println("✅ No hay alertas de vencimiento activas.");
        }

        System.out.println("📢 === FIN DE ALERTAS ===\n");
    }
}