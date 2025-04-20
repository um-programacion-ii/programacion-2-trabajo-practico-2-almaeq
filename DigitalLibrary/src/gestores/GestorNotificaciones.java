package gestores;

import servicios.ServicioNotificaciones;
import servicios.ServicioNotificacionesEmail;
import servicios.ServicioNotificacionesSMS;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestorNotificaciones {
    private final List<ServicioNotificaciones> servicios;
    private final ExecutorService executorService;

    public GestorNotificaciones() {
        this.servicios = List.of(
                new ServicioNotificacionesEmail(),
                new ServicioNotificacionesSMS()
        );
        this.executorService = Executors.newFixedThreadPool(4); // 🔁 Ajustable según tu carga
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
            executorService.submit(() -> {
                if (servicio.estaActivo(destinatario)) {
                    servicio.enviarNotificacion(destinatario, mensaje);
                }
            });
        }
    }

    public List<ServicioNotificaciones> getServicios() {
        return servicios;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
