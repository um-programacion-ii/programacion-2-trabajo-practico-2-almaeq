package alertas;

import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import gestores.GestorReserva;
import modelos.Prestamo;
import modelos.RecursoDigital;
import modelos.Reserva;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.Scanner;

public class AlertaDisponibilidad {

    private final GestorReserva gestorReserva;

    public AlertaDisponibilidad(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }

    public void notificarDisponibilidad(RecursoDigital recurso, Scanner scanner, GestorPrestamo gestorPrestamo) {
        Reserva siguiente = gestorReserva.getProximaReservaParaRecurso(recurso);
        if (siguiente != null) {
            Usuario usuario = siguiente.getUsuario();
            GestorNotificaciones notificador = new GestorNotificaciones();
            notificador.activarPara(usuario.getEmail());
            recurso.configurarNotificaciones(notificador.getServicios(), usuario.getEmail());

            notificador.enviar(
                    usuario.getEmail(),
                    "📢 El recurso '" + recurso.getTitulo() + "' está disponible para tu reserva.",
                    usuario.getCanalesPreferidos()
            );

            // Preguntar en consola si desea tomarlo ahora
            System.out.printf("👤 %s, ¿deseás tomar el recurso '%s' ahora? (s/n): ",
                    usuario.getNombre(), recurso.getTitulo());
            String respuesta = scanner.nextLine().trim().toLowerCase();

            if (respuesta.equals("s")) {
                try {
                    System.out.print("📅 Ingresá la fecha de devolución (YYYY-MM-DD): ");
                    LocalDate fechaDevolucion = LocalDate.parse(scanner.nextLine());

                    Prestamo nuevoPrestamo = gestorPrestamo.crearPrestamo(usuario, recurso, fechaDevolucion);
                    gestorReserva.completarReserva(siguiente.getId());

                    System.out.println("✅ Préstamo registrado desde la alerta:\n" + nuevoPrestamo);
                } catch (Exception e) {
                    System.out.println("❌ No se pudo generar el préstamo desde la alerta: " + e.getMessage());
                }
            } else {
                System.out.println("ℹ️ Se mantuvo la reserva activa.");
            }
        } else {
            System.out.println("ℹ️ No hay reservas activas para el recurso: " + recurso.getTitulo());
        }
    }
}
