package modelos;

import enums.EstadoReserva;
import enums.PrioridadReserva;
import usuario.Usuario;

import java.time.LocalDate;

public class Reserva {
    int id;
    private Usuario usuario;
    private RecursoDigital recurso;
    private PrioridadReserva prioridad;
    private LocalDate fechaReserva;
    private EstadoReserva estado;

    public Reserva(int id, Usuario usuario, RecursoDigital recurso, PrioridadReserva prioridad, LocalDate fechaReserva, EstadoReserva estado) {
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

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
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
