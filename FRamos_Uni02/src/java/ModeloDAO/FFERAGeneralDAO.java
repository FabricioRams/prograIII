package modeloDAO;

import config.FFERAConexion;
import interfaces.IFFERAGeneralDAO;
import modelo.FFERARol;
import modelo.FFERACategoria;
import modelo.FFERAEstado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para entidades auxiliares (Roles, Categorías, Estados)
 * @author FFERA
 */
public class FFERAGeneralDAO implements IFFERAGeneralDAO {
    
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    // ========== ROLES ==========
    
    @Override
    public List<FFERARol> listarRoles() {
        List<FFERARol> lista = new ArrayList<>();
        String sql = "SELECT * FROM fr_roles ORDER BY nombre_rol";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearRol(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar roles: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public FFERARol obtenerRolPorId(int idRol) {
        FFERARol rol = null;
        String sql = "SELECT * FROM fr_roles WHERE id_rol = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idRol);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                rol = mapearRol(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rol: " + e.getMessage());
        }
        return rol;
    }
    
    @Override
    public boolean registrarRol(FFERARol rol) {
        String sql = "INSERT INTO fr_roles (nombre_rol, descripcion, estado) VALUES (?, ?, ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rol.getNombreRol());
            ps.setString(2, rol.getDescripcion());
            ps.setBoolean(3, rol.isEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar rol: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean actualizarRol(FFERARol rol) {
        String sql = "UPDATE fr_roles SET nombre_rol = ?, descripcion = ?, estado = ? WHERE id_rol = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rol.getNombreRol());
            ps.setString(2, rol.getDescripcion());
            ps.setBoolean(3, rol.isEstado());
            ps.setInt(4, rol.getIdRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar rol: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminarRol(int idRol) {
        String sql = "DELETE FROM fr_roles WHERE id_rol = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idRol);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar rol: " + e.getMessage());
            return false;
        }
    }
    
    // ========== CATEGORÍAS ==========
    
    @Override
    public List<FFERACategoria> listarCategorias() {
        List<FFERACategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM fr_categorias ORDER BY nombre_categoria";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearCategoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERACategoria> listarCategoriasActivas() {
        List<FFERACategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM fr_categorias WHERE estado = 1 ORDER BY nombre_categoria";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearCategoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías activas: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public FFERACategoria obtenerCategoriaPorId(int idCategoria) {
        FFERACategoria categoria = null;
        String sql = "SELECT * FROM fr_categorias WHERE id_categoria = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                categoria = mapearCategoria(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener categoría: " + e.getMessage());
        }
        return categoria;
    }
    
    @Override
    public boolean registrarCategoria(FFERACategoria categoria) {
        String sql = "INSERT INTO fr_categorias (nombre_categoria, descripcion, estado) " +
                     "VALUES (?, ?, ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, categoria.getNombreCategoria());
            ps.setString(2, categoria.getDescripcion());
            ps.setBoolean(3, categoria.isEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar categoría: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean actualizarCategoria(FFERACategoria categoria) {
        String sql = "UPDATE fr_categorias SET nombre_categoria = ?, descripcion = ?, " +
                     "estado = ? WHERE id_categoria = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, categoria.getNombreCategoria());
            ps.setString(2, categoria.getDescripcion());
            ps.setBoolean(3, categoria.isEstado());
            ps.setInt(4, categoria.getIdCategoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminarCategoria(int idCategoria) {
        String sql = "DELETE FROM fr_categorias WHERE id_categoria = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean cambiarEstadoCategoria(int idCategoria, boolean nuevoEstado) {
        String sql = "UPDATE fr_categorias SET estado = ? WHERE id_categoria = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado categoría: " + e.getMessage());
            return false;
        }
    }
    
    // ========== ESTADOS ==========
    
    @Override
    public List<FFERAEstado> listarEstados() {
        List<FFERAEstado> lista = new ArrayList<>();
        String sql = "SELECT * FROM fr_estados ORDER BY orden_proceso";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearEstado(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar estados: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public FFERAEstado obtenerEstadoPorId(int idEstado) {
        FFERAEstado estado = null;
        String sql = "SELECT * FROM fr_estados WHERE id_estado = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEstado);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                estado = mapearEstado(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estado: " + e.getMessage());
        }
        return estado;
    }
    
    @Override
    public FFERAEstado obtenerEstadoPorNombre(String nombreEstado) {
        FFERAEstado estado = null;
        String sql = "SELECT * FROM fr_estados WHERE nombre_estado = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombreEstado);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                estado = mapearEstado(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estado por nombre: " + e.getMessage());
        }
        return estado;
    }
    
    @Override
    public boolean registrarEstado(FFERAEstado estado) {
        String sql = "INSERT INTO fr_estados (nombre_estado, descripcion, orden_proceso) " +
                     "VALUES (?, ?, ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado.getNombreEstado());
            ps.setString(2, estado.getDescripcion());
            ps.setInt(3, estado.getOrdenProceso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar estado: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean actualizarEstado(FFERAEstado estado) {
        String sql = "UPDATE fr_estados SET nombre_estado = ?, descripcion = ?, " +
                     "orden_proceso = ? WHERE id_estado = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado.getNombreEstado());
            ps.setString(2, estado.getDescripcion());
            ps.setInt(3, estado.getOrdenProceso());
            ps.setInt(4, estado.getIdEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminarEstado(int idEstado) {
        String sql = "DELETE FROM fr_estados WHERE id_estado = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEstado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar estado: " + e.getMessage());
            return false;
        }
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private FFERARol mapearRol(ResultSet rs) throws SQLException {
        FFERARol rol = new FFERARol();
        rol.setIdRol(rs.getInt("id_rol"));
        rol.setNombreRol(rs.getString("nombre_rol"));
        rol.setDescripcion(rs.getString("descripcion"));
        rol.setEstado(rs.getBoolean("estado"));
        rol.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return rol;
    }
    
    private FFERACategoria mapearCategoria(ResultSet rs) throws SQLException {
        FFERACategoria categoria = new FFERACategoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombreCategoria(rs.getString("nombre_categoria"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setEstado(rs.getBoolean("estado"));
        categoria.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return categoria;
    }
    
    private FFERAEstado mapearEstado(ResultSet rs) throws SQLException {
        FFERAEstado estado = new FFERAEstado();
        estado.setIdEstado(rs.getInt("id_estado"));
        estado.setNombreEstado(rs.getString("nombre_estado"));
        estado.setDescripcion(rs.getString("descripcion"));
        estado.setOrdenProceso(rs.getInt("orden_proceso"));
        return estado;
    }
}