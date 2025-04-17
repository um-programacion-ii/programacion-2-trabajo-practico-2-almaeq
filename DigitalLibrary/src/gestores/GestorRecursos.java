package gestores;

import recursos.*;

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

    // ✅ Buscar por ID (case-insensitive)
    public static RecursoDigital buscarPorId(String id) {
        return recursos.stream()
                .filter(r -> r.getIdentificador().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    // ✅ Buscar por TÍTULO exacto (ignore case)
    public static RecursoDigital buscarPorTitulo(String titulo) {
        return recursos.stream()
                .filter(r -> r.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    // ✅ Buscar recursos que contengan un fragmento del título (ignore case)
    public static List<RecursoDigital> buscarPorFragmentoTitulo(String fragmento) {
        String f = fragmento.toLowerCase();
        return recursos.stream()
                .filter(r -> r.getTitulo().toLowerCase().contains(f))
                .collect(Collectors.toList());
    }

    // ✅ Buscar por tipo de recurso (Libro, Revista, Audiolibro...)
    public static List<RecursoDigital> buscarPorTipo(Class<?> tipo) {
        return recursos.stream()
                .filter(tipo::isInstance)
                .collect(Collectors.toList());
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

    public static RecursoDigital crearRecurso(int tipo, String titulo, String id, EstadoRecurso estado, Scanner scanner) {
        return switch (tipo) {
            case 1 -> {
                System.out.print("Autor: ");
                String autor = scanner.nextLine();
                System.out.print("Cantidad de páginas: ");
                int paginas = Integer.parseInt(scanner.nextLine());
                yield new Libro(titulo, id, estado, paginas, autor);
            }
            case 2 -> {
                System.out.print("Número de edición: ");
                int numero = Integer.parseInt(scanner.nextLine());
                yield new Revista(titulo, id, estado, numero);
            }
            case 3 -> {
                System.out.print("Narrador: ");
                String narrador = scanner.nextLine();
                System.out.print("Duración (horas): ");
                double duracion = Double.parseDouble(scanner.nextLine());
                yield new Audiolibro(titulo, id, estado, narrador, duracion);
            }
            default -> null;
        };
    }


}
