package gestores;

import usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuario {

    private static final List<Usuario> usuarios = new ArrayList<>();

    public static Usuario getUsuario(String nombre, String apellido, String email, int ID) {
        Usuario nuevo = new Usuario(nombre, apellido, email, ID);
        usuarios.add(nuevo);
        return nuevo;
    }

    public static List<Usuario> listar() {
        return usuarios;
    }

    public static boolean estaVacio() {
        return usuarios.isEmpty();
    }

    public static Usuario buscarPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getID() == id) {
                return u;
            }
        }
        return null;
    }
}
