package recursoDigital;

public class Revista implements RecursoDigital {
    private String identificador;
    private EstadoRecurso estado;
    private String titulo;
    private int numero;

    public Revista(String identificador, EstadoRecurso estado, String titulo, int numero) {
        this.identificador = identificador;
        this.estado = estado;
        this.titulo = titulo;
        this.numero = numero;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public String getIdentificador() {
        return identificador;
    }

    @Override
    public EstadoRecurso getEstado() {
        return estado;
    }

    @Override
    public void actualizarEstado(EstadoRecurso estado) {
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
