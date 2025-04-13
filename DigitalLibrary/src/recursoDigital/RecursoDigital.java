package recursoDigital;

public interface RecursoDigital {
    String getTitulo();
    String getIdentificador();
    EstadoRecurso getEstado();
    void actualizarEstado(EstadoRecurso estado);
}
