package modeloDAO;

import config.FFERAConexion;
import interfaces.IFFERAReclamoDAO;
import modelo.FFERAReclamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación DAO para Reclamos
 * @author FFERA
 */
public class FFERAReclamoDAO implements IFFERAReclamoDAO {
    
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    // ========== CRUD ==========
    
    @Override
    public boolean registrarReclamo(FFERAReclamo reclamo) {
        String sql = "INSERT INTO fr_reclamos (codigo_reclamo, id_usuario, id_categoria, " +
                     "id_estado, asunto, descripcion, prioridad) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, reclamo.getCodigoReclamo());
            ps.setInt(2, reclamo.getIdUsuario());
            ps.setInt(3, reclamo.getIdCategoria());
            ps.setInt(4, reclamo.getIdEstado());
            ps.setString(5, reclamo.getAsunto());
            ps.setString(6, reclamo.getDescripcion());
            ps.setString(7, reclamo.getPrioridad());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar reclamo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public FFERAReclamo obtenerReclamoPorId(int idReclamo) {
        FFERAReclamo reclamo = null;
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReclamo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                reclamo = mapearReclamo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reclamo por ID: " + e.getMessage());
        }
        return reclamo;
    }
    
    @Override
    public FFERAReclamo obtenerReclamoPorCodigo(String codigoReclamo) {
        FFERAReclamo reclamo = null;
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.codigo_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, codigoReclamo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                reclamo = mapearReclamo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reclamo por código: " + e.getMessage());
        }
        return reclamo;
    }
    
    @Override
    public List<FFERAReclamo> listarTodosReclamos() {
        return ejecutarConsultaLista(
            "SELECT r.*, " +
            "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
            "c.nombre_categoria, e.nombre_estado, " +
            "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
            "FROM fr_reclamos r " +
            "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
            "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
            "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
            "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
            "ORDER BY r.fecha_registro DESC"
        );
    }
    
    @Override
    public boolean actualizarReclamo(FFERAReclamo reclamo) {
        String sql = "UPDATE fr_reclamos SET id_categoria = ?, asunto = ?, " +
                     "descripcion = ?, prioridad = ? WHERE id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, reclamo.getIdCategoria());
            ps.setString(2, reclamo.getAsunto());
            ps.setString(3, reclamo.getDescripcion());
            ps.setString(4, reclamo.getPrioridad());
            ps.setInt(5, reclamo.getIdReclamo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar reclamo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminarReclamo(int idReclamo) {
        String sql = "DELETE FROM fr_reclamos WHERE id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReclamo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reclamo: " + e.getMessage());
            return false;
        }
    }
    
    // ========== LISTADOS ESPECÍFICOS ==========
    
    @Override
    public List<FFERAReclamo> listarReclamosPorUsuario(int idUsuario) {
        List<FFERAReclamo> lista = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.id_usuario = ? ORDER BY r.fecha_registro DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos por usuario: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERAReclamo> listarReclamosPorEstado(int idEstado) {
        List<FFERAReclamo> lista = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.id_estado = ? ORDER BY r.fecha_registro DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEstado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos por estado: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERAReclamo> listarReclamosPorCategoria(int idCategoria) {
        List<FFERAReclamo> lista = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.id_categoria = ? ORDER BY r.fecha_registro DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos por categoría: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERAReclamo> listarReclamosPorAsignado(int idAsignado) {
        List<FFERAReclamo> lista = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.id_asignado = ? ORDER BY r.fecha_registro DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idAsignado);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos por asignado: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERAReclamo> listarReclamosPendientes() {
        return listarReclamosPorEstado(1); // Estado: Pendiente
    }
    
    @Override
    public List<FFERAReclamo> listarReclamosEnAtencion() {
        return listarReclamosPorEstado(2); // Estado: En Atención
    }
    
    @Override
    public List<FFERAReclamo> listarReclamosResueltos() {
        return listarReclamosPorEstado(3); // Estado: Resuelto
    }
    
    // CONTINUACIÓN DE FFERAReclamoDAO.java
// Agregar estos métodos a la clase FFERAReclamoDAO

    // ========== OPERACIONES DE GESTIÓN ==========
    
    @Override
    public boolean asignarReclamo(int idReclamo, int idAsignado) {
        String sql = "UPDATE fr_reclamos SET id_asignado = ? WHERE id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idAsignado);
            ps.setInt(2, idReclamo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al asignar reclamo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean cambiarEstadoReclamo(int idReclamo, int nuevoEstado) {
        String sql = "UPDATE fr_reclamos SET id_estado = ? WHERE id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nuevoEstado);
            ps.setInt(2, idReclamo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean cambiarPrioridadReclamo(int idReclamo, String nuevaPrioridad) {
        String sql = "UPDATE fr_reclamos SET prioridad = ? WHERE id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nuevaPrioridad);
            ps.setInt(2, idReclamo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar prioridad: " + e.getMessage());
            return false;
        }
    }
    
    // ========== GENERACIÓN DE CÓDIGOS ==========
    
    @Override
    public String generarCodigoReclamo() {
        String codigo = null;
        String sql = "SELECT CONCAT('RCL-', YEAR(CURDATE()), '-', " +
                     "LPAD(COUNT(*) + 1, 3, '0')) as codigo " +
                     "FROM fr_reclamos WHERE YEAR(fecha_registro) = YEAR(CURDATE())";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getString("codigo");
            }
        } catch (SQLException e) {
            System.err.println("Error al generar código: " + e.getMessage());
        }
        return codigo;
    }
    
    // ========== BÚSQUEDAS Y FILTROS ==========
    
    @Override
    public List<FFERAReclamo> buscarReclamos(String criterio) {
        List<FFERAReclamo> lista = new ArrayList<>();
        String sql = "SELECT r.*, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "c.nombre_categoria, e.nombre_estado, " +
                     "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
                     "FROM fr_reclamos r " +
                     "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
                     "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
                     "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
                     "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario " +
                     "WHERE r.codigo_reclamo LIKE ? OR r.asunto LIKE ? OR r.descripcion LIKE ? " +
                     "ORDER BY r.fecha_registro DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            String patron = "%" + criterio + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reclamos: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERAReclamo> filtrarReclamos(Integer idEstado, Integer idCategoria, String prioridad) {
        List<FFERAReclamo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, " +
            "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
            "c.nombre_categoria, e.nombre_estado, " +
            "CONCAT(ua.nombres, ' ', ua.apellidos) as nombre_asignado " +
            "FROM fr_reclamos r " +
            "INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario " +
            "INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria " +
            "INNER JOIN fr_estados e ON r.id_estado = e.id_estado " +
            "LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario WHERE 1=1 "
        );
        
        if (idEstado != null) sql.append("AND r.id_estado = ? ");
        if (idCategoria != null) sql.append("AND r.id_categoria = ? ");
        if (prioridad != null) sql.append("AND r.prioridad = ? ");
        sql.append("ORDER BY r.fecha_registro DESC");
        
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql.toString());
            
            int index = 1;
            if (idEstado != null) ps.setInt(index++, idEstado);
            if (idCategoria != null) ps.setInt(index++, idCategoria);
            if (prioridad != null) ps.setString(index++, prioridad);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar reclamos: " + e.getMessage());
        }
        return lista;
    }
    
    // ========== ESTADÍSTICAS ==========
    
    @Override
    public int contarReclamosPorUsuario(int idUsuario) {
        return ejecutarConteo("SELECT COUNT(*) FROM fr_reclamos WHERE id_usuario = ?", idUsuario);
    }
    
    @Override
    public int contarReclamosPorEstado(int idEstado) {
        return ejecutarConteo("SELECT COUNT(*) FROM fr_reclamos WHERE id_estado = ?", idEstado);
    }
    
    @Override
    public int contarReclamosPorCategoria(int idCategoria) {
        return ejecutarConteo("SELECT COUNT(*) FROM fr_reclamos WHERE id_categoria = ?", idCategoria);
    }
    
    @Override
    public Map<String, Integer> obtenerEstadisticasPorEstado() {
        Map<String, Integer> estadisticas = new HashMap<>();
        String sql = "SELECT e.nombre_estado, COUNT(r.id_reclamo) as total " +
                     "FROM fr_estados e " +
                     "LEFT JOIN fr_reclamos r ON e.id_estado = r.id_estado " +
                     "GROUP BY e.id_estado, e.nombre_estado " +
                     "ORDER BY e.orden_proceso";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                estadisticas.put(rs.getString("nombre_estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas por estado: " + e.getMessage());
        }
        return estadisticas;
    }
    
    @Override
    public Map<String, Integer> obtenerEstadisticasPorCategoria() {
        Map<String, Integer> estadisticas = new HashMap<>();
        String sql = "SELECT c.nombre_categoria, COUNT(r.id_reclamo) as total " +
                     "FROM fr_categorias c " +
                     "LEFT JOIN fr_reclamos r ON c.id_categoria = r.id_categoria " +
                     "WHERE c.estado = 1 " +
                     "GROUP BY c.id_categoria, c.nombre_categoria " +
                     "ORDER BY total DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                estadisticas.put(rs.getString("nombre_categoria"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas por categoría: " + e.getMessage());
        }
        return estadisticas;
    }
    
    @Override
    public Map<String, Integer> obtenerEstadisticasPorPrioridad() {
        Map<String, Integer> estadisticas = new HashMap<>();
        String sql = "SELECT prioridad, COUNT(*) as total " +
                     "FROM fr_reclamos " +
                     "GROUP BY prioridad " +
                     "ORDER BY FIELD(prioridad, 'Urgente', 'Alta', 'Media', 'Baja')";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                estadisticas.put(rs.getString("prioridad"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas por prioridad: " + e.getMessage());
        }
        return estadisticas;
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private FFERAReclamo mapearReclamo(ResultSet rs) throws SQLException {
        FFERAReclamo reclamo = new FFERAReclamo();
        reclamo.setIdReclamo(rs.getInt("id_reclamo"));
        reclamo.setCodigoReclamo(rs.getString("codigo_reclamo"));
        reclamo.setIdUsuario(rs.getInt("id_usuario"));
        reclamo.setNombreUsuario(rs.getString("nombre_usuario"));
        reclamo.setIdCategoria(rs.getInt("id_categoria"));
        reclamo.setNombreCategoria(rs.getString("nombre_categoria"));
        reclamo.setIdEstado(rs.getInt("id_estado"));
        reclamo.setNombreEstado(rs.getString("nombre_estado"));
        reclamo.setAsunto(rs.getString("asunto"));
        reclamo.setDescripcion(rs.getString("descripcion"));
        
        int idAsignado = rs.getInt("id_asignado");
        reclamo.setIdAsignado(rs.wasNull() ? null : idAsignado);
        reclamo.setNombreAsignado(rs.getString("nombre_asignado"));
        
        reclamo.setPrioridad(rs.getString("prioridad"));
        reclamo.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        reclamo.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        reclamo.setFechaResolucion(rs.getTimestamp("fecha_resolucion"));
        return reclamo;
    }
    
    private List<FFERAReclamo> ejecutarConsultaLista(String sql) {
        List<FFERAReclamo> lista = new ArrayList<>();
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en consulta: " + e.getMessage());
        }
        return lista;
    }
    
    private int ejecutarConteo(String sql, int parametro) {
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, parametro);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar: " + e.getMessage());
        }
        return 0;
    }
}