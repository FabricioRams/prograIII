package modelo;

/**
 * Clase JavaBean para Estados de Reclamos
 * @author FFERA
 */
public class FFERAEstado {
    
    private int idEstado;
    private String nombreEstado;
    private String descripcion;
    private int ordenProceso;
    
    // Constructores
    public FFERAEstado() {
    }
    
    public FFERAEstado(int idEstado, String nombreEstado) {
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
    }
    
    public FFERAEstado(int idEstado, String nombreEstado, String descripcion, int ordenProceso) {
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
        this.descripcion = descripcion;
        this.ordenProceso = ordenProceso;
    }

    // Getters y Setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getOrdenProceso() {
        return ordenProceso;
    }

    public void setOrdenProceso(int ordenProceso) {
        this.ordenProceso = ordenProceso;
    }

    @Override
    public String toString() {
        return "FFERAEstado{" +
                "idEstado=" + idEstado +
                ", nombreEstado='" + nombreEstado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ordenProceso=" + ordenProceso +
                '}';
    }
}