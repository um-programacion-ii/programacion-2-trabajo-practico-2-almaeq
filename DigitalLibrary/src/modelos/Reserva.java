package modelos;

import enums.EstadoReserva;
import enums.PrioridadReserva;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.Date;

public class Reserva implements Comparable<Reserva>{
    int id;
    private Usuario usuario;
    private RecursoDigital recurso;
    private PrioridadReserva prioridad;
    private Date fechaReserva;
    private EstadoReserva estado;

    public Reserva(int id, Usuario usuario, RecursoDigital recurso, PrioridadReserva prioridad, Date fechaReserva, EstadoReserva estado) {
        this.id = id;
        this.usuario = usuario;
        this.recurso = recurso;
        this.prioridad = prioridad;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public RecursoDigital getRecurso() {
        return recurso;
    }

    public PrioridadReserva getPrioridad() {
        return prioridad;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    @Override
    public int compareTo(Reserva otra) {
        int cmp = this.prioridad.compareTo(otra.prioridad);
        if (cmp == 0) {
            // Si tienen la misma prioridad, comparar por fecha (la más vieja primero)
            return this.fechaReserva.compareTo(otra.fechaReserva);
        }
        return cmp;
    }

    @Override
    public String toString() {
        return "📌 Reserva #" + id +
                " | Usuario: " + usuario.getNombre() +
                " | Recurso: " + recurso.getTitulo() +
                " | Prioridad: " + prioridad +
                " | Fecha: " + fechaReserva +
                " | Estado: " + estado;
    }

}
