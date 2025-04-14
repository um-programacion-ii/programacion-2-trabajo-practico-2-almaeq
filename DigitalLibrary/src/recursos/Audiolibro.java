package recursos;

import recursos.RecursoDigital;

public class Audiolibro extends RecursoDigital {
    private String narrador;
    private double duracion;

    public Audiolibro(String titulo, String identificador, EstadoRecurso estado, String narrador, double duracion) {
        super(titulo, identificador, estado);
        this.narrador = narrador;
        this.duracion = duracion;
    }

    public String getNarrador() {
        return narrador;
    }

    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    @Override
    public String mostrar() {
        return "🎧 Audiolibro - " + titulo + " | Narrador: " + narrador + " | Duración: " + duracion + " hs | Estado: " + estado;
    }
}
