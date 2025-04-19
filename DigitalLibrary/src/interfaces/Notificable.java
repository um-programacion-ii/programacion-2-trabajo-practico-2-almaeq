package interfaces;

import servicios.ServicioNotificaciones;

public interface Notificable {
    void agregarServicioNotificacion(ServicioNotificaciones servicio);
    void setDestinatarioNotificacion(String destinatario);
}
