package utils;

import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.List;

public class AlertaVencimiento {

    private final GestorPrestamo gestorPrestamo;
    private final GestorNotificaciones gestorNotificaciones = new GestorNotificaciones();

    public AlertaVencimiento(GestorPrestamo gestorPrestamo) {
        this.gestorPrestamo = gestorPrestamo;
    }

    public void verificarYNotificarVencimientos() {
        List<Prestamo> prestamosActivos = gestorPrestamo.listar().stream()
                .filter(Prestamo::estaActivo)
                .toList();

        LocalDate hoy = LocalDate.now();

        for (Prestamo prestamo : prestamosActivos) {
            LocalDate fechaDevolucion = prestamo.getFechaDevolucion();
            Usuario usuario = prestamo.getUsuario();

            if (fechaDevolucion != null) {
                long diasRestantes = hoy.until(fechaDevolucion).getDays();

                if (diasRestantes == 1) {
                    gestorNotificaciones.enviar(usuario.getEmail(), "⚠️ Tu préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence mañana.");
                } else if (diasRestantes == 0) {
                    gestorNotificaciones.enviar(usuario.getEmail(), "⏰ Tu préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence hoy.");
                } else if (diasRestantes < 0) {
                    gestorNotificaciones.enviar(usuario.getEmail(), "❗ Tu préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' está vencido desde hace " + Math.abs(diasRestantes) + " día(s).");
                }
            }
        }

        System.out.println("✅ Verificación de vencimientos completada.");
    }
}
