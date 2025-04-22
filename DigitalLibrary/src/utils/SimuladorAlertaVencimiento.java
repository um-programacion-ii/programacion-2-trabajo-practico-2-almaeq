package utils;

import alertas.AlertaVencimiento;
import enums.CategoriaRecurso;
import enums.EstadoPrestamo;
import enums.EstadoRecurso;
import gestores.GestorNotificaciones;
import gestores.GestorPrestamo;
import modelos.Prestamo;
import modelos.RecursoDigital;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SimuladorAlertaVencimiento {

    public static void ejecutar() {
        System.out.println("🧪 Simulando alertas de vencimiento... (modo aislado)");

        Usuario usuario = new Usuario("Sim", "Usuario", "sim@demo.com", 999);

        RecursoDigital recurso1 = recursoSimulado("Recurso 1");
        RecursoDigital recurso2 = recursoSimulado("Recurso 2");
        RecursoDigital recurso3 = recursoSimulado("Recurso 3");

        List<Prestamo> prestamosSimulados = new ArrayList<>();
        prestamosSimulados.add(new Prestamo(1, recurso1, usuario, LocalDate.now().minusDays(3), LocalDate.now().plusDays(1), EstadoPrestamo.PRESTADO, 0)); // Mañana
        prestamosSimulados.add(new Prestamo(2, recurso2, usuario, LocalDate.now().minusDays(5), LocalDate.now(), EstadoPrestamo.PRESTADO, 0)); // Hoy
        prestamosSimulados.add(new Prestamo(3, recurso3, usuario, LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), EstadoPrestamo.PRESTADO, 0)); // Ya vencido

        // GestorPrestamo simulado solo para esta ejecución
        GestorPrestamo gestorPrestamoSimulado = new GestorPrestamo(null, null, null, null) {
            @Override
            public List<Prestamo> listar() {
                return prestamosSimulados;
            }
        };

        GestorNotificaciones notificadorSimulado = new GestorNotificaciones() {
            @Override
            public void enviar(String destinatario, String mensaje) {
                System.out.println("📢 Alerta a " + destinatario + ": " + mensaje);
            }
        };

        AlertaVencimiento alerta = new AlertaVencimiento(gestorPrestamoSimulado, notificadorSimulado);
        alerta.verificarYNotificarVencimientos();
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
