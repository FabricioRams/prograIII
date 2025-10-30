package interfaces;

import modelo.FFERARol;
import modelo.FFERACategoria;
import modelo.FFERAEstado;
import java.util.List;

/**
 * Interface para operaciones CRUD de entidades auxiliares
 * @author FFERA
 */
public interface IFFERAGeneralDAO {
    
    // ========== ROLES ==========
    List<FFERARol> listarRoles();
    FFERARol obtenerRolPorId(int idRol);
    boolean registrarRol(FFERARol rol);
    boolean actualizarRol(FFERARol rol);
    boolean eliminarRol(int idRol);
    
    // ========== CATEGORÍAS ==========
    List<FFERACategoria> listarCategorias();
    List<FFERACategoria> listarCategoriasActivas();
    FFERACategoria obtenerCategoriaPorId(int idCategoria);
    boolean registrarCategoria(FFERACategoria categoria);
    boolean actualizarCategoria(FFERACategoria categoria);
    boolean eliminarCategoria(int idCategoria);
    boolean cambiarEstadoCategoria(int idCategoria, boolean nuevoEstado);
    
    // ========== ESTADOS ==========
    List<FFERAEstado> listarEstados();
    FFERAEstado obtenerEstadoPorId(int idEstado);
    FFERAEstado obtenerEstadoPorNombre(String nombreEstado);
    boolean registrarEstado(FFERAEstado estado);
    boolean actualizarEstado(FFERAEstado estado);
    boolean eliminarEstado(int idEstado);
}