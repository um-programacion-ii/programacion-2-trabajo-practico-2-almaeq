package gestores;

import enums.CategoriaRecurso;
import modelos.Prestamo;
import modelos.RecursoDigital;
import usuario.Usuario;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class GestorReportes {

    private final GestorPrestamo gestorPrestamo;
    private final ExecutorService executor;

    public GestorReportes(GestorPrestamo gestorPrestamo) {
        this.gestorPrestamo = gestorPrestamo;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void mostrarRecursosMasPrestados(int top) {
        executor.submit(() -> {
            System.out.println("\n📊 Reporte: Recursos más prestados\n");

            List<Prestamo> prestamos = gestorPrestamo.listar();

            if (prestamos.isEmpty()) {
                System.out.println("⚠️ No hay préstamos registrados.");
                return;
            }

            Map<RecursoDigital, Long> conteo = prestamos.stream()
                    .collect(Collectors.groupingBy(Prestamo::getRecurso, Collectors.counting()));

            List<Map.Entry<RecursoDigital, Long>> topRecursos = conteo.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(top)
                    .toList();

            int posicion = 1;
            for (Map.Entry<RecursoDigital, Long> entry : topRecursos) {
                System.out.printf("%d. %s - %d préstamo(s)%n", posicion++, entry.getKey().getTitulo(), entry.getValue());
            }

            System.out.println("✅ Reporte finalizado.\n");
        });
    }

    public void mostrarUsuariosMasActivos(int top) {
        executor.submit(() -> {
            System.out.println("\n👤 Reporte: Usuarios más activos\n");

            List<Prestamo> prestamos = gestorPrestamo.listar();

            if (prestamos.isEmpty()) {
                System.out.println("⚠️ No hay préstamos registrados.");
                return;
            }

            Map<Usuario, Long> conteo = prestamos.stream()
                    .collect(Collectors.groupingBy(Prestamo::getUsuario, Collectors.counting()));

            List<Map.Entry<Usuario, Long>> topUsuarios = conteo.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(top)
                    .toList();

            int posicion = 1;
            for (Map.Entry<Usuario, Long> entry : topUsuarios) {
                System.out.printf("%d. %s - %d préstamo(s)%n", posicion++, entry.getKey().getNombreCompleto(), entry.getValue());
            }

            System.out.println("✅ Reporte finalizado.\n");
        });
    }

    public void mostrarEstadisticasPorCategoria() {
        executor.submit(() -> {
            System.out.println("\n📈 Estadísticas de Uso por Categoría de Recurso\n");

            List<Prestamo> prestamos = gestorPrestamo.listar();

            if (prestamos.isEmpty()) {
                System.out.println("⚠️ No hay préstamos registrados.");
                return;
            }

            Map<CategoriaRecurso, Long> conteoPorCategoria = prestamos.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getRecurso().getCategoria(),
                            Collectors.counting()
                    ));

            conteoPorCategoria.forEach((categoria, cantidad) ->
                    System.out.printf("📚 %s: %d préstamo(s)%n", categoria, cantidad)
            );

            System.out.println("✅ Reporte finalizado.\n");
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
