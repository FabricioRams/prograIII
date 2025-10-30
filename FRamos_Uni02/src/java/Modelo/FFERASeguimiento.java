package modelo;

import java.sql.Timestamp;

/**
 * Clase JavaBean para Seguimiento de Reclamos
 * @author FFERA
 */
public class FFERASeguimiento {
    
    private int idSeguimiento;
    private int idReclamo;
    private String codigoReclamo; // Para mostrar en vistas
    private int idUsuario;
    private String nombreUsuario; // Para mostrar en vistas
    private Integer idEstadoAnterior;
    private String nombreEstadoAnterior; // Para mostrar en vistas
    private int idEstadoNuevo;
    private String nombreEstadoNuevo; // Para mostrar en vistas
    private String observaciones;
    private String accionRealizada;
    private Timestamp fechaSeguimiento;
    
    // Constructores
    public FFERASeguimiento() {
    }
    
    public FFERASeguimiento(int idReclamo, int idUsuario, int idEstadoNuevo, 
                           String observaciones, String accionRealizada) {
        this.idReclamo = idReclamo;
        this.idUsuario = idUsuario;
        this.idEstadoNuevo = idEstadoNuevo;
        this.observaciones = observaciones;
        this.accionRealizada = accionRealizada;
    }
    
    public FFERASeguimiento(int idReclamo, int idUsuario, Integer idEstadoAnterior,
                           int idEstadoNuevo, String observaciones, String accionRealizada) {
        this.idReclamo = idReclamo;
        this.idUsuario = idUsuario;
        this.idEstadoAnterior = idEstadoAnterior;
        this.idEstadoNuevo = idEstadoNuevo;
        this.observaciones = observaciones;
        this.accionRealizada = accionRealizada;
    }

    // Getters y Setters
    public int getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(int idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public int getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(int idReclamo) {
        this.idReclamo = idReclamo;
    }

    public String getCodigoReclamo() {
        return codigoReclamo;
    }

    public void setCodigoReclamo(String codigoReclamo) {
        this.codigoReclamo = codigoReclamo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getIdEstadoAnterior() {
        return idEstadoAnterior;
    }

    public void setIdEstadoAnterior(Integer idEstadoAnterior) {
        this.idEstadoAnterior = idEstadoAnterior;
    }

    public String getNombreEstadoAnterior() {
        return nombreEstadoAnterior;
    }

    public void setNombreEstadoAnterior(String nombreEstadoAnterior) {
        this.nombreEstadoAnterior = nombreEstadoAnterior;
    }

    public int getIdEstadoNuevo() {
        return idEstadoNuevo;
    }

    public void setIdEstadoNuevo(int idEstadoNuevo) {
        this.idEstadoNuevo = idEstadoNuevo;
    }

    public String getNombreEstadoNuevo() {
        return nombreEstadoNuevo;
    }

    public void setNombreEstadoNuevo(String nombreEstadoNuevo) {
        this.nombreEstadoNuevo = nombreEstadoNuevo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getAccionRealizada() {
        return accionRealizada;
    }

    public void setAccionRealizada(String accionRealizada) {
        this.accionRealizada = accionRealizada;
    }

    public Timestamp getFechaSeguimiento() {
        return fechaSeguimiento;
    }

    public void setFechaSeguimiento(Timestamp fechaSeguimiento) {
        this.fechaSeguimiento = fechaSeguimiento;
    }

    @Override
    public String toString() {
        return "FFERASeguimiento{" +
                "idSeguimiento=" + idSeguimiento +
                ", idReclamo=" + idReclamo +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", nombreEstadoNuevo='" + nombreEstadoNuevo + '\'' +
                ", accionRealizada='" + accionRealizada + '\'' +
                ", fechaSeguimiento=" + fechaSeguimiento +
                '}';
    }
}