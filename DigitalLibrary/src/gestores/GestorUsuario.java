package gestores;

import usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorUsuario {

    // Usamos un HashMap para gestionar usuarios por email
    private static final Map<String, Usuario> usuarios = new HashMap<>();

    // Crear y agregar usuario al mapa
    public static Usuario getUsuario(String nombre, String apellido, String email, int ID) {
        Usuario nuevo = new Usuario(nombre, apellido, email, ID);
        usuarios.put(email, nuevo);
        return nuevo;
    }

    // Listar todos los usuarios
    public static List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }

    // Verificar si está vacío
    public static boolean estaVacio() {
        return usuarios.isEmpty();
    }

    // Buscar por ID (revisando los valores del mapa)
    public static Usuario buscarPorId(int id) {
        for (Usuario u : usuarios.values()) {
            if (u.getID() == id) {
                return u;
            }
        }
        return null;
    }

    // Buscar por email directamente
    public static Usuario buscarPorEmail(String email) {
        return usuarios.get(email);
    }
}
