package interfaces;

import modelo.FFERAUsuario;
import java.util.List;

/**
 * Interface para operaciones CRUD de Usuarios
 * @author FFERA
 */
public interface IFFERAUsuarioDAO {
    
    // Autenticación
    FFERAUsuario validarUsuario(String username, String password);
    FFERAUsuario validarUsuarioConIP(String username, String password, String ipCliente);
    boolean actualizarUltimaConexion(int idUsuario);
    
    // CRUD
    boolean registrarUsuario(FFERAUsuario usuario);
    FFERAUsuario obtenerUsuarioPorId(int idUsuario);
    FFERAUsuario obtenerUsuarioPorUsername(String username);
    List<FFERAUsuario> listarTodosUsuarios();
    List<FFERAUsuario> listarUsuariosPorRol(int idRol);
    boolean actualizarUsuario(FFERAUsuario usuario);
    boolean eliminarUsuario(int idUsuario);
    boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado);
    
    // Validaciones
    boolean existeUsername(String username);
    boolean existeEmail(String email);
    
    // Búsquedas
    List<FFERAUsuario> buscarUsuarios(String criterio);
    int contarUsuarios();
    int contarUsuariosPorRol(int idRol);
}