package gestores;

import modelos.Prestamo;
import modelos.RecursoDigital;

import java.util.*;
import java.util.stream.Collectors;

public class GestorReportes {

    private final GestorPrestamo gestorPrestamo;

    public GestorReportes(GestorPrestamo gestorPrestamo) {
        this.gestorPrestamo = gestorPrestamo;
    }

    public void mostrarRecursosMasPrestados(int top) {
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

        System.out.println();
    }

}
