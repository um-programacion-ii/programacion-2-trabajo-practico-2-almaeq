package usuario;

import enums.CanalNotificacion;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Usuario {
    private static final AtomicInteger idGenerator = new AtomicInteger(1); // 👈 ID incremental automático

    private final int ID;
    private String nombre;
    private String apellido;
    private String email;
    private EnumSet<CanalNotificacion> canalesPreferidos = EnumSet.of(CanalNotificacion.EMAIL); // Por defecto email

    public Usuario(String nombre, String apellido, String email) {
        this.ID = idGenerator.getAndIncrement();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EnumSet<CanalNotificacion> getCanalesPreferidos() {
        return canalesPreferidos;
    }

    public void setCanalesPreferidos(EnumSet<CanalNotificacion> canalesPreferidos) {
        this.canalesPreferidos = canalesPreferidos;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "ID=" + ID +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
