package co.stylovibes.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "caracteristicas")
public class Caracteristica {

    public enum Estado { PENDIENTE, EN_CURSO, COMPLETADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caracteristica")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(length = 40)
    private String tipoRostro;

    @Column(length = 300)
    private String facciones;

    @Column(length = 20)
    private String subtono;

    @Column(length = 20)
    private String estacion;

    @Column(length = 20)
    private String tipoCuerpo;

    @Column(length = 300)
    private String estilos;

    @Lob
    @Column(name = "respuestas_n1")
    private String respuestasN1;

    @Lob
    @Column(name = "respuestas_n2")
    private String respuestasN2;

    @Lob
    @Column(name = "respuestas_n3")
    private String respuestasN3;

    @Lob
    @Column(name = "respuestas_n4")
    private String respuestasN4;

    @Lob
    @Column(name = "respuestas_n5")
    private String respuestasN5;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    public boolean nivelCompletado(int nivel) {
        switch (nivel) {
            case 1: return tipoRostro != null;
            case 2: return facciones != null && !facciones.isEmpty();
            case 3: return subtono != null && estacion != null;
            case 4: return tipoCuerpo != null;
            case 5: return estilos != null && !estilos.isEmpty();
            default: return false;
        }
    }

    public int nivelActual() {
        for (int i = 1; i <= 5; i++) {
            if (!nivelCompletado(i)) {
                return i;
            }
        }
        return 5;
    }

    public boolean desbloqueado(int nivel) {
        if (nivel <= 1) {
            return true;
        }
        for (int i = 1; i < nivel; i++) {
            if (!nivelCompletado(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean todoCompletado() {
        for (int i = 1; i <= 5; i++) {
            if (!nivelCompletado(i)) {
                return false;
            }
        }
        return true;
    }

    public int porcentaje() {
        int completos = 0;
        for (int i = 1; i <= 5; i++) {
            if (nivelCompletado(i)) {
                completos++;
            }
        }
        return completos * 20;
    }

    public Estado estado(int nivel) {
        if (nivelCompletado(nivel)) {
            return Estado.COMPLETADO;
        }
        if (!desbloqueado(nivel)) {
            return Estado.PENDIENTE;
        }
        return Estado.EN_CURSO;
    }

    public String respuestasNivel(int nivel) {
        switch (nivel) {
            case 1: return respuestasN1;
            case 2: return respuestasN2;
            case 3: return respuestasN3;
            case 4: return respuestasN4;
            case 5: return respuestasN5;
            default: return null;
        }
    }

    public void guardarRespuestas(int nivel, String respuestas) {
        switch (nivel) {
            case 1: respuestasN1 = respuestas; break;
            case 2: respuestasN2 = respuestas; break;
            case 3: respuestasN3 = respuestas; break;
            case 4: respuestasN4 = respuestas; break;
            case 5: respuestasN5 = respuestas; break;
            default: break;
        }
        fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getTipoRostro() { return tipoRostro; }
    public void setTipoRostro(String tipoRostro) { this.tipoRostro = tipoRostro; }
    public String getFacciones() { return facciones; }
    public void setFacciones(String facciones) { this.facciones = facciones; }
    public String getSubtono() { return subtono; }
    public void setSubtono(String subtono) { this.subtono = subtono; }
    public String getEstacion() { return estacion; }
    public void setEstacion(String estacion) { this.estacion = estacion; }
    public String getTipoCuerpo() { return tipoCuerpo; }
    public void setTipoCuerpo(String tipoCuerpo) { this.tipoCuerpo = tipoCuerpo; }
    public String getEstilos() { return estilos; }
    public void setEstilos(String estilos) { this.estilos = estilos; }
    public String getRespuestasN1() { return respuestasN1; }
    public String getRespuestasN2() { return respuestasN2; }
    public String getRespuestasN3() { return respuestasN3; }
    public String getRespuestasN4() { return respuestasN4; }
    public String getRespuestasN5() { return respuestasN5; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
