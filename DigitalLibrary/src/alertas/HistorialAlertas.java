package alertas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialAlertas {
    private static final List<String> historial = new ArrayList<>();

    public static void registrar(String mensaje) {
        historial.add(mensaje);
    }

    public static List<String> obtenerHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public static void mostrarHistorial() {
        System.out.println("\n📜 === HISTORIAL DE ALERTAS ===");
        if (historial.isEmpty()) {
            System.out.println("🟢 No hay alertas registradas.");
        } else {
            historial.forEach(System.out::println);
        }
        System.out.println("📜 === FIN DEL HISTORIAL ===\n");
    }

    public static void limpiarHistorial() {
        historial.clear();
    }
}
