package utils;

import alertas.AlertaVencimiento;
import enums.CanalNotificacion;
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
import java.util.Scanner;
import java.util.Set;

public class SimuladorAlertaVencimiento {

    public static void ejecutar() {
        System.out.println("🧪 Simulando alertas de vencimiento... (modo aislado)");

        Usuario usuario = new Usuario("Sim", "Usuario", "sim@demo.com");

        RecursoDigital recurso1 = recursoSimulado("Recurso 1");
        RecursoDigital recurso2 = recursoSimulado("Recurso 2");
        RecursoDigital recurso3 = recursoSimulado("Recurso 3");

        List<Prestamo> prestamosSimulados = new ArrayList<>();
        prestamosSimulados.add(new Prestamo(1, recurso1, usuario, LocalDate.now().minusDays(3), LocalDate.now().plusDays(1), EstadoPrestamo.PRESTADO, 0)); // Mañana
        prestamosSimulados.add(new Prestamo(2, recurso2, usuario, LocalDate.now().minusDays(5), LocalDate.now(), EstadoPrestamo.PRESTADO, 0)); // Hoy
        prestamosSimulados.add(new Prestamo(3, recurso3, usuario, LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), EstadoPrestamo.PRESTADO, 0)); // Ya vencido

        GestorPrestamo gestorPrestamoSimulado = new GestorPrestamo(null, null, null, null) {
            @Override
            public List<Prestamo> listar() {
                return prestamosSimulados;
            }
            @Override
            public Prestamo buscarPorId(int id) {
                return prestamosSimulados.stream()
                        .filter(p -> p.getId() == id)
                        .findFirst()
                        .orElse(null);
            }
            @Override
            public boolean renovarPrestamo(int id) {
                Prestamo p = buscarPorId(id);
                if (p != null && p.estaActivo()) {
                    System.out.print("📅 Ingrese la nueva fecha de devolución (YYYY-MM-DD): ");
                    LocalDate nuevaFecha = LocalDate.parse(new Scanner(System.in).nextLine());

                    if (nuevaFecha.isBefore(LocalDate.now())) {
                        System.out.println("❌ La nueva fecha de devolución no puede ser anterior a hoy.");
                        return false;
                    }

                    p.renovar();
                    p.setFechaDevolucion(nuevaFecha);
                    System.out.println("✅ Préstamo renovado correctamente. Nueva devolución: " + nuevaFecha);
                    return true;
                } else {
                    System.out.println("❌ Préstamo no encontrado o ya no está activo.");
                    return false;
                }
            }
        };

        GestorNotificaciones notificadorSimulado = new GestorNotificaciones() {
        @Override
        public void enviar(String destinatario, String mensaje, Set<CanalNotificacion> canalesPreferidos) {
            System.out.println("📢 Alerta a " + destinatario + ": " + mensaje);
        }
    };

        AlertaVencimiento alerta = new AlertaVencimiento(gestorPrestamoSimulado, notificadorSimulado, new Scanner(System.in));        alerta.verificarYNotificarVencimientos();
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
