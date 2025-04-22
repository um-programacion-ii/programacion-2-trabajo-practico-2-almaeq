package modelos;

import enums.CategoriaRecurso;
import enums.EstadoRecurso;
import interfaces.IRecursoDigital;
import servicios.ServicioNotificaciones;

import java.util.List;

public abstract class RecursoDigital implements IRecursoDigital {
    private static int contador = 1;

    protected String titulo;
    protected String identificador;
    protected EstadoRecurso estado;

    public RecursoDigital(String titulo, EstadoRecurso estado) {
        this.titulo = titulo;
        this.identificador = generarId();
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

    private static synchronized String generarId() {
        return "" + contador++;
    }

    @Override
    public enums.EstadoRecurso getEstado() {
        return estado;
    }

    @Override
    public void actualizarEstado(enums.EstadoRecurso estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecursoDigital that)) return false;
        return identificador.equals(that.identificador);
    }

    @Override
    public int hashCode() {
        return identificador.hashCode();
    }

    // ✅ Estos métodos deben ser abstractos para que se llamen desde las subclases
    public abstract void prestarSiEsPosible();

    public abstract void devolverSiEsPosible();

    public abstract void renovarSiEsPosible();

    public abstract CategoriaRecurso getCategoria();

    public void configurarNotificaciones(ServicioNotificaciones servicio, String destinatario) {
    }

    public void configurarNotificaciones(List<ServicioNotificaciones> servicios, String destinatario) {
    }

    public boolean esRenovable() {
        return false; // Por defecto
    }

    public boolean esPrestable() {
        return estado == EstadoRecurso.DISPONIBLE;
    }

    public abstract String mostrar();

}
