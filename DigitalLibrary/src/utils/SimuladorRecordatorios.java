package utils;

import alertas.RecordatorioPeriodico;
import enums.CategoriaRecurso;
import enums.EstadoPrestamo;
import enums.EstadoRecurso;
import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import modelos.RecursoDigital;
import usuario.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimuladorRecordatorios {

    public static void ejecutar() {
        System.out.println("🧪 Simulando recordatorios de vencimiento... (modo aislado)");

        // Mostrar hora de ejecución
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("🕒 Hora de ejecución: " + timestamp + "\n");

        // Usuarios ficticios
        Usuario usuario1 = new Usuario("Juan", "Pérez", "usuario1@example.com", 1);
        Usuario usuario2 = new Usuario("Ana", "López", "usuario2@example.com", 2);
        Usuario usuario3 = new Usuario("Luis", "Gómez", "usuario3@example.com", 3);

        // Recursos simulados
        RecursoDigital recurso1 = recursoSimulado("Libro 1");
        RecursoDigital recurso2 = recursoSimulado("Libro 2");
        RecursoDigital recurso3 = recursoSimulado("Libro 3");

        // Préstamos simulados
        List<Prestamo> prestamosSimulados = new ArrayList<>();
        prestamosSimulados.add(new Prestamo(1, recurso1, usuario1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), EstadoPrestamo.PRESTADO, 0)); // Mañana
        prestamosSimulados.add(new Prestamo(2, recurso2, usuario2, LocalDate.now().minusDays(2), LocalDate.now(), EstadoPrestamo.PRESTADO, 0)); // Hoy
        prestamosSimulados.add(new Prestamo(3, recurso3, usuario3, LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), EstadoPrestamo.PRESTADO, 0)); // Vencido

        // GestorPrestamo simulado
        GestorPrestamo gestorPrestamo = new GestorPrestamo(null, null, null, new Scanner(System.in)) {
            @Override
            public List<Prestamo> listar() {
                return prestamosSimulados;
            }
        };

        // Notificador simulado
        GestorNotificaciones gestorNotificaciones = new GestorNotificaciones() {
            @Override
            public void enviar(String destinatario, String mensaje) {
                System.out.println("📢 Alerta a " + destinatario + ": " + mensaje);
            }
        };

        // Ejecutar el recordatorio
        RecordatorioPeriodico recordatorio = new RecordatorioPeriodico(gestorPrestamo, gestorNotificaciones);
        recordatorio.run();
    }

    private static RecursoDigital recursoSimulado(String titulo) {
        return new RecursoDigital(titulo, EstadoRecurso.PRESTADO) {
            @Override public void prestarSiEsPosible() {}
            @Override public void devolverSiEsPosible() {}
            @Override public void renovarSiEsPosible() {}
            @Override public String mostrar() { return titulo; }
            @Override public CategoriaRecurso getCategoria() { return CategoriaRecurso.LIBRO; }
        };
    }
}
