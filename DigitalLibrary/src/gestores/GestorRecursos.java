package gestores;

import enums.CategoriaRecurso;
import enums.EstadoRecurso;
import recursos.*;
import utils.Comparadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GestorRecursos {

    private static final List<RecursoDigital> recursos = new ArrayList<>();

    public static void agregar(RecursoDigital recurso) {
        recursos.add(recurso);
    }

    public static List<RecursoDigital> listar() {
        return recursos;
    }

    public static boolean estaVacio() {
        return recursos.isEmpty();
    }

    public static RecursoDigital buscarPorId(String id) {
        return recursos.stream()
                .filter(r -> r.getIdentificador().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public static RecursoDigital buscarPorTitulo(String titulo) {
        return recursos.stream()
                .filter(r -> r.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    public static List<RecursoDigital> buscarPorFragmentoTitulo(String fragmento) {
        String f = fragmento.toLowerCase();
        return recursos.stream()
                .filter(r -> r.getTitulo().toLowerCase().contains(f))
                .collect(Collectors.toList());
    }

    public static List<RecursoDigital> buscarPorTipo(Class<?> tipo) {
        return recursos.stream()
                .filter(tipo::isInstance)
                .collect(Collectors.toList());
    }

    public static List<RecursoDigital> buscarPorCategoria(CategoriaRecurso categoria) {
        return switch (categoria) {
            case LIBRO -> buscarPorTipo(Libro.class);
            case REVISTA -> buscarPorTipo(Revista.class);
            case AUDIOLIBRO -> buscarPorTipo(Audiolibro.class);
        };
    }

    public static void mostrarListado() {
        if (estaVacio()) {
            System.out.println("⚠️ No hay recursos digitales cargados.\n");
        } else {
            System.out.println("=== Recursos Disponibles ===");
            recursos.forEach(System.out::println);
            System.out.println();
        }
    }

    public static RecursoDigital crearRecurso(CategoriaRecurso categoria, String titulo, String id, EstadoRecurso estado, Scanner scanner) {
        return switch (categoria) {
            case LIBRO -> {
                System.out.print("Autor: ");
                String autor = scanner.nextLine();
                System.out.print("Cantidad de páginas: ");
                int paginas = Integer.parseInt(scanner.nextLine());
                yield new Libro(titulo, id, estado, paginas, autor);
            }
            case REVISTA -> {
                System.out.print("Número de edición: ");
                int numero = Integer.parseInt(scanner.nextLine());
                yield new Revista(titulo, id, estado, numero);
            }
            case AUDIOLIBRO -> {
                System.out.print("Narrador: ");
                String narrador = scanner.nextLine();
                System.out.print("Duración (horas): ");
                double duracion = Double.parseDouble(scanner.nextLine());
                yield new Audiolibro(titulo, id, estado, narrador, duracion);
            }
        };
    }

    public static List<RecursoDigital> listarPorTitulo() {
        return recursos.stream()
                .sorted(Comparadores.POR_TITULO)
                .toList();
    }

    public static List<RecursoDigital> listarPorEstado() {
        return recursos.stream()
                .sorted(Comparadores.POR_ESTADO)
                .toList();
    }

    public static List<RecursoDigital> listarPorRenovable() {
        return recursos.stream()
                .sorted(Comparadores.POR_RENOVABLE)
                .toList();
    }
}
