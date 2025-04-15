package servicios;

public class ServicioNotificacionesSMS implements ServicioNotificaciones{
    private boolean activado = false;

    @Override
    public void enviarNotificacion(String destinatario, String mensaje) {
        System.out.println("Enviando notificación por SMS a: " + destinatario);
        System.out.println(mensaje);
    }

    @Override
    public void desactivarNotificaciones(String usuario) {
        System.out.println("Desactivando notificaciones por SMS para: " + usuario);
        activado = false;
    }

    @Override
    public boolean estaActivo(String usuario) {
        return activado;
    }
}
