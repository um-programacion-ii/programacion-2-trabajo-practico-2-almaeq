package alertas;

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

                String mensaje = getString(prestamo, diasRestantes);

                if (mensaje != null) {
                    System.out.println(mensaje);
                    gestorNotificaciones.enviar(usuario.getEmail(), mensaje);
                    huboAlertas = true;

                    // Permitir renovación solo si puede renovarse y está por vencer
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

    private static String getString(Prestamo prestamo, long diasRestantes) {
        String mensaje = null;
        if (diasRestantes == 1) {
            mensaje = "⏰ [Mañana] El préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence mañana.";
        } else if (diasRestantes == 0) {
            mensaje = "⚠️ [HOY] El préstamo del recurso '" + prestamo.getRecurso().getTitulo() + "' vence hoy.";
        } else if (diasRestantes < 0) {
            mensaje = "❗ [VENCIDO] El préstamo del recurso '" + prestamo.getRecurso().getTitulo() +
                    "' venció hace " + Math.abs(diasRestantes) + " día(s).";
        }
        return mensaje;
    }
}
