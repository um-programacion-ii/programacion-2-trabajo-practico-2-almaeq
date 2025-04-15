package servicios;

public interface ServicioNotificaciones {
    void enviarNotificacion(String destinatario, String mensaje);
    void desactivarNotificaciones(String usuario);
    boolean estaActivo(String usuario); //verificar si el usuario las tiene activadas
    void activarNotificaciones(String usuario);

}
