package gestores;

import enums.EstadoRecurso;
import enums.EstadoReserva;
import enums.PrioridadReserva;
import excepciones.RecursoNoDisponibleExcepcion;
import excepciones.UsuarioNoEncontradoExcepcion;
import modelos.RecursoDigital;
import modelos.Reserva;
import gestores.GestorNotificaciones;
import usuario.Usuario;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class GestorReserva {
    private final BlockingQueue<Reserva> colaReservas = new PriorityBlockingQueue<>();
    private final List<Reserva> historialReservas = new ArrayList<>();
    private final GestorUsuario gestorUsuario;
    private final GestorRecursos gestorRecursos;
    private final GestorNotificaciones gestorNotificaciones = new GestorNotificaciones();
    private static final AtomicInteger generadorId = new AtomicInteger(1);

    public GestorReserva(GestorUsuario gestorUsuario, GestorRecursos gestorRecursos) {
        this.gestorUsuario = gestorUsuario;
        this.gestorRecursos = gestorRecursos;
    }

    public List<Reserva> getHistorialReservas() {
        return historialReservas;
    }

    public void registrarReserva(Scanner scanner) {
        try {
            Usuario usuario = solicitarUsuario(scanner);
            RecursoDigital recurso = solicitarRecurso(scanner);

            if (validarReserva(usuario, recurso)) {
                Reserva reserva = crearReserva(usuario, recurso);
                colaReservas.add(reserva);
                recalcularPrioridades();
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

    public Reserva crearReserva(Usuario usuario, RecursoDigital recurso) {
        return new Reserva(
                generadorId.getAndIncrement(),
                usuario,
                recurso,
                PrioridadReserva.MEDIA,
                new Date(),
                EstadoReserva.ACTIVA
        );
    }

    public boolean validarReserva(Usuario usuario, RecursoDigital recurso) {
        if (recurso.getEstado() == EstadoRecurso.DISPONIBLE) {
            System.out.println("⚠️ El recurso está disponible, no es necesario reservarlo.");
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

    public void completarReserva(int idReserva) {
        for (Reserva r : colaReservas) {
            if (r.getId() == idReserva && r.getEstado() == EstadoReserva.ACTIVA) {
                colaReservas.remove(r);
                r.setEstado(EstadoReserva.COMPLETADA);
                historialReservas.add(r);
                System.out.println("✅ Reserva #" + idReserva + " marcada como completada.");
                recalcularPrioridades();
                return;
            }
        }
        System.out.println("⚠️ Reserva no encontrada o ya no está activa.");
    }

    public void cancelarReserva(int idReserva) {
        for (Reserva r : colaReservas) {
            if (r.getId() == idReserva && r.getEstado() == EstadoReserva.ACTIVA) {
                colaReservas.remove(r);
                r.setEstado(EstadoReserva.CANCELADA);
                historialReservas.add(r);
                System.out.println("❌ Reserva #" + idReserva + " cancelada.");
                recalcularPrioridades();
                return;
            }
        }
        System.out.println("⚠️ Reserva no encontrada o ya no está activa.");
    }

    public void listarReservas() {
        if (colaReservas.isEmpty() && historialReservas.isEmpty()) {
            System.out.println("📭 No hay reservas registradas.");
            return;
        }

        System.out.println("📋 Reservas activas:");
        colaReservas.stream()
                .sorted()
                .forEach(System.out::println);

        System.out.println("\n📋 Reservas canceladas y completadas:");
        historialReservas.stream()
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
        historialReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.CANCELADA)
                .sorted()
                .forEach(System.out::println);
    }

    public void listarReservasCompletadas() {
        System.out.println("📋 Reservas Completadas:");
        historialReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.COMPLETADA)
                .sorted()
                .forEach(System.out::println);
    }

    public void mostrarProximaReserva() {
        Reserva proxima = colaReservas.peek();
        if (proxima != null) {
            System.out.println("🎯 Próxima reserva a atender:\n" + proxima);
        } else {
            System.out.println("📭 No hay reservas pendientes.");
        }
    }

    public void recalcularPrioridades() {
        List<Reserva> activas = colaReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.ACTIVA)
                .sorted(Comparator.comparing(Reserva::getFechaReserva))
                .toList();

        colaReservas.removeIf(r -> r.getEstado() == EstadoReserva.ACTIVA);

        int total = activas.size();
        for (int i = 0; i < total; i++) {
            Reserva r = activas.get(i);
            if (i < total / 3) {
                r.setPrioridad(PrioridadReserva.ALTA);
            } else if (i < (2 * total) / 3) {
                r.setPrioridad(PrioridadReserva.MEDIA);
            } else {
                r.setPrioridad(PrioridadReserva.BAJA);
            }
            colaReservas.add(r);
        }
        System.out.println("🔄 Prioridades recalculadas dinámicamente.");
    }

    public Reserva getProximaReservaParaRecurso(RecursoDigital recurso) {
        return colaReservas.stream()
                .filter(r -> r.getEstado() == EstadoReserva.ACTIVA && r.getRecurso().equals(recurso))
                .sorted()
                .findFirst()
                .orElse(null);
    }

    public void notificarPrimeraReservaDisponible(RecursoDigital recurso) {
        Reserva proxima = getProximaReservaParaRecurso(recurso);
        if (proxima != null) {
            recurso.configurarNotificaciones(
                    new GestorNotificaciones().getServicios(),
                    proxima.getUsuario().getEmail()
            );
            System.out.println("📩 Notificación: el recurso '" + recurso.getTitulo() + "' está disponible para " + proxima.getUsuario().getNombre());
        }
    }
}
