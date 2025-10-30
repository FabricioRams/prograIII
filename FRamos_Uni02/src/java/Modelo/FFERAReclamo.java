package modelo;

import java.sql.Timestamp;

/**
 * Clase JavaBean para Reclamos
 * @author FFERA
 */
public class FFERAReclamo {
    
    private int idReclamo;
    private String codigoReclamo;
    private int idUsuario;
    private String nombreUsuario; // Para mostrar en vistas
    private int idCategoria;
    private String nombreCategoria; // Para mostrar en vistas
    private int idEstado;
    private String nombreEstado; // Para mostrar en vistas
    private String asunto;
    private String descripcion;
    private Integer idAsignado;
    private String nombreAsignado; // Para mostrar en vistas
    private String prioridad;
    private Timestamp fechaRegistro;
    private Timestamp fechaActualizacion;
    private Timestamp fechaResolucion;
    
    // Constructores
    public FFERAReclamo() {
    }
    
    public FFERAReclamo(int idUsuario, int idCategoria, String asunto, String descripcion) {
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.prioridad = "Media";
    }
    
    public FFERAReclamo(String codigoReclamo, int idUsuario, int idCategoria, int idEstado,
                        String asunto, String descripcion, String prioridad) {
        this.codigoReclamo = codigoReclamo;
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.idEstado = idEstado;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
    }

    // Getters y Setters
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdAsignado() {
        return idAsignado;
    }

    public void setIdAsignado(Integer idAsignado) {
        this.idAsignado = idAsignado;
    }

    public String getNombreAsignado() {
        return nombreAsignado;
    }

    public void setNombreAsignado(String nombreAsignado) {
        this.nombreAsignado = nombreAsignado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Timestamp getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Timestamp fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    @Override
    public String toString() {
        return "FFERAReclamo{" +
                "idReclamo=" + idReclamo +
                ", codigoReclamo='" + codigoReclamo + '\'' +
                ", asunto='" + asunto + '\'' +
                ", nombreEstado='" + nombreEstado + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}