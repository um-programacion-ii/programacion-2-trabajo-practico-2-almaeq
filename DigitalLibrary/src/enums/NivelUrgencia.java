package enums;

public enum NivelUrgencia {
    INFO("\u2139"),         // ℹ️ Información
    WARNING("\u26A0"),      // ⚠️ Advertencia
    ERROR("\u2757");        // ❗ Error

    private final String simbolo;

    NivelUrgencia(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

}
