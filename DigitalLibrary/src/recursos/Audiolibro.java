package recursos;


import interfaces.Prestable;
import servicios.ServicioNotificaciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Audiolibro extends RecursoDigital implements Prestable {
    private String narrador;
    private double duracion;
    private boolean prestado = false;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    private final List<ServicioNotificaciones> serviciosNotificaciones = new ArrayList<>();
    private String destinatarioNotificacion;

    public Audiolibro(String titulo, String identificador, EstadoRecurso estado, String narrador, double duracion) {
        super(titulo, identificador, estado);
        this.narrador = narrador;
        this.duracion = duracion;
    }

    // === GETTERS Y SETTERS ===
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

    // === MÉTODOS DE INTERFACES ===
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
            notificar("📘 Se prestó el audiolibro: " + getTitulo());
        }
    }

    @Override
    public void devolver() {
        if (prestado) {
            prestado = false;
            estado = EstadoRecurso.DISPONIBLE;
            fechaDevolucion = LocalDate.now();
            notificar("📘 Se devolvió el audiolibro: " + getTitulo());
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
    public void prestarSiEsPosible() {
        if (!estaPrestado()) {
            prestar();
            System.out.println("✅ Recurso prestado con éxito.");
        } else {
            System.out.println("⚠️ El recurso ya está prestado.");
        }
    }

    @Override
    public void devolverSiEsPosible() {
        if (estaPrestado()) {
            devolver();
            System.out.println("✅ Recurso devuelto con éxito.");
        } else {
            System.out.println("⚠️ El recurso no estaba prestado.");
        }
    }

    @Override
    public String mostrar() {
        String prestamoInfo = prestado
                ? " (Prestado desde: " + fechaPrestamo + ")"
                : (fechaDevolucion != null ? " (Devuelto el: " + fechaDevolucion + ")" : "");
        return "🎧 Audiolibro - " + titulo + " | Narrador: " + narrador + " | Duración: " + duracion + " hs | Estado: " + estado+ prestamoInfo;
    }

    // === MÉTODOS PARA NOTIFICACIONES ===
    public void agregarServicioNotificacion(ServicioNotificaciones servicio) {
        this.serviciosNotificaciones.add(servicio);
    }

    public void setDestinatarioNotificacion(String destinatario) {
        this.destinatarioNotificacion = destinatario;
    }

    private void notificar(String mensaje) {
        for (ServicioNotificaciones servicio : serviciosNotificaciones) {
            if (destinatarioNotificacion != null && servicio.estaActivo(destinatarioNotificacion)) {
                servicio.enviarNotificacion(destinatarioNotificacion, mensaje);
            }
        }
    }
}
