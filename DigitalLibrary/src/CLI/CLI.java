package CLI;

import recursos.*;
import gestores.GestorUsuario;
import gestores.GestorRecursos;
import usuario.Usuario;

import java.util.Scanner;

public class CLI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> listarUsuarios();
                case 3 -> crearRecursoDigital();
                case 4 -> listarRecursosDigitales();
                case 5 -> prestarRecurso();
                case 6 -> devolverRecurso();
                case 7 -> renovarRecurso();
                case 8 -> System.out.println("Saliendo...");
                default -> System.out.println("❌ Opción inválida.\n");
            }

        } while (opcion != 8);
    }

    private static void mostrarMenu() {
        System.out.println("""
                === MENÚ PRINCIPAL ===
                1. Crear Usuario
                2. Listar Usuarios
                3. Crear Recurso Digital
                4. Listar Recursos Digitales
                5. Prestar Recurso
                6. Devolver Recurso
                7. Renovar Recurso
                8. Salir
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

        GestorUsuario.getUsuario(nombre, apellido, email, id);
        System.out.println("✅ Usuario creado con éxito.\n");
    }

    private static void listarUsuarios() {
        if (GestorUsuario.estaVacio()) {
            System.out.println("⚠️ No hay usuarios cargados.\n");
        } else {
            System.out.println("=== Lista de Usuarios ===");
            for (Usuario u : GestorUsuario.listar()) {
                System.out.println(u);
            }
            System.out.println();
        }
    }

    private static void crearRecursoDigital() {
        System.out.println("""
                Tipo de recurso:
                1. Libro
                2. Revista
                3. Audiolibro
                """);
        int tipo = Integer.parseInt(scanner.nextLine());

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Id: ");
        String id = scanner.nextLine();

        EstadoRecurso estado = EstadoRecurso.DISPONIBLE;

        switch (tipo) {
            case 1 -> {
                System.out.print("Autor: ");
                String autor = scanner.nextLine();
                System.out.print("Cantidad de páginas: ");
                int paginas = Integer.parseInt(scanner.nextLine());

                GestorRecursos.agregar(new Libro(titulo, id, estado, paginas, autor));
                System.out.println("📘 Libro agregado.\n");
            }
            case 2 -> {
                System.out.print("Número de edición: ");
                int numero = Integer.parseInt(scanner.nextLine());

                GestorRecursos.agregar(new Revista(titulo, id, estado, numero));
                System.out.println("📰 Revista agregada.\n");
            }
            case 3 -> {
                System.out.print("Narrador: ");
                String narrador = scanner.nextLine();
                System.out.print("Duración (horas): ");
                double duracion = Double.parseDouble(scanner.nextLine());

                GestorRecursos.agregar(new Audiolibro(titulo, id, estado, narrador, duracion));
                System.out.println("🎧 Audiolibro agregado.\n");
            }
            default -> System.out.println("❌ Tipo inválido.\n");
        }
    }

    private static void listarRecursosDigitales() {
        if (GestorRecursos.estaVacio()) {
            System.out.println("⚠️ No hay recursos digitales cargados.\n");
        } else {
            System.out.println("=== Recursos Digitales ===");
            for (RecursoDigital r : GestorRecursos.listar()) {
                System.out.println(r.mostrar());
            }
            System.out.println();
        }
    }

    private static void prestarRecurso() {
        System.out.print("Ingrese el ID del recurso a prestar: ");
        String id = scanner.nextLine();

        RecursoDigital encontrado = GestorRecursos.buscarPorId(id);
        if (encontrado != null) {
            encontrado.prestarSiEsPosible();
        } else {
            System.out.println("❌ Recurso no encontrado.");
        }
    }

    private static void devolverRecurso() {
        System.out.print("Ingrese el ID del recurso a devolver: ");
        String id = scanner.nextLine();

        RecursoDigital encontrado = GestorRecursos.buscarPorId(id);
        if (encontrado != null) {
            encontrado.devolverSiEsPosible();
        } else {
            System.out.println("❌ Recurso no encontrado.");
        }
    }

    private static void renovarRecurso() {
        System.out.print("Ingrese el ID del recurso a renovar: ");
        String id = scanner.nextLine();

        RecursoDigital encontrado = GestorRecursos.buscarPorId(id);
        if (encontrado != null) {
            encontrado.renovarSiEsPosible();
        } else {
            System.out.println("❌ Recurso no encontrado.");
        }
    }
}
