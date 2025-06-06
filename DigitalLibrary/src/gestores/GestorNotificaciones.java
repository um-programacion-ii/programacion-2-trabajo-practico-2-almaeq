package gestores;

import enums.CanalNotificacion;
import servicios.ServicioNotificaciones;
import servicios.ServicioNotificacionesEmail;
import servicios.ServicioNotificacionesSMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
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

    public void enviar(String destinatario, String mensaje, Set<CanalNotificacion> canalesPreferidos) {
        List<Callable<Void>> tareas = new ArrayList<>();
        for (ServicioNotificaciones servicio : servicios) {
            if (canalesPreferidos.contains(servicio.getCanal()) && servicio.estaActivo(destinatario)) {
                tareas.add(() -> {
                    servicio.enviarNotificacion(destinatario, mensaje);
                    return null;
                });
            }
        }
        try {
            executorService.invokeAll(tareas);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public List<ServicioNotificaciones> getServicios() {
        return servicios;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
