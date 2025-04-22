package alertas;

import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.List;

public class RecordatorioPeriodico implements Runnable {

    private final GestorPrestamo gestorPrestamo;
    private final GestorNotificaciones gestorNotificaciones;

    public RecordatorioPeriodico(GestorPrestamo gestorPrestamo, GestorNotificaciones gestorNotificaciones) {
        this.gestorPrestamo = gestorPrestamo;
        this.gestorNotificaciones = gestorNotificaciones;
    }

    @Override
    public void run() {
        List<Prestamo> prestamosActivos = gestorPrestamo.listar().stream()
                .filter(Prestamo::estaActivo)
                .toList();

        LocalDate hoy = LocalDate.now();

        for (Prestamo prestamo : prestamosActivos) {
            LocalDate fechaDevolucion = prestamo.getFechaDevolucion();
            Usuario usuario = prestamo.getUsuario();

            if (fechaDevolucion != null) {
                long diasRestantes = hoy.until(fechaDevolucion).getDays();
                String mensaje = null;

                if (diasRestantes == 1) {
                    mensaje = "⏰ Recordatorio: tu préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence mañana.";
                } else if (diasRestantes == 0) {
                    mensaje = "⚠️ Hoy vence tu préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "'.";
                } else if (diasRestantes < 0) {
                    mensaje = "❗ Tu préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' venció hace " + Math.abs(diasRestantes) + " días.";
                }

                if (mensaje != null) {
                    gestorNotificaciones.enviar(usuario.getEmail(), mensaje);
                }
            }
        }
    }
}
