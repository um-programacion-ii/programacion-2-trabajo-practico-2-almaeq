package CLI;

import recursos.*;
import gestores.GestorUsuario;
import gestores.GestorRecursos;
import servicios.ServicioNotificaciones;
import servicios.ServicioNotificacionesEmail;
import servicios.ServicioNotificacionesSMS;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.List;
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
                case 2 -> submenuBuscarUsuario();
                case 3 -> crearRecursoDigital();
                case 4 -> submenuBuscarRecurso();
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
                2. Buscar Usuario
                3. Crear Recurso Digital
                4. Buscar Recurso
                5. Prestar Recurso
                6. Devolver Recurso
                7. Renovar Recurso
                8. Salir
                Ingrese una opción:
                """);
    }

    private static void submenuBuscarUsuario() {
        int opcion;
        do {
            System.out.println("""
                === BUSCAR USUARIO ===
                1. Listar todos
                2. Buscar por nombre exacto
                3. Buscar por fragmento en nombre o apellido
                4. Listar ordenados por apellido
                5. Listar ordenados por nombre y apellido
                6. Volver al Menú Principal
                """);
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> GestorUsuario.mostrarListado();
                case 2 -> buscarUsuarioPorNombre();
                case 3 -> buscarUsuarioPorFragmento();
                case 4 -> listarUsuariosPorApellido();
                case 5 -> listarUsuariosPorNombre();
                case 6 -> System.out.println("↩️ Volviendo...\n");
                default -> System.out.println("❌ Opción inválida.\n");
            }
        } while (opcion != 6);
    }


    private static void submenuBuscarRecurso() {
        int opcion;
        do {
            System.out.println("""
                === BUSCAR RECURSO ===
                1. Listar Todos
                2. Buscar por Título Exacto
                3. Buscar por Palabra en el Título
                4. Buscar por Tipo
                5. Listar ordenados por Título
                6. Listar ordenados por Estado
                7. Listar recursos renovables primero
                8. Volver al Menú Principal
                """);
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> GestorRecursos.mostrarListado();
                case 2 -> buscarRecursoPorTituloExacto();
                case 3 -> buscarRecursosPorPalabraEnTitulo();
                case 4 -> buscarRecursosPorTipo();
                case 5 -> listarOrdenadoPorTitulo();
                case 6 -> listarOrdenadoPorEstado();
                case 7 -> listarOrdenadoPorRenovable();
                case 8 -> System.out.println("↩️ Volviendo...\n");
                default -> System.out.println("❌ Opción inválida.\n");
            }
        } while (opcion != 8);
    }


    private static void crearUsuario() {
        Usuario nuevo = GestorUsuario.crearUsuarioDesdeInput(scanner);
        System.out.println("✅ Usuario creado con éxito: " + nuevo + "\n");
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
        RecursoDigital recurso = GestorRecursos.crearRecurso(tipo, titulo, id, estado, scanner);

        GestorRecursos.agregar(recurso);
        System.out.println("✅ Recurso agregado con éxito.\n");
    }

    private static void prestarRecurso() {
        // Verificar que haya usuarios
        if (GestorUsuario.estaVacio()) {
            System.out.println("⚠️ No hay usuarios registrados. Cree uno primero.\n");
            return;
        }
        // Verificar que haya recursos
        if (GestorRecursos.estaVacio()) {
            System.out.println("⚠️ No hay recursos digitales cargados.\n");
            return;
        }
        // Mostrar recursos disponibles
        GestorRecursos.mostrarListado();
        // Seleccionar usuario
        System.out.println("Seleccione un usuario por ID para prestar el recurso:");
        GestorUsuario.listar().forEach(u ->
                System.out.println("- ID: " + u.getID() + " | " + u.getNombre() + " " + u.getApellido()));
        int userId = Integer.parseInt(scanner.nextLine());
        Usuario usuario = GestorUsuario.buscarPorId(userId);
        if (usuario == null) {
            System.out.println("❌ Usuario no encontrado.");
            return;
        }
        // Ingresar ID del recurso
        System.out.print("Ingrese el ID del recurso a prestar: ");
        String recursoId = scanner.nextLine();
        RecursoDigital recurso = GestorRecursos.buscarPorId(recursoId);
        if (recurso == null) {
            System.out.println("❌ Recurso no encontrado.");
            return;
        }

        // Configurar notificaciones
        ServicioNotificacionesEmail email = new ServicioNotificacionesEmail();
        ServicioNotificacionesSMS sms = new ServicioNotificacionesSMS();
        email.activarNotificaciones(usuario.getEmail());
        sms.activarNotificaciones(usuario.getEmail());

        List<ServicioNotificaciones> servicios = new ArrayList<>();
        servicios.add(email);
        servicios.add(sms);
        recurso.configurarNotificaciones(servicios, usuario.getEmail());

        recurso.prestarSiEsPosible();
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

    private static void buscarUsuarioPorNombre() {
        System.out.print("Ingrese el nombre a buscar: ");
        String nombre = scanner.nextLine();
        List<Usuario> resultados = GestorUsuario.buscarPorNombreOrdenado(nombre);

        if (resultados.isEmpty()) {
            System.out.println("⚠️ No se encontraron usuarios con ese nombre.");
        } else {
            System.out.println("=== Usuarios Encontrados ===");
            resultados.forEach(System.out::println);
        }
        System.out.println();
    }

    private static void buscarRecursoPorTituloExacto() {
        System.out.print("Ingrese el título exacto del recurso: ");
        String titulo = scanner.nextLine();
        RecursoDigital recurso = GestorRecursos.buscarPorTitulo(titulo);

        if (recurso != null) {
            System.out.println("✅ Recurso encontrado:");
            System.out.println(recurso.mostrar());
        } else {
            System.out.println("❌ No se encontró un recurso con ese título.");
        }
        System.out.println();
    }

    private static void buscarRecursosPorPalabraEnTitulo() {
        System.out.print("Ingrese una palabra clave del título: ");
        String palabra = scanner.nextLine();
        List<RecursoDigital> resultados = GestorRecursos.buscarPorFragmentoTitulo(palabra);

        if (resultados.isEmpty()) {
            System.out.println("⚠️ No se encontraron recursos con esa palabra.");
        } else {
            System.out.println("=== Recursos Encontrados ===");
            resultados.forEach(r -> System.out.println(r.mostrar()));
        }
        System.out.println();
    }

    private static void buscarRecursosPorTipo() {
        System.out.println("""
                Tipo de recurso a buscar:
                1. Libro
                2. Revista
                3. Audiolibro
                """);
        int opcion = Integer.parseInt(scanner.nextLine());

        Class<?> tipo = switch (opcion) {
            case 1 -> Libro.class;
            case 2 -> Revista.class;
            case 3 -> Audiolibro.class;
            default -> null;
        };

        if (tipo == null) {
            System.out.println("❌ Tipo inválido.\n");
            return;
        }

        List<RecursoDigital> resultados = GestorRecursos.buscarPorTipo(tipo);

        if (resultados.isEmpty()) {
            System.out.println("⚠️ No se encontraron recursos de ese tipo.\n");
        } else {
            System.out.println("=== Recursos Encontrados ===");
            resultados.forEach(r -> System.out.println(r.mostrar()));
            System.out.println();
        }
    }

    private static void listarOrdenadoPorTitulo() {
        List<RecursoDigital> ordenados = GestorRecursos.listarPorTitulo();
        System.out.println("=== Recursos ordenados por Título ===");
        ordenados.forEach(r -> System.out.println(r.mostrar()));
        System.out.println();
    }

    private static void listarOrdenadoPorEstado() {
        List<RecursoDigital> ordenados = GestorRecursos.listarPorEstado();
        System.out.println("=== Recursos ordenados por Estado (DISPONIBLE primero) ===");
        ordenados.forEach(r -> System.out.println(r.mostrar()));
        System.out.println();
    }

    private static void listarOrdenadoPorRenovable() {
        List<RecursoDigital> ordenados = GestorRecursos.listarPorRenovable();
        System.out.println("=== Recursos ordenados por posibilidad de Renovación ===");
        ordenados.forEach(r -> System.out.println(r.mostrar()));
        System.out.println();
    }

    private static void buscarUsuarioPorFragmento() {
        System.out.print("Ingrese fragmento del nombre o apellido: ");
        String frag = scanner.nextLine();
        List<Usuario> resultados = GestorUsuario.buscarPorFragmentoNombre(frag);

        if (resultados.isEmpty()) {
            System.out.println("⚠️ No se encontraron usuarios.");
        } else {
            System.out.println("=== Usuarios Encontrados ===");
            resultados.forEach(System.out::println);
        }
        System.out.println();
    }

    private static void listarUsuariosPorApellido() {
        List<Usuario> ordenados = GestorUsuario.listar().stream()
                .sorted(utils.Comparadores.POR_APELLIDO)
                .toList();

        System.out.println("=== Usuarios ordenados por Apellido ===");
        ordenados.forEach(System.out::println);
        System.out.println();
    }

    private static void listarUsuariosPorNombre() {
        List<Usuario> ordenados = GestorUsuario.listar().stream()
                .sorted(utils.Comparadores.POR_NOMBRE)
                .toList();

        System.out.println("=== Usuarios ordenados por Nombre y Apellido ===");
        ordenados.forEach(System.out::println);
        System.out.println();
    }


}
