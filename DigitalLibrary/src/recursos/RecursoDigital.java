package recursos;

import interfaces.IRecursoDigital;

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

    public void prestarSiEsPosible() {
        System.out.println("❌ El recurso no puede ser prestado.");
    }

    public void devolverSiEsPosible() {
        System.out.println("❌ El recurso no puede ser devuelto.");
    }

    public void renovarSiEsPosible() {
        System.out.println("❌ El recurso no es renovable.");
    }

    // Método abstracto para mostrar información personalizada
    public abstract String mostrar();
}
