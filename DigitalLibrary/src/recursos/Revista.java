package recursos;

import recursos.RecursoDigital;

public class Revista extends RecursoDigital {
    private int numero;

    public Revista(String titulo, String identificador, EstadoRecurso estado, int numero) {
        super(titulo, identificador, estado);
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String mostrar() {
        return "📰 Revista - " + titulo + " | Nº: " + numero + " | Estado: " + estado;
    }
}