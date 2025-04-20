package gestores;

import servicios.ServicioNotificaciones;
import servicios.ServicioNotificacionesEmail;
import servicios.ServicioNotificacionesSMS;

import java.util.List;

public class GestorNotificaciones {

    private final List<ServicioNotificaciones> servicios;

    public GestorNotificaciones() {
        // Se pueden agregar más servicios en el futuro
        this.servicios = List.of(
                new ServicioNotificacionesEmail(),
                new ServicioNotificacionesSMS()
        );
    }

    public void activarPara(String destinatario) {
        for (ServicioNotificaciones servicio : servicios) {
            servicio.activarNotificaciones(destinatario);
        }
    }

    public void desactivarPara(String destinatario) {
        for (ServicioNotificaciones servicio : servicios) {
            servicio.desactivarNotificaciones(destinatario);
        }
    }

    public void enviar(String destinatario, String mensaje) {
        for (ServicioNotificaciones servicio : servicios) {
            if (servicio.estaActivo(destinatario)) {
                servicio.enviarNotificacion(destinatario, mensaje);
            }
        }
    }

    public List<ServicioNotificaciones> getServiciosActivos() {
        return servicios;
    }
}
