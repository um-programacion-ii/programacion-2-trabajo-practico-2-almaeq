package utils;

import enums.CategoriaRecurso;
import enums.EstadoPrestamo;
import enums.EstadoRecurso;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import modelos.RecursoDigital;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimuladorPrestamos {

    public static void ejecutar() {
        System.out.println("🧪 Simulando múltiples usuarios compitiendo por el mismo recurso... (modo aislado)");

        // 🧪 Crear usuarios simulados
        Usuario usuario1 = new Usuario("Sim", "Uno", "sim1@demo.com");
        Usuario usuario2 = new Usuario("Sim", "Dos", "sim2@demo.com");
        Usuario usuario3 = new Usuario("Sim", "Tres", "sim3@demo.com");

        // 🧪 Crear recurso simulado compartido
        RecursoDigital recurso = new RecursoDigital("Recurso Compartido", EstadoRecurso.DISPONIBLE) {
            @Override public void prestarSiEsPosible() {}
            @Override public void devolverSiEsPosible() {}
            @Override public void renovarSiEsPosible() {}
            @Override public String mostrar() { return getTitulo(); }
            @Override public CategoriaRecurso getCategoria() { return CategoriaRecurso.LIBRO; }
        };

        // 📦 Crear gestorPrestamo aislado
        GestorPrestamo gestorPrestamo = new GestorPrestamo(null, null, null, new Scanner(System.in)) {
            private final List<Prestamo> prestamosSimulados = new ArrayList<>();
            private int id = 1;

            @Override
            public List<Prestamo> listar() {
                return prestamosSimulados;
            }

            @Override
            public synchronized Prestamo crearPrestamo(Usuario usuario, RecursoDigital recurso, LocalDate fechaDevolucion) {
                if (!puedePrestarse(recurso)) {
                    throw new RuntimeException("El recurso ya está prestado.");
                }
                recurso.actualizarEstado(EstadoRecurso.PRESTADO);
                Prestamo p = new Prestamo(id++, recurso, usuario, LocalDate.now(), fechaDevolucion, EstadoPrestamo.PRESTADO, 0);
                prestamosSimulados.add(p);
                return p;
            }

            @Override
            public synchronized boolean puedePrestarse(RecursoDigital recurso) {
                return prestamosSimulados.stream()
                        .noneMatch(p -> p.getRecurso().equals(recurso) && p.estaActivo());
            }
        };

        LocalDate fechaDevolucion = LocalDate.now().plusDays(3);

        Thread t1 = new Thread(() -> simularPrestamo(usuario1, recurso, fechaDevolucion, "[HILO 1]", gestorPrestamo));
        Thread t2 = new Thread(() -> simularPrestamo(usuario2, recurso, fechaDevolucion, "[HILO 2]", gestorPrestamo));
        Thread t3 = new Thread(() -> simularPrestamo(usuario3, recurso, fechaDevolucion, "[HILO 3]", gestorPrestamo));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("❌ Error al esperar hilos: " + e.getMessage());
        }

        System.out.println("📋 Resultado final:");
        gestorPrestamo.listar().forEach(System.out::println);
        System.out.println("✅ Simulación aislada completada.\n");
    }

    private static void simularPrestamo(Usuario usuario, RecursoDigital recurso,
                                        LocalDate fechaDevolucion, String tag,
                                        GestorPrestamo gestorPrestamo) {
        try {
            gestorPrestamo.crearPrestamo(usuario, recurso, fechaDevolucion);
            System.out.println("✅ " + tag + " Préstamo otorgado a " + usuario.getNombre());
        } catch (Exception e) {
            System.out.println("❌ " + tag + " Error: " + e.getMessage());
        }
    }
}
