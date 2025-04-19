package gestores;

import excepciones.UsuarioNoEncontradoExcepcion;
import usuario.Usuario;
import utils.Comparadores;

import java.util.*;
import java.util.stream.Collectors;

public class GestorUsuario {

    private static final Map<String, Usuario> usuarios = new HashMap<>();

    public static Usuario getUsuario(String nombre, String apellido, String email, int ID) {
        Usuario nuevo = new Usuario(nombre, apellido, email, ID);
        usuarios.put(email, nuevo);
        return nuevo;
    }

    public static List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }

    public static boolean estaVacio() {
        return usuarios.isEmpty();
    }

    public static Usuario buscarPorId(int id) throws UsuarioNoEncontradoExcepcion {
        return usuarios.values()
                .stream()
                .filter(u -> u.getID() == id)
                .findFirst()
                .orElseThrow(() -> new UsuarioNoEncontradoExcepcion("Usuario con ID " + id + " no encontrado."));
    }

    public static List<Usuario> buscarPorFragmento(String fragmento) throws UsuarioNoEncontradoExcepcion {
        String f = fragmento.toLowerCase();
        List<Usuario> resultado = usuarios.values()
                .stream()
                .filter(u -> u.getNombre().toLowerCase().contains(f)
                        || u.getApellido().toLowerCase().contains(f))
                .toList();
        if (resultado.isEmpty()) {
            throw new UsuarioNoEncontradoExcepcion("No se encontraron usuarios que coincidan con: " + fragmento);
        }
        return resultado;
    }

    public static void mostrarListado() {
        if (estaVacio()) {
            System.out.println("⚠️ No hay usuarios cargados.\n");
        } else {
            System.out.println("=== Lista de Usuarios ===");
            usuarios.values().forEach(System.out::println);
            System.out.println();
        }
    }

    public static Usuario crearUsuarioDesdeInput(Scanner scanner) {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("ID (número): ");
        int id = Integer.parseInt(scanner.nextLine());

        Usuario nuevo = new Usuario(nombre, apellido, email, id);
        usuarios.put(email, nuevo);
        return nuevo;
    }

    public static List<Usuario> listarOrdenadoPorApellido(String apellido) {
        return usuarios.values().stream()
                .sorted(Comparadores.POR_APELLIDO)
                .filter(u -> u.getApellido().equalsIgnoreCase(apellido))
                .toList();
    }

    public static List<Usuario> buscarPorNombreOrdenado(String nombre) throws UsuarioNoEncontradoExcepcion {
        List<Usuario> resultado = usuarios.values().stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre))
                .sorted(Comparadores.POR_NOMBRE)
                .toList();

        if (resultado.isEmpty()) {
            throw new UsuarioNoEncontradoExcepcion("No se encontraron usuarios con nombre: " + nombre);
        }
        return resultado;
    }


}
