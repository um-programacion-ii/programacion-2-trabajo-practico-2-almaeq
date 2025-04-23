package servicios;

import enums.CanalNotificacion;

import java.util.HashSet;
import java.util.Set;

public class ServicioNotificacionesSMS implements ServicioNotificaciones {

    private final Set<String> usuariosActivos = new HashSet<>();

    @Override
    public void enviarNotificacion(String destinatario, String mensaje) {
        System.out.println("📱 SMS a " + destinatario + ": " + mensaje);
    }

    @Override
    public void desactivarNotificaciones(String usuario) {
        usuariosActivos.remove(usuario);
    }

    @Override
    public boolean estaActivo(String usuario) {
        return usuariosActivos.contains(usuario);
    }

    public void activarNotificaciones(String usuario) {
        usuariosActivos.add(usuario);
    }

    @Override
    public CanalNotificacion getCanal() {
        return CanalNotificacion.SMS;
    }
}
