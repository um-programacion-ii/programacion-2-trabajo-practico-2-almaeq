package interfaces;

import recursos.EstadoRecurso;

public interface IRecursoDigital {
    String getTitulo();
    String getIdentificador();
    EstadoRecurso getEstado();
    void actualizarEstado(EstadoRecurso estado);
}
