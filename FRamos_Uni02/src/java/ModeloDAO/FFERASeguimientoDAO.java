package modeloDAO;

import config.FFERAConexion;
import interfaces.IFFERASeguimientoDAO;
import modelo.FFERASeguimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para Seguimiento
 * @author FFERA
 */
public class FFERASeguimientoDAO implements IFFERASeguimientoDAO {
    
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    
    @Override
    public boolean registrarSeguimiento(FFERASeguimiento seguimiento) {
        String sql = "INSERT INTO fr_seguimiento (id_reclamo, id_usuario, id_estado_anterior, " +
                     "id_estado_nuevo, observaciones, accion_realizada) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, seguimiento.getIdReclamo());
            ps.setInt(2, seguimiento.getIdUsuario());
            
            if (seguimiento.getIdEstadoAnterior() != null) {
                ps.setInt(3, seguimiento.getIdEstadoAnterior());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setInt(4, seguimiento.getIdEstadoNuevo());
            ps.setString(5, seguimiento.getObservaciones());
            ps.setString(6, seguimiento.getAccionRealizada());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar seguimiento: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public FFERASeguimiento obtenerSeguimientoPorId(int idSeguimiento) {
        FFERASeguimiento seguimiento = null;
        String sql = "SELECT s.*, r.codigo_reclamo, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "ea.nombre_estado as nombre_estado_anterior, " +
                     "en.nombre_estado as nombre_estado_nuevo " +
                     "FROM fr_seguimiento s " +
                     "INNER JOIN fr_reclamos r ON s.id_reclamo = r.id_reclamo " +
                     "INNER JOIN fr_usuarios u ON s.id_usuario = u.id_usuario " +
                     "LEFT JOIN fr_estados ea ON s.id_estado_anterior = ea.id_estado " +
                     "INNER JOIN fr_estados en ON s.id_estado_nuevo = en.id_estado " +
                     "WHERE s.id_seguimiento = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idSeguimiento);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                seguimiento = mapearSeguimiento(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener seguimiento: " + e.getMessage());
        }
        return seguimiento;
    }
    
    @Override
    public List<FFERASeguimiento> listarTodosSeguimientos() {
        return ejecutarConsultaLista(
            "SELECT s.*, r.codigo_reclamo, " +
            "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
            "ea.nombre_estado as nombre_estado_anterior, " +
            "en.nombre_estado as nombre_estado_nuevo " +
            "FROM fr_seguimiento s " +
            "INNER JOIN fr_reclamos r ON s.id_reclamo = r.id_reclamo " +
            "INNER JOIN fr_usuarios u ON s.id_usuario = u.id_usuario " +
            "LEFT JOIN fr_estados ea ON s.id_estado_anterior = ea.id_estado " +
            "INNER JOIN fr_estados en ON s.id_estado_nuevo = en.id_estado " +
            "ORDER BY s.fecha_seguimiento DESC"
        );
    }
    
