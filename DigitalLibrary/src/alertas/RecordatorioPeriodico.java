package alertas;

import enums.NivelUrgencia;
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

                NivelUrgencia urgencia = determinarUrgencia(diasRestantes);
                String mensaje = construirMensaje(prestamo, diasRestantes, urgencia);

                if (mensaje != null && urgencia != null) {
                    String mensajeConSimbolo = urgencia.getSimbolo() + " " + mensaje;
                    System.out.println(mensajeConSimbolo);
                    gestorNotificaciones.enviar(usuario.getEmail(), mensajeConSimbolo,usuario.getCanalesPreferidos());

                    HistorialAlertas.registrar(mensaje);
                }
            }
        }
    }

    private NivelUrgencia determinarUrgencia(long diasRestantes) {
        if (diasRestantes == 1) return NivelUrgencia.WARNING;
        if (diasRestantes == 0) return NivelUrgencia.WARNING;
        if (diasRestantes < 0) return NivelUrgencia.ERROR;
        return NivelUrgencia.INFO;
    }

    private String construirMensaje(Prestamo prestamo, long diasRestantes, NivelUrgencia urgencia) {
        String titulo = prestamo.getRecurso().getTitulo();

        return switch (urgencia) {
            case WARNING -> (diasRestantes == 1)
                    ? "Recordatorio: tu préstamo del recurso '" + titulo + "' vence mañana."
                    : "Hoy vence tu préstamo del recurso '" + titulo + "'.";
            case ERROR ->
                    "Tu préstamo del recurso '" + titulo + "' venció hace " + Math.abs(diasRestantes) + " día(s).";
            case INFO -> "Tu préstamo del recurso '" + titulo + "' sigue activo.";
        };
    }

}
