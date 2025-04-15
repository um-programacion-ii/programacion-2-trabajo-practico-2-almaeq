package interfaces;

import java.time.LocalDate;

public interface Prestable {
    boolean estaPrestado();
    void prestar();
    void devolver();
    LocalDate getFechaPrestamo();
    LocalDate getFechaDevolucion();
}
