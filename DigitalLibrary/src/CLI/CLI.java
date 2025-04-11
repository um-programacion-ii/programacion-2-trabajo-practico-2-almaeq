package CLI;

import usuario.GestorUsuario;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

public class CLI {

    private static final ArrayList<Usuario> usuarios = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> listarUsuarios();
                case 3 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 3);
    }

    private static void mostrarMenu() {
        System.out.println("""
                === MENÚ PRINCIPAL ===
                1. Crear Usuario
                2. Listar Usuarios
                3. Salir
                Ingrese una opción:
                """);
    }

    private static void crearUsuario() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("ID (número): ");
        int id = Integer.parseInt(scanner.nextLine());

        Usuario nuevo = GestorUsuario.getUsuario(nombre, apellido, email, id);
        usuarios.add(nuevo);
        System.out.println("✅ Usuario creado con éxito.\n");
    }

    private static void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("⚠️ No hay usuarios cargados.");
        } else {
            System.out.println("=== Lista de Usuarios ===");
            for (Usuario u : usuarios) {
                System.out.println(u);
            }
        }
        System.out.println();
    }
}
