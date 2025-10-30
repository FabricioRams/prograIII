package modeloDAO;

import config.FFERAConexion;
import interfaces.IFFERAUsuarioDAO;
import modelo.FFERAUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para Usuarios
 * @author FFERA
 */
public class FFERAUsuarioDAO implements IFFERAUsuarioDAO {
    
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    // ========== AUTENTICACIÓN ==========
    
    @Override
    public FFERAUsuario validarUsuario(String username, String password) {
        FFERAUsuario usuario = null;
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ? AND u.password = ? AND u.estado = 1";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }
    
    @Override
    public FFERAUsuario validarUsuarioConIP(String username, String password, String ipCliente) {
        FFERAUsuario usuario = null;
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ? AND u.password = ? AND u.estado = 1 " +
                     "AND (u.ip_autorizada IS NULL OR u.ip_autorizada = ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, ipCliente);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario con IP: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }
    
    @Override
    public boolean actualizarUltimaConexion(int idUsuario) {
        String sql = "UPDATE fr_usuarios SET ultima_conexion = NOW() WHERE id_usuario = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar última conexión: " + e.getMessage());
            return false;
        }
    }
    
    // ========== CRUD ==========
    
    @Override
    public boolean registrarUsuario(FFERAUsuario usuario) {
        String sql = "INSERT INTO fr_usuarios (username, password, nombres, apellidos, " +
                     "email, telefono, id_rol, ip_autorizada, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getNombres());
            ps.setString(4, usuario.getApellidos());
            ps.setString(5, usuario.getEmail());
            ps.setString(6, usuario.getTelefono());
            ps.setInt(7, usuario.getIdRol());
            ps.setString(8, usuario.getIpAutorizada());
            ps.setBoolean(9, usuario.isEstado());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public FFERAUsuario obtenerUsuarioPorId(int idUsuario) {
        FFERAUsuario usuario = null;
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.id_usuario = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
        }
        return usuario;
    }
    
    @Override
    public FFERAUsuario obtenerUsuarioPorUsername(String username) {
        FFERAUsuario usuario = null;
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por username: " + e.getMessage());
        }
        return usuario;
    }
    
    @Override
    public List<FFERAUsuario> listarTodosUsuarios() {
        List<FFERAUsuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "ORDER BY u.fecha_registro DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERAUsuario> listarUsuariosPorRol(int idRol) {
        List<FFERAUsuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.id_rol = ? ORDER BY u.nombres";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idRol);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios por rol: " + e.getMessage());
        }
        return lista;
    }
    
    // Continúa en Parte 2...
    
    // ========== MÉTODO AUXILIAR ==========
    
    private FFERAUsuario mapearUsuario(ResultSet rs) throws SQLException {
        FFERAUsuario usuario = new FFERAUsuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setIdRol(rs.getInt("id_rol"));
        usuario.setNombreRol(rs.getString("nombre_rol"));
        usuario.setIpAutorizada(rs.getString("ip_autorizada"));
        usuario.setEstado(rs.getBoolean("estado"));
        usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        usuario.setUltimaConexion(rs.getTimestamp("ultima_conexion"));
        return usuario;
    }
    // CONTINUACIÓN DE FFERAUsuarioDAO.java
// Agregar estos métodos a la clase FFERAUsuarioDAO

    @Override
    public boolean actualizarUsuario(FFERAUsuario usuario) {
        String sql = "UPDATE fr_usuarios SET username = ?, nombres = ?, apellidos = ?, " +
                     "email = ?, telefono = ?, id_rol = ?, ip_autorizada = ?, estado = ? " +
                     "WHERE id_usuario = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getTelefono());
            ps.setInt(6, usuario.getIdRol());
            ps.setString(7, usuario.getIpAutorizada());
            ps.setBoolean(8, usuario.isEstado());
            ps.setInt(9, usuario.getIdUsuario());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM fr_usuarios WHERE id_usuario = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado) {
        String sql = "UPDATE fr_usuarios SET estado = ? WHERE id_usuario = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado usuario: " + e.getMessage());
            return false;
        }
    }
    
    // ========== VALIDACIONES ==========
    
    @Override
    public boolean existeUsername(String username) {
        String sql = "SELECT COUNT(*) FROM fr_usuarios WHERE username = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar username: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM fr_usuarios WHERE email = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
        }
        return false;
    }
    
    // ========== BÚSQUEDAS ==========
    
    @Override
    public List<FFERAUsuario> buscarUsuarios(String criterio) {
        List<FFERAUsuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nombre_rol FROM fr_usuarios u " +
                     "INNER JOIN fr_roles r ON u.id_rol = r.id_rol " +
                     "WHERE u.username LIKE ? OR u.nombres LIKE ? OR u.apellidos LIKE ? " +
                     "OR u.email LIKE ? ORDER BY u.nombres";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            String patron = "%" + criterio + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            ps.setString(4, patron);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuarios: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public int contarUsuarios() {
        String sql = "SELECT COUNT(*) FROM fr_usuarios";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar usuarios: " + e.getMessage());
        }
        return 0;
    }
    
    @Override
    public int contarUsuariosPorRol(int idRol) {
        String sql = "SELECT COUNT(*) FROM fr_usuarios WHERE id_rol = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idRol);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar usuarios por rol: " + e.getMessage());
        }
        return 0;
    }
}
