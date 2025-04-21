package utils;

import excepciones.UsuarioNoEncontradoExcepcion;
import gestores.GestorPrestamo;
import gestores.GestorRecursos;
import gestores.GestorUsuario;
import modelos.RecursoDigital;
import usuario.Usuario;

public class SimuladorPrestamos {

    public static void ejecutar(GestorUsuario gestorUsuario, GestorRecursos gestorRecursos, GestorPrestamo gestorPrestamo) {
        System.out.println("🧪 Simulando múltiples usuarios compitiendo por el mismo recurso...");

        // 🔁 Recurso compartido
        RecursoDigital recurso = gestorRecursos.buscarPorId("1");
        if (recurso == null) {
            System.out.println("❌ Recurso con ID '1' no encontrado. Aborta simulación.");
            return;
        }

        Runnable tarea1 = () -> simularPrestamo(gestorUsuario, gestorPrestamo, 1, recurso, "[HILO 1]");
        Runnable tarea2 = () -> simularPrestamo(gestorUsuario, gestorPrestamo, 2, recurso, "[HILO 2]");
        Runnable tarea3 = () -> simularPrestamo(gestorUsuario, gestorPrestamo, 3, recurso, "[HILO 3]");

        Thread t1 = new Thread(tarea1);
        Thread t2 = new Thread(tarea2);
        Thread t3 = new Thread(tarea3);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("❌ Error al esperar hilos: " + e.getMessage());
        }

        System.out.println("✅ Simulación completada.\n");
    }

    private static void simularPrestamo(GestorUsuario gestorUsuario, GestorPrestamo gestorPrestamo,
                                        int idUsuario, RecursoDigital recurso, String tag) {
        try {
            Usuario usuario = gestorUsuario.buscarPorId(idUsuario);
            if (usuario != null) {
                gestorPrestamo.crearPrestamo(usuario, recurso);
                System.out.println("✅ " + tag + " Préstamo otorgado a " + usuario.getNombre());
            }
        } catch (Exception e) {
            System.out.println("❌ " + tag + " Error para usuario ID " + idUsuario + ": " + e.getMessage());
        }
    }
}
