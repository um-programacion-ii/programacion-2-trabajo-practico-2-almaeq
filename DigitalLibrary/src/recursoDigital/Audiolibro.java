package recursoDigital;

public class Audiolibro implements RecursoDigital {
    private String nombre;
    private String identificador;
    private EstadoRecurso estado;
    private String narrador;
    private double duracion;

    public Audiolibro(String nombre, String identificador, EstadoRecurso estado, String narrador, double duracion) {
        this.nombre = nombre;
        this.identificador = identificador;
        this.estado = estado;
        this.narrador = narrador;
        this.duracion = duracion;
    }

    @Override
    public String getTitulo() {
        return nombre;
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

    public String getNarrador() {
        return narrador;
    }

    public void setNarrador(String narrador) {
        this.narrador = narrador;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }
}
