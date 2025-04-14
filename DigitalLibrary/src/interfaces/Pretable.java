package interfaces;

import java.time.LocalDate;

public interface Pretable {
    boolean estaPrestado();
    void prestar();
    void devolver();
    LocalDate getFechaPrestamo();
    LocalDate getFechaDevolucion();
}
