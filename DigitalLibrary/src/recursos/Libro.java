package recursos;

import interfaces.Prestable;
import interfaces.Renovable;

import java.time.LocalDate;

public class Libro extends RecursoDigital implements Prestable, Renovable {
    private int cant_paginas;
    private String autor;
    private boolean prestado = false;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private int renovacionesDisponibles = 1;

    public Libro(String titulo, String identificador, EstadoRecurso estado, int cant_paginas, String autor) {
        super(titulo, identificador, estado);
        this.cant_paginas = cant_paginas;
        this.autor = autor;
    }

    public int getCant_paginas() {
        return cant_paginas;
    }

    public void setCant_paginas(int cant_paginas) {
        this.cant_paginas = cant_paginas;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
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
        return "📘 Libro - " + titulo + " | Autor: " + autor + " | Páginas: " + cant_paginas + " | Estado: " + estado + prestamoInfo;
    }
}
