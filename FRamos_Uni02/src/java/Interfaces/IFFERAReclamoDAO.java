package interfaces;

import modelo.FFERAReclamo;
import java.util.List;
import java.util.Map;

/**
 * Interface para operaciones CRUD de Reclamos
 * @author FFERA
 */
public interface IFFERAReclamoDAO {
    
    // CRUD
    boolean registrarReclamo(FFERAReclamo reclamo);
    FFERAReclamo obtenerReclamoPorId(int idReclamo);
    FFERAReclamo obtenerReclamoPorCodigo(String codigoReclamo);
    List<FFERAReclamo> listarTodosReclamos();
    boolean actualizarReclamo(FFERAReclamo reclamo);
    boolean eliminarReclamo(int idReclamo);
    
    // Listados específicos
    List<FFERAReclamo> listarReclamosPorUsuario(int idUsuario);
    List<FFERAReclamo> listarReclamosPorEstado(int idEstado);
    List<FFERAReclamo> listarReclamosPorCategoria(int idCategoria);
    List<FFERAReclamo> listarReclamosPorAsignado(int idAsignado);
    List<FFERAReclamo> listarReclamosPendientes();
    List<FFERAReclamo> listarReclamosEnAtencion();
    List<FFERAReclamo> listarReclamosResueltos();
    
    // Operaciones de gestión
    boolean asignarReclamo(int idReclamo, int idAsignado);
    boolean cambiarEstadoReclamo(int idReclamo, int nuevoEstado);
    boolean cambiarPrioridadReclamo(int idReclamo, String nuevaPrioridad);
    
    // Generación de códigos
    String generarCodigoReclamo();
    
    // Búsquedas y filtros
    List<FFERAReclamo> buscarReclamos(String criterio);
    List<FFERAReclamo> filtrarReclamos(Integer idEstado, Integer idCategoria, String prioridad);
    
    // Estadísticas
    int contarReclamosPorUsuario(int idUsuario);
    int contarReclamosPorEstado(int idEstado);
    int contarReclamosPorCategoria(int idCategoria);
    Map<String, Integer> obtenerEstadisticasPorEstado();
    Map<String, Integer> obtenerEstadisticasPorCategoria();
    Map<String, Integer> obtenerEstadisticasPorPrioridad();
}