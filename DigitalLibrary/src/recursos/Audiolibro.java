package recursos;


import interfaces.Prestable;

import java.time.LocalDate;

public class Audiolibro extends RecursoDigital implements Prestable {
    private String narrador;
    private double duracion;
    private boolean prestado = false;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

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
    public boolean estaPrestado() {
        return prestado;
    }

    @Override
    public void prestar() {
        if (!prestado) {
            prestado = true;
            estado = EstadoRecurso.PRESTADO;
            fechaPrestamo = LocalDate.now();
            fechaDevolucion = null;
        }
    }

    @Override
    public void devolver() {
        if (prestado) {
            prestado = false;
            estado = EstadoRecurso.DISPONIBLE;
            fechaDevolucion = LocalDate.now();
        }
    }

    @Override
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    @Override
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    @Override
    public String mostrar() {
        String prestamoInfo = prestado
                ? " (Prestado desde: " + fechaPrestamo + ")"
                : (fechaDevolucion != null ? " (Devuelto el: " + fechaDevolucion + ")" : "");
        return "🎧 Audiolibro - " + titulo + " | Narrador: " + narrador + " | Duración: " + duracion + " hs | Estado: " + estado+ prestamoInfo;
    }
}
