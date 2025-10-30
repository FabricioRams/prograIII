package interfaces;

import modelo.FFERASeguimiento;
import java.util.List;

/**
 * Interface para operaciones CRUD de Seguimiento
 * @author FFERA
 */
public interface IFFERASeguimientoDAO {
    
    // CRUD
    boolean registrarSeguimiento(FFERASeguimiento seguimiento);
    FFERASeguimiento obtenerSeguimientoPorId(int idSeguimiento);
    List<FFERASeguimiento> listarTodosSeguimientos();
    
    // Listados específicos
    List<FFERASeguimiento> listarSeguimientosPorReclamo(int idReclamo);
    List<FFERASeguimiento> listarSeguimientosPorUsuario(int idUsuario);
    FFERASeguimiento obtenerUltimoSeguimientoReclamo(int idReclamo);
    
    // Registro automático con cambio de estado
    boolean registrarSeguimientoConCambioEstado(int idReclamo, int idUsuario, 
                                                 int nuevoEstado, String observaciones, 
                                                 String accionRealizada);
    
    // Consultas
    int contarSeguimientosPorReclamo(int idReclamo);
}