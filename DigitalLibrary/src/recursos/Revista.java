package recursos;

import interfaces.Renovable;
import interfaces.Prestable;


import java.time.LocalDate;

public class Revista extends RecursoDigital implements Renovable, Prestable {
    private int numero;private boolean prestado = false;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private int renovacionesDisponibles = 1;

    public Revista(String titulo, String identificador, EstadoRecurso estado, int numero) {
        super(titulo, identificador, estado);
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
            renovacionesDisponibles = 1; // resetear
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
    public boolean puedeRenovarse() {
        return prestado && renovacionesDisponibles > 0;
    }

    @Override
    public void renovar() {
        if (puedeRenovarse()) {
            fechaPrestamo = LocalDate.now();
            renovacionesDisponibles--;
            System.out.println("🔁 Libro renovado con éxito.");
        } else {
            System.out.println("⚠️ No se puede renovar el libro.");
        }
    }

    @Override
    public String mostrar() {
        String prestamoInfo = prestado
                ? " (Prestado desde: " + fechaPrestamo + (puedeRenovarse() ? ", renovable" : ", sin renovaciones") + ")"
                : (fechaDevolucion != null ? " (Devuelto el: " + fechaDevolucion + ")" : "");
        return "📰 Revista - " + titulo + " | Nº: " + numero + " | Estado: " + estado + " | Estado: " + estado + prestamoInfo;
    }

}