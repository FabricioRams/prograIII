package modelo;

import java.sql.Timestamp;

/**
 * Clase JavaBean para Usuarios
 * @author FFERA
 */
public class FFERAUsuario {
    
    private int idUsuario;
    private String username;
    private String password;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private int idRol;
    private String nombreRol; // Para mostrar en vistas
    private String ipAutorizada;
    private boolean estado;
    private Timestamp fechaRegistro;
    private Timestamp ultimaConexion;
    
    // Constructores
    public FFERAUsuario() {
    }
    
    public FFERAUsuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public FFERAUsuario(int idUsuario, String username, String nombres, String apellidos, 
                        String email, int idRol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.idRol = idRol;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getIpAutorizada() {
        return ipAutorizada;
    }

    public void setIpAutorizada(String ipAutorizada) {
        this.ipAutorizada = ipAutorizada;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Timestamp getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(Timestamp ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }
    
    // Método auxiliar para nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return "FFERAUsuario{" +
                "idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", idRol=" + idRol +
                ", nombreRol='" + nombreRol + '\'' +
                '}';
    }
}