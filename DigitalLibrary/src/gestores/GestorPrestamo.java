package gestores;

import enums.EstadoRecurso;
import enums.EstadosPrestamo;
import excepciones.RecursoNoDisponibleExcepcion;
import excepciones.UsuarioNoEncontradoExcepcion;
import modelos.Prestamo;
import modelos.RecursoDigital;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class GestorPrestamo {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private static final AtomicInteger generadorId = new AtomicInteger(1);
    private final GestorRecursos gestorRecursos;
    private final GestorUsuario gestorUsuario;
    private final Scanner scanner;

    public GestorPrestamo(GestorRecursos gestorRecursos, GestorUsuario gestorUsuario, Scanner scanner) {
        this.gestorRecursos = gestorRecursos;
        this.gestorUsuario = gestorUsuario;
        this.scanner = scanner;
    }

    public Prestamo crearPrestamo(Usuario usuario, RecursoDigital recurso) throws RecursoNoDisponibleExcepcion {
        if (!puedePrestarse(recurso)) {
            throw new RecursoNoDisponibleExcepcion("El recurso no puede ser prestado.");
        }

        int id = generadorId.getAndIncrement();
        Prestamo nuevo = new Prestamo(id, recurso, usuario, LocalDate.now(), null, EstadosPrestamo.PRESTADO, 0);
        prestamos.add(nuevo);
        recurso.actualizarEstado(EstadoRecurso.PRESTADO);
        return nuevo;
    }
    
    public Prestamo registrarPrestamoInteractivo() {
        try {
            Usuario usuario = seleccionarUsuario();
            RecursoDigital recurso = seleccionarRecursoDisponible();
            Prestamo nuevo = crearPrestamo(usuario, recurso);

            System.out.println("✅ Préstamo registrado con éxito:\n" + nuevo);
            return nuevo;

        } catch (UsuarioNoEncontradoExcepcion | RecursoNoDisponibleExcepcion e) {
            System.out.println("❌ Error: " + e.getMessage());
            return null;
        }
    }

    private Usuario seleccionarUsuario() throws UsuarioNoEncontradoExcepcion {
        if (gestorUsuario.estaVacio()) {
            throw new UsuarioNoEncontradoExcepcion("No hay usuarios registrados.");
        }
        System.out.println("Seleccione un usuario por ID:");
        gestorUsuario.listar().forEach(u ->
                System.out.println("- ID: " + u.getID() + " | " + u.getNombre() + " " + u.getApellido()));
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Usuario usuario = gestorUsuario.buscarPorId(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoExcepcion("Usuario con ID " + id + " no encontrado.");
        }
        return usuario;
    }

    private RecursoDigital seleccionarRecursoDisponible() throws RecursoNoDisponibleExcepcion {
        List<RecursoDigital> disponibles = gestorRecursos.listar().stream()
                .filter(RecursoDigital::esPrestable)
                .toList();
        if (disponibles.isEmpty()) {
            throw new RecursoNoDisponibleExcepcion("No hay recursos disponibles para préstamo.");
        }
        System.out.println("Seleccione un recurso por ID:");
        disponibles.forEach(r -> System.out.println("- ID: " + r.getIdentificador() + " | " + r.getTitulo()));
        System.out.print("ID: ");
        String id = scanner.nextLine();
        RecursoDigital recurso = gestorRecursos.buscarPorId(id);
        if (recurso == null || !recurso.esPrestable()) {
            throw new RecursoNoDisponibleExcepcion("Recurso no disponible o ya prestado.");
        }
        return recurso;
    }

    public List<Prestamo> listar() {
        return prestamos;
    }

    public Prestamo buscarPorId(int id) {
        return prestamos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Prestamo> buscarPorUsuario(Usuario usuario) {
        return prestamos.stream()
                .filter(p -> p.getUsuario().equals(usuario))
                .toList();
    }

    public void devolverPrestamo(Prestamo prestamo) {
        prestamo.devolver();
        prestamo.getRecurso().actualizarEstado(EstadoRecurso.DISPONIBLE);
        System.out.println("🔁 El recurso '" + prestamo.getRecurso().getTitulo() + "' ha sido devuelto y está disponible para préstamo.");
    }

    public boolean renovarPrestamo(int id) {
        Prestamo p = buscarPorId(id);
        RecursoDigital recurso = p != null ? p.getRecurso() : null;
        if (p != null && p.estaActivo() && recurso != null && recurso.esRenovable()) {
            p.renovar();
            return true;
        }
        return false;
    }

    public void mostrarTodos() {
        if (prestamos.isEmpty()) {
            System.out.println("⚠️ No hay préstamos registrados.\n");
        } else {
            System.out.println("=== Préstamos Registrados ===");
            prestamos.forEach(System.out::println);
            System.out.println();
        }
    }

    public boolean puedePrestarse(RecursoDigital recurso) {
        return prestamos.stream()
                .noneMatch(p -> p.getRecurso().equals(recurso) &&
                        (p.getEstado() == EstadosPrestamo.PRESTADO || p.getEstado() == EstadosPrestamo.RENOVADO));
    }
}
