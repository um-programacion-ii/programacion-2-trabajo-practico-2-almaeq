package gestores;

import usuario.Usuario;

public class GestorUsuario {
    public  static Usuario getUsuario(String nombre, String apellido, String email, int ID){
        return new Usuario(nombre, apellido, email, ID);
    }
}
