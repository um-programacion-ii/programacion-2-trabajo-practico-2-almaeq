package gestores;

import recursos.RecursoDigital;

import java.util.ArrayList;

public class GestorRecursos {

    private static final ArrayList<RecursoDigital> recursos = new ArrayList<>();

    public static void agregar(RecursoDigital recurso) {
        recursos.add(recurso);
    }

    public static ArrayList<RecursoDigital> listar() {
        return recursos;
    }

    public static RecursoDigital buscarPorId(String id) {
        for (RecursoDigital r : recursos) {
            if (r.getIdentificador().equalsIgnoreCase(id)) {
                return r;
            }
        }
        return null;
    }

    public static boolean estaVacio() {
        return recursos.isEmpty();
    }
}
