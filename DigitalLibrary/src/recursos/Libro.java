package recursos;

import recursos.RecursoDigital;

public class Libro extends RecursoDigital {
    private int cant_paginas;
    private String autor;

    public Libro(String titulo, String identificador, EstadoRecurso estado, int cant_paginas, String autor) {
        super(titulo, identificador, estado);
        this.cant_paginas = cant_paginas;
        this.autor = autor;
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

    @Override
    public String mostrar() {
        return "📘 Libro - " + titulo + " | Autor: " + autor + " | Páginas: " + cant_paginas + " | Estado: " + estado;
    }
}

