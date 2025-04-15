package recursos;

import interfaces.Renovable;
import interfaces.Prestable;
import servicios.ServicioNotificaciones;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Revista extends RecursoDigital implements Renovable, Prestable {
    private int numero;private boolean prestado = false;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private int renovacionesDisponibles = 1;

    private final List<ServicioNotificaciones> serviciosNotificaciones = new ArrayList<>();
    private String destinatarioNotificacion;

    public Revista(String titulo, String identificador, EstadoRecurso estado, int numero) {
        super(titulo, identificador, estado);
        this.numero = numero;
    }

    // === GETTERS Y SETTERS ===
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
            renovacionesDisponibles = 1; // resetear
            notificar("📘 Se prestó la revista: " + getTitulo());
        }
    }

    @Override
    public void devolver() {
        if (prestado) {
            prestado = false;
            estado = EstadoRecurso.DISPONIBLE;
            fechaDevolucion = LocalDate.now();
            notificar("📘 Se devolvió la revista: " + getTitulo());
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
            notificar("🔁 Se renovó la revista: " + getTitulo());
            System.out.println("🔁 Libro renovado con éxito.");
        } else {
            System.out.println("⚠️ No se puede renovar el libro.");
        }
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
    public void renovarSiEsPosible() {
        if (puedeRenovarse()) {
            renovar();
        } else {
            System.out.println("⚠️ El recurso no puede renovarse en este momento.");
        }
    }

    @Override
    public String mostrar() {
        String prestamoInfo = prestado
                ? " (Prestado desde: " + fechaPrestamo + (puedeRenovarse() ? ", renovable" : ", sin renovaciones") + ")"
                : (fechaDevolucion != null ? " (Devuelto el: " + fechaDevolucion + ")" : "");
        return "📰 Revista - " + titulo + " | Nº: " + numero + " | Estado: " + estado + " | Estado: " + estado + prestamoInfo;
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