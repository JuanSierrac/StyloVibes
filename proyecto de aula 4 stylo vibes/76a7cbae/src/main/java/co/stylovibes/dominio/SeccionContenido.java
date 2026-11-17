package co.stylovibes.dominio;

public enum SeccionContenido {
    INICIO("Inicio"),
    NIVELES("Niveles del diagnóstico"),
    CATALOGO("Catálogo"),
    CONSEJOS("Consejos");

    private final String titulo;

    SeccionContenido(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
