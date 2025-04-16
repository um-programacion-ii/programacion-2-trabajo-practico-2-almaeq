package gestores;

import recursos.RecursoDigital;

import java.util.ArrayList;
import java.util.List;
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
}
