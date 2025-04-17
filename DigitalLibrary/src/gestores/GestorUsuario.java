package gestores;

import usuario.Usuario;

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

    public static Usuario buscarPorId(int id) {
        return usuarios.values()
                .stream()
                .filter(u -> u.getID() == id)
                .findFirst()
                .orElse(null);
    }

    public static Usuario buscarPorEmail(String email) {
        return usuarios.get(email);
    }

    // ✅ Buscar por nombre exacto (ignora mayúsculas)
    public static List<Usuario> buscarPorNombre(String nombre) {
        return usuarios.values()
                .stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre))
                .collect(Collectors.toList());
    }

    // ✅ Buscar por fragmento en nombre o apellido (ignora mayúsculas)
    public static List<Usuario> buscarPorFragmentoNombre(String fragmento) {
        String f = fragmento.toLowerCase();
        return usuarios.values()
                .stream()
                .filter(u -> u.getNombre().toLowerCase().contains(f)
                        || u.getApellido().toLowerCase().contains(f))
                .collect(Collectors.toList());
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


}