    @Override
    public List<FFERASeguimiento> listarSeguimientosPorReclamo(int idReclamo) {
        List<FFERASeguimiento> lista = new ArrayList<>();
        String sql = "SELECT s.*, r.codigo_reclamo, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "ea.nombre_estado as nombre_estado_anterior, " +
                     "en.nombre_estado as nombre_estado_nuevo " +
                     "FROM fr_seguimiento s " +
                     "INNER JOIN fr_reclamos r ON s.id_reclamo = r.id_reclamo " +
                     "INNER JOIN fr_usuarios u ON s.id_usuario = u.id_usuario " +
                     "LEFT JOIN fr_estados ea ON s.id_estado_anterior = ea.id_estado " +
                     "INNER JOIN fr_estados en ON s.id_estado_nuevo = en.id_estado " +
                     "WHERE s.id_reclamo = ? ORDER BY s.fecha_seguimiento ASC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReclamo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearSeguimiento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar seguimientos por reclamo: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public List<FFERASeguimiento> listarSeguimientosPorUsuario(int idUsuario) {
        List<FFERASeguimiento> lista = new ArrayList<>();
        String sql = "SELECT s.*, r.codigo_reclamo, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "ea.nombre_estado as nombre_estado_anterior, " +
                     "en.nombre_estado as nombre_estado_nuevo " +
                     "FROM fr_seguimiento s " +
                     "INNER JOIN fr_reclamos r ON s.id_reclamo = r.id_reclamo " +
                     "INNER JOIN fr_usuarios u ON s.id_usuario = u.id_usuario " +
                     "LEFT JOIN fr_estados ea ON s.id_estado_anterior = ea.id_estado " +
                     "INNER JOIN fr_estados en ON s.id_estado_nuevo = en.id_estado " +
                     "WHERE s.id_usuario = ? ORDER BY s.fecha_seguimiento DESC";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearSeguimiento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar seguimientos por usuario: " + e.getMessage());
        }
        return lista;
    }
    
    @Override
    public FFERASeguimiento obtenerUltimoSeguimientoReclamo(int idReclamo) {
        FFERASeguimiento seguimiento = null;
        String sql = "SELECT s.*, r.codigo_reclamo, " +
                     "CONCAT(u.nombres, ' ', u.apellidos) as nombre_usuario, " +
                     "ea.nombre_estado as nombre_estado_anterior, " +
                     "en.nombre_estado as nombre_estado_nuevo " +
                     "FROM fr_seguimiento s " +
                     "INNER JOIN fr_reclamos r ON s.id_reclamo = r.id_reclamo " +
                     "INNER JOIN fr_usuarios u ON s.id_usuario = u.id_usuario " +
                     "LEFT JOIN fr_estados ea ON s.id_estado_anterior = ea.id_estado " +
                     "INNER JOIN fr_estados en ON s.id_estado_nuevo = en.id_estado " +
                     "WHERE s.id_reclamo = ? ORDER BY s.fecha_seguimiento DESC LIMIT 1";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReclamo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                seguimiento = mapearSeguimiento(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener último seguimiento: " + e.getMessage());
        }
        return seguimiento;
    }
    
    @Override
    public boolean registrarSeguimientoConCambioEstado(int idReclamo, int idUsuario,
                                                        int nuevoEstado, String observaciones,
                                                        String accionRealizada) {
        Connection conn = null;
        try {
            conn = FFERAConexion.getConexion();
            conn.setAutoCommit(false);
            
            // Obtener estado actual
            String sqlEstadoActual = "SELECT id_estado FROM fr_reclamos WHERE id_reclamo = ?";
            PreparedStatement psEstado = conn.prepareStatement(sqlEstadoActual);
            psEstado.setInt(1, idReclamo);
            ResultSet rsEstado = psEstado.executeQuery();
            
            Integer estadoAnterior = null;
            if (rsEstado.next()) {
                estadoAnterior = rsEstado.getInt("id_estado");
            }
            
            // Registrar seguimiento
            String sqlSeguimiento = "INSERT INTO fr_seguimiento (id_reclamo, id_usuario, " +
                                   "id_estado_anterior, id_estado_nuevo, observaciones, accion_realizada) " +
                                   "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psSeguimiento = conn.prepareStatement(sqlSeguimiento);
            psSeguimiento.setInt(1, idReclamo);
            psSeguimiento.setInt(2, idUsuario);
            if (estadoAnterior != null) {
                psSeguimiento.setInt(3, estadoAnterior);
            } else {
                psSeguimiento.setNull(3, Types.INTEGER);
            }
            psSeguimiento.setInt(4, nuevoEstado);
            psSeguimiento.setString(5, observaciones);
            psSeguimiento.setString(6, accionRealizada);
            psSeguimiento.executeUpdate();
            
            // Actualizar estado del reclamo
            String sqlActualizar = "UPDATE fr_reclamos SET id_estado = ? WHERE id_reclamo = ?";
            PreparedStatement psActualizar = conn.prepareStatement(sqlActualizar);
            psActualizar.setInt(1, nuevoEstado);
            psActualizar.setInt(2, idReclamo);
            psActualizar.executeUpdate();
            
            // Si el estado es "Resuelto" (3), actualizar fecha_resolucion
            if (nuevoEstado == 3) {
                String sqlResolucion = "UPDATE fr_reclamos SET fecha_resolucion = NOW() WHERE id_reclamo = ?";
                PreparedStatement psResolucion = conn.prepareStatement(sqlResolucion);
                psResolucion.setInt(1, idReclamo);
                psResolucion.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error en transacción de seguimiento: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public int contarSeguimientosPorReclamo(int idReclamo) {
        String sql = "SELECT COUNT(*) FROM fr_seguimiento WHERE id_reclamo = ?";
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReclamo);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar seguimientos: " + e.getMessage());
        }
        return 0;
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private FFERASeguimiento mapearSeguimiento(ResultSet rs) throws SQLException {
        FFERASeguimiento seguimiento = new FFERASeguimiento();
        seguimiento.setIdSeguimiento(rs.getInt("id_seguimiento"));
        seguimiento.setIdReclamo(rs.getInt("id_reclamo"));
        seguimiento.setCodigoReclamo(rs.getString("codigo_reclamo"));
        seguimiento.setIdUsuario(rs.getInt("id_usuario"));
        seguimiento.setNombreUsuario(rs.getString("nombre_usuario"));
        
        int idEstadoAnterior = rs.getInt("id_estado_anterior");
        seguimiento.setIdEstadoAnterior(rs.wasNull() ? null : idEstadoAnterior);
        seguimiento.setNombreEstadoAnterior(rs.getString("nombre_estado_anterior"));
        
        seguimiento.setIdEstadoNuevo(rs.getInt("id_estado_nuevo"));
        seguimiento.setNombreEstadoNuevo(rs.getString("nombre_estado_nuevo"));
        seguimiento.setObservaciones(rs.getString("observaciones"));
        seguimiento.setAccionRealizada(rs.getString("accion_realizada"));
        seguimiento.setFechaSeguimiento(rs.getTimestamp("fecha_seguimiento"));
        return seguimiento;
    }
    
    private List<FFERASeguimiento> ejecutarConsultaLista(String sql) {
        List<FFERASeguimiento> lista = new ArrayList<>();
        try {
            conn = FFERAConexion.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearSeguimiento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en consulta: " + e.getMessage());
        }
        return lista;
    }
}