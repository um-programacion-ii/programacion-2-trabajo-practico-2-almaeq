package gestores;

import enums.EstadoRecurso;
import enums.EstadoReserva;
import enums.PrioridadReserva;
import excepciones.RecursoNoDisponibleExcepcion;
import excepciones.UsuarioNoEncontradoExcepcion;
import modelos.RecursoDigital;
import modelos.Reserva;
import usuario.Usuario;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class GestorReserva {
    private final BlockingQueue<Reserva> colaReservas = new PriorityBlockingQueue<>();
    private final GestorUsuario gestorUsuario;
    private final GestorRecursos gestorRecursos;
    private static final AtomicInteger generadorId = new AtomicInteger(1);

    public GestorReserva(GestorUsuario gestorUsuario, GestorRecursos gestorRecursos) {
        this.gestorUsuario = gestorUsuario;
        this.gestorRecursos = gestorRecursos;
    }

    public void registrarReserva(PrioridadReserva prioridad, Scanner scanner) {
        try {
            Usuario usuario = solicitarUsuario(scanner);
            RecursoDigital recurso = solicitarRecurso(scanner);

            if (validarReserva(usuario, recurso)) {
                Reserva reserva = crearReserva(usuario, recurso, prioridad);
                colaReservas.add(reserva);
                System.out.println("✅ Reserva registrada con éxito:\n" + reserva);
            } else {
                System.out.println("❌ La reserva no pudo ser registrada.");
            }

        } catch (RecursoNoDisponibleExcepcion | UsuarioNoEncontradoExcepcion e) {
            System.out.println("❌ Error al registrar la reserva: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ ID de usuario inválido.");
        }
    }

    public void mostrarProximaReserva() {
        Reserva proxima = colaReservas.peek();
        if (proxima != null) {
            System.out.println("🎯 Próxima reserva a atender:\n" + proxima);
        } else {
            System.out.println("📭 No hay reservas pendientes.");
        }
    }

    private Usuario solicitarUsuario(Scanner scanner) throws UsuarioNoEncontradoExcepcion {
        System.out.print("Ingrese el ID del usuario: ");
        int idUsuario = Integer.parseInt(scanner.nextLine());
        return gestorUsuario.buscarPorId(idUsuario);
    }

    private RecursoDigital solicitarRecurso(Scanner scanner) throws RecursoNoDisponibleExcepcion {
        System.out.print("Ingrese el título del recurso: ");
        String titulo = scanner.nextLine();
        return gestorRecursos.buscarPorTitulo(titulo);
    }

    public Reserva crearReserva(Usuario usuario, RecursoDigital recurso, PrioridadReserva prioridad) {
        return new Reserva(
                generadorId.getAndIncrement(),
                usuario,
                recurso,
                prioridad,
                new Date(),
                EstadoReserva.ACTIVA
        );
    }

    public boolean validarReserva(Usuario usuario, RecursoDigital recurso) {
        if (!recurso.esPrestable()) {
            System.out.println("⚠️ El recurso no está disponible para ser reservado.");
            return false;
        }
        for (Reserva r : colaReservas) {
            if (r.getUsuario().equals(usuario) &&
                    r.getRecurso().equals(recurso) &&
                    r.getEstado() == EstadoReserva.ACTIVA) {
                System.out.println("⚠️ Ya existe una reserva activa para este recurso y usuario.");
                return false;
            }
        }
        return true;
    }

    public boolean cancelarReserva(int idReserva) {
        for (Reserva r : colaReservas) {
            if (r.getId() == idReserva && r.getEstado() == EstadoReserva.ACTIVA) {
                colaReservas.remove(r);
                r.setEstado(EstadoReserva.CANCELADA);
                colaReservas.add(r); // opcional
                System.out.println("❌ Reserva #" + idReserva + " cancelada.");
                return true;
            }
        }
        System.out.println("⚠️ Reserva no encontrada o ya no está activa.");
        return false;
    }

    public boolean completarReserva(int idReserva) {
        for (Reserva r : colaReservas) {
            if (r.getId() == idReserva && r.getEstado() == EstadoReserva.ACTIVA) {
                colaReservas.remove(r);
                r.setEstado(EstadoReserva.COMPLETADA);
                System.out.println("✅ Reserva #" + idReserva + " marcada como completada.");
                return true;
            }
        }
        System.out.println("⚠️ Reserva no encontrada o ya no está activa.");
        return false;
    }

    public void listarReservas() {
        if (colaReservas.isEmpty()) {
            System.out.println("📭 No hay reservas registradas.");
            return;
        }
        System.out.println("📋 Listado de Reservas:");
        colaReservas.stream()
                .sorted()
                .forEach(System.out::println);
    }

    public void listarReservasActivas() {
        System.out.println("📋 Reservas Activas:");
        colaReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.ACTIVA)
                .sorted()
                .forEach(System.out::println);
    }

    public void listarReservasCanceladas() {
        System.out.println("📋 Reservas Canceladas:");
        colaReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA)
                .sorted()
                .forEach(System.out::println);
    }

    public void listarReservasCompletadas() {
        System.out.println("📋 Reservas Completadas:");
        colaReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.COMPLETADA)
                .sorted()
                .forEach(System.out::println);
    }
}
