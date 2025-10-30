package controlador;

import modelo.FFERAUsuario;
import modeloDAO.FFERAUsuarioDAO;
import modeloDAO.FFERAGeneralDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet para gestión de Usuarios (CRUD)
 * @author FFERA
 */
@WebServlet(name = "FFERAUsuarioServlet", urlPatterns = {"/FFERAUsuarioServlet"})
public class FFERAUsuarioServlet extends HttpServlet {

    private FFERAUsuarioDAO usuarioDAO = new FFERAUsuarioDAO();
    private FFERAGeneralDAO generalDAO = new FFERAGeneralDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("FFERALoginServlet?accion=login");
            return;
        }
        
        // Solo administradores pueden gestionar usuarios
        int idRol = (Integer) session.getAttribute("idRol");
        if (idRol != 1) {
            response.sendRedirect("FFERALoginServlet?accion=login");
            return;
        }
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "listar";
        }
        
        switch (accion) {
            case "listar":
                listarUsuarios(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "registrar":
                registrarUsuario(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "actualizar":
                actualizarUsuario(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            case "cambiarEstado":
                cambiarEstado(request, response);
                break;
            case "buscar":
                buscarUsuarios(request, response);
                break;
            default:
                listarUsuarios(request, response);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<FFERAUsuario> usuarios = usuarioDAO.listarTodosUsuarios();
        
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("vistas/admin/listar_usuarios.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("roles", generalDAO.listarRoles());
        request.getRequestDispatcher("vistas/admin/nuevo_usuario.jsp").forward(request, response);
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Obtener datos del formulario
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            String ipAutorizada = request.getParameter("ipAutorizada");
            boolean estado = request.getParameter("estado") != null;
            
            // Validar campos obligatorios
            if (username == null || password == null || nombres == null || 
                apellidos == null || email == null ||
                username.trim().isEmpty() || password.trim().isEmpty() || 
                nombres.trim().isEmpty() || apellidos.trim().isEmpty() || 
                email.trim().isEmpty()) {
                
                request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/nuevo_usuario.jsp").forward(request, response);
                return;
            }
            
            // Validar username único
            if (usuarioDAO.existeUsername(username)) {
                request.setAttribute("error", "El username ya está en uso");
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/nuevo_usuario.jsp").forward(request, response);
                return;
            }
            
            // Validar email único
            if (usuarioDAO.existeEmail(email)) {
                request.setAttribute("error", "El email ya está registrado");
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/nuevo_usuario.jsp").forward(request, response);
                return;
            }
            
            // Crear usuario
            FFERAUsuario usuario = new FFERAUsuario();
            usuario.setUsername(username);
            usuario.setPassword(password); // En producción: hashear con BCrypt
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setIdRol(idRol);
            usuario.setIpAutorizada(ipAutorizada);
            usuario.setEstado(estado);
            
            // Registrar
            boolean registrado = usuarioDAO.registrarUsuario(usuario);
            
            if (registrado) {
                session.setAttribute("mensaje", "Usuario registrado exitosamente");
                response.sendRedirect("FFERAUsuarioServlet?accion=listar");
            } else {
                request.setAttribute("error", "Error al registrar el usuario");
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/nuevo_usuario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el formulario: " + e.getMessage());
            request.setAttribute("roles", generalDAO.listarRoles());
            request.getRequestDispatcher("vistas/admin/nuevo_usuario.jsp").forward(request, response);
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            
            FFERAUsuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
            
            if (usuario != null) {
                request.setAttribute("usuario", usuario);
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/editar_usuario.jsp").forward(request, response);
            } else {
                response.sendRedirect("FFERAUsuarioServlet?accion=listar");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAUsuarioServlet?accion=listar");
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String username = request.getParameter("username");
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            String ipAutorizada = request.getParameter("ipAutorizada");
            boolean estado = request.getParameter("estado") != null;
            
            // Validar campos obligatorios
            if (username == null || nombres == null || apellidos == null || email == null ||
                username.trim().isEmpty() || nombres.trim().isEmpty() || 
                apellidos.trim().isEmpty() || email.trim().isEmpty()) {
                
                request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
                FFERAUsuario usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
                request.setAttribute("usuario", usuario);
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/editar_usuario.jsp").forward(request, response);
                return;
            }
            
            // Crear usuario actualizado
            FFERAUsuario usuario = new FFERAUsuario();
            usuario.setIdUsuario(idUsuario);
            usuario.setUsername(username);
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setIdRol(idRol);
            usuario.setIpAutorizada(ipAutorizada);
            usuario.setEstado(estado);
            
            // Actualizar
            boolean actualizado = usuarioDAO.actualizarUsuario(usuario);
            
            if (actualizado) {
                session.setAttribute("mensaje", "Usuario actualizado exitosamente");
                response.sendRedirect("FFERAUsuarioServlet?accion=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el usuario");
                request.setAttribute("usuario", usuario);
                request.setAttribute("roles", generalDAO.listarRoles());
                request.getRequestDispatcher("vistas/admin/editar_usuario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAUsuarioServlet?accion=listar");
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            int idUsuarioActual = (Integer) session.getAttribute("idUsuario");
            
            // No permitir auto-eliminación
            if (idUsuario == idUsuarioActual) {
                session.setAttribute("error", "No puede eliminar su propio usuario");
                response.sendRedirect("FFERAUsuarioServlet?accion=listar");
                return;
            }
            
            boolean eliminado = usuarioDAO.eliminarUsuario(idUsuario);
            
            if (eliminado) {
                session.setAttribute("mensaje", "Usuario eliminado exitosamente");
            } else {
                session.setAttribute("error", "Error al eliminar el usuario");
            }
            
            response.sendRedirect("FFERAUsuarioServlet?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAUsuarioServlet?accion=listar");
        }
    }

    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("estado"));
            
            boolean cambiado = usuarioDAO.cambiarEstadoUsuario(idUsuario, nuevoEstado);
            
            if (cambiado) {
                session.setAttribute("mensaje", "Estado actualizado exitosamente");
            } else {
                session.setAttribute("error", "Error al cambiar el estado");
            }
            
            response.sendRedirect("FFERAUsuarioServlet?accion=listar");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAUsuarioServlet?accion=listar");
        }
    }

    private void buscarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String criterio = request.getParameter("criterio");
        
        List<FFERAUsuario> usuarios = usuarioDAO.buscarUsuarios(criterio);
        
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("criterio", criterio);
        request.getRequestDispatcher("vistas/admin/listar_usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}