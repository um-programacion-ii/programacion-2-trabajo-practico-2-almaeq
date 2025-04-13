package recursoDigital;

public class Libro implements RecursoDigital {
    private String titulo;
    private String identificador;
    private EstadoRecurso estado;
    private int cant_paginas;
    private String autor;

    public Libro(String titulo, String identificador, EstadoRecurso estado, int cant_paginas, String autor) {
        this.titulo = titulo;
        this.identificador = identificador;
        this.estado = estado;
        this.cant_paginas = cant_paginas;
        this.autor = autor;
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

    public int getCant_paginas() {
        return cant_paginas;
    }

    public void setCant_paginas(int cant_paginas) {
        this.cant_paginas = cant_paginas;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
