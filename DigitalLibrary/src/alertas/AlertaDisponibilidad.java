package alertas;

import gestores.GestorReserva;
import gestores.GestorNotificaciones;
import modelos.RecursoDigital;
import modelos.Reserva;
import usuario.Usuario;

public class AlertaDisponibilidad {

    private final GestorReserva gestorReserva;

    public AlertaDisponibilidad(GestorReserva gestorReserva) {
        this.gestorReserva = gestorReserva;
    }

    public void notificarDisponibilidad(RecursoDigital recurso) {
        Reserva siguiente = gestorReserva.getProximaReservaParaRecurso(recurso);
        if (siguiente != null) {
            Usuario usuario = siguiente.getUsuario();
            GestorNotificaciones notificador = new GestorNotificaciones();
            notificador.activarPara(usuario.getEmail());
            recurso.configurarNotificaciones(notificador.getServicios(), usuario.getEmail());

            notificador.enviar(usuario.getEmail(),
                    "📢 El recurso '" + recurso.getTitulo() + "' está disponible para tu reserva.");
        } else {
            System.out.println("ℹ️ No hay reservas activas para el recurso: " + recurso.getTitulo());
        }
    }
}
