package alertas;

import enums.NivelUrgencia;
import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import usuario.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class AlertaVencimiento {

    private final GestorPrestamo gestorPrestamo;
    private final GestorNotificaciones gestorNotificaciones;
    private final Scanner scanner;

    public AlertaVencimiento(GestorPrestamo gestorPrestamo, GestorNotificaciones gestorNotificaciones, Scanner scanner) {
        this.gestorPrestamo = gestorPrestamo;
        this.gestorNotificaciones = gestorNotificaciones;
        this.scanner = scanner;
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

            if (fechaDevolucion != null) {
                long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaDevolucion);

                String mensaje = construirMensaje(prestamo, diasRestantes);
                NivelUrgencia nivel = obtenerNivel(diasRestantes);

                if (mensaje != null && nivel != null) {
                    String mensajeConSimbolo = nivel.getSimbolo() + " " + mensaje;
                    System.out.println(mensajeConSimbolo);
                    gestorNotificaciones.enviar(usuario.getEmail(), mensajeConSimbolo,usuario.getCanalesPreferidos());
                    HistorialAlertas.registrar(mensaje);
                    huboAlertas = true;

                    if ((diasRestantes == 0 || diasRestantes == 1) && prestamo.puedeRenovarse()) {
                        System.out.print("🔁 ¿Deseás renovar el préstamo #" + prestamo.getId() + "? (s/n): ");
                        String respuesta = scanner.nextLine().trim().toLowerCase();
                        if (respuesta.equals("s")) {
                            boolean renovado = gestorPrestamo.renovarPrestamo(prestamo.getId());
                            if (renovado) {
                                System.out.println("✅ Préstamo renovado correctamente. Nueva fecha de devolución: "
                                        + prestamo.getFechaDevolucion() + "\n");
                            } else {
                                System.out.println("❌ No se pudo renovar el préstamo #" + prestamo.getId() + ".\n");
                            }
                        }
                    }
                }
            }
        }

        if (!huboAlertas) {
            System.out.println("✅ No hay alertas de vencimiento activas.");
        }

        System.out.println("📢 === FIN DE ALERTAS ===\n");
    }

    private NivelUrgencia obtenerNivel(long diasRestantes) {
        if (diasRestantes == 1) return NivelUrgencia.INFO;
        if (diasRestantes == 0) return NivelUrgencia.WARNING;
        if (diasRestantes < 0) return NivelUrgencia.ERROR;
        return null;
    }

    private String construirMensaje(Prestamo prestamo, long diasRestantes) {
        if (diasRestantes == 1) {
            return "[Mañana] El préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence mañana.";
        } else if (diasRestantes == 0) {
            return "[HOY] El préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence hoy.";
        } else if (diasRestantes < 0) {
            return "[VENCIDO] El préstamo del recurso '" + prestamo.getRecurso().getTitulo() +
                    "' venció hace " + Math.abs(diasRestantes) + " día(s).";
        }
        return null;
    }
}
