package co.stylovibes.dominio;

public enum Seccion {
    ROSTRO_Y_CORTES("Rostro y cortes"),
    FACCIONES_Y_CUIDADO("Facciones y cuidado facial"),
    COLORIMETRIA_Y_PALETAS("Colorimetría y paletas"),
    CUERPO_Y_VESTUARIO("Tipo de cuerpo y vestuario"),
    ESTILO_PERSONAL("Estilo personal"),
    OUTFITS("Outfits y combinaciones"),
    ACCESORIOS("Accesorios"),
    CONSEJOS("Consejos de presentación");

    private final String titulo;

    Seccion(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
