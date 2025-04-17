package recursos;

import interfaces.IRecursoDigital;
import servicios.ServicioNotificaciones;

import java.util.List;

public abstract class RecursoDigital implements IRecursoDigital {
    protected String titulo;
    protected String identificador;
    protected EstadoRecurso estado;

    public RecursoDigital(String titulo, String identificador, EstadoRecurso estado) {
        this.titulo = titulo;
        this.identificador = identificador;
        this.estado = estado;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public String getIdentificador() {
        return identificador;
    }

    @Override
    public EstadoRecurso getEstado() {
        return estado;
    }

    @Override
    public void actualizarEstado(EstadoRecurso estado) {
        this.estado = estado;
    }

    // ✅ Estos métodos deben ser abstractos para que se llamen desde las subclases
    public abstract void prestarSiEsPosible();

    public abstract void devolverSiEsPosible();

    public abstract void renovarSiEsPosible();

    public void configurarNotificaciones(ServicioNotificaciones servicio, String destinatario) {
    }

    public void configurarNotificaciones(List<ServicioNotificaciones> servicios, String destinatario) {
    }

    public boolean esRenovable() {
        return false; // Por defecto
    }

    public abstract String mostrar();
}
