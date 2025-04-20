package modelos;

import enums.EstadoPrestamo;
import usuario.Usuario;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private RecursoDigital recurso;
    private Usuario usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;
    private int renovaciones = 0;

    private static final int MAX_RENOVACIONES = 1;

    public Prestamo(int id, RecursoDigital recurso, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion, EstadoPrestamo estado, int renovaciones) {
        this.id = id;
        this.recurso = recurso;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.renovaciones = renovaciones;
    }

    public int getId() {
        return id;
    }

    public RecursoDigital getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        if (fechaPrestamo == null || fechaPrestamo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de préstamo no puede ser nula ni futura.");
        }
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        if (fechaDevolucion != null && fechaDevolucion.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la de préstamo.");
        }
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public int getRenovaciones() {
        return renovaciones;
    }

    public boolean estaVencido() {
        return fechaDevolucion != null && fechaDevolucion.isBefore(LocalDate.now());
    }

    public boolean estaActivo() {
        return estado == EstadoPrestamo.PRESTADO || estado == EstadoPrestamo.RENOVADO;
    }

    public boolean estaDisponible() {
        return estado == EstadoPrestamo.DISPONIBLE;
    }

    public boolean puedeRenovarse() {
        return estaActivo() && renovaciones < MAX_RENOVACIONES;
    }

    public void devolver() {
        this.estado = EstadoPrestamo.DISPONIBLE;
        this.fechaDevolucion = LocalDate.now();
    }

    public void renovar() {
        this.fechaPrestamo = LocalDate.now();
        this.estado = EstadoPrestamo.RENOVADO;
        this.renovaciones++;
    }

    @Override
    public String toString() {
        return "📚 Préstamo #" + id +
                " | Recurso: " + recurso.getTitulo() +
                " | Usuario: " + usuario.getNombre() + " " + usuario.getApellido() +
                " | Fecha préstamo: " + fechaPrestamo +
                (fechaDevolucion != null ? " | Devolución: " + fechaDevolucion : "") +
                " | Estado: " + estado +
                " | Renovaciones: " + renovaciones;
    }
}
