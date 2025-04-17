package utils;

import recursos.EstadoRecurso;
import recursos.RecursoDigital;
import usuario.Usuario;

import java.util.Comparator;

public class Comparadores {
    // Comparador de usuarios por apellido (alfabético)
    public static final Comparator<Usuario> POR_APELLIDO =
            Comparator.comparing(Usuario::getApellido, String.CASE_INSENSITIVE_ORDER);

    // Comparador de usuarios por nombre
    public static final Comparator<Usuario> POR_NOMBRE =
            Comparator.comparing(Usuario::getNombre, String.CASE_INSENSITIVE_ORDER);

    // Comparador de recursos por título
    public static final Comparator<RecursoDigital> POR_TITULO =
            Comparator.comparing(RecursoDigital::getTitulo, String.CASE_INSENSITIVE_ORDER);

    // Comparador de recursos por estado (DISPONIBLE primero)
    public static final Comparator<RecursoDigital> POR_ESTADO =
            Comparator.comparingInt(r -> r.getEstado() == EstadoRecurso.DISPONIBLE ? 0 : 1);

    // Comparador de recursos por renovable (renovable primero)
    public static final Comparator<RecursoDigital> POR_RENOVABLE =
            Comparator.comparing(RecursoDigital::esRenovable).reversed();

}
