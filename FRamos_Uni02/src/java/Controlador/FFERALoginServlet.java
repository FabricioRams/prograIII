package controlador;

import modelo.FFERAUsuario;
import modeloDAO.FFERAUsuarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet para Login y Control de Acceso con Validación de IP y Captcha
 * @author FFERA
 */
@WebServlet(name = "FFERALoginServlet", urlPatterns = {"/FFERALoginServlet"})
public class FFERALoginServlet extends HttpServlet {

    private FFERAUsuarioDAO usuarioDAO = new FFERAUsuarioDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "login";
        }
        
        switch (accion) {
            case "login":
                mostrarLogin(request, response);
                break;
            case "validar":
                validarLogin(request, response);
                break;
            case "logout":
                cerrarSesion(request, response);
                break;
            default:
                mostrarLogin(request, response);
        }
    }

    private void mostrarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("vistas/login.jsp").forward(request, response);
    }

    private void validarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captchaUsuario = request.getParameter("captcha");
        
        // Obtener IP del cliente
        String ipCliente = obtenerIPCliente(request);
        
        // Validar campos vacíos
        if (username == null || password == null || captchaUsuario == null ||
            username.trim().isEmpty() || password.trim().isEmpty() || captchaUsuario.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios");
            request.getRequestDispatcher("vistas/login.jsp").forward(request, response);
            return;
        }
        
        // Validar CAPTCHA
        HttpSession session = request.getSession();
        String captchaGenerado = (String) session.getAttribute("captcha");
        
        if (captchaGenerado == null || !captchaGenerado.equals(captchaUsuario)) {
            request.setAttribute("error", "CAPTCHA incorrecto");
            request.setAttribute("username", username);
            request.getRequestDispatcher("vistas/login.jsp").forward(request, response);
            return;
        }
        
        // Validar usuario con IP
        FFERAUsuario usuario = usuarioDAO.validarUsuarioConIP(username, password, ipCliente);
        
        if (usuario != null) {
            // Actualizar última conexión
            usuarioDAO.actualizarUltimaConexion(usuario.getIdUsuario());
            
            // Crear sesión
            session.setAttribute("usuario", usuario);
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("nombreUsuario", usuario.getNombreCompleto());
            session.setAttribute("rolUsuario", usuario.getNombreRol());
            session.setAttribute("idRol", usuario.getIdRol());
            session.setMaxInactiveInterval(1800); // 30 minutos
            
            // Redirigir según rol
            if (usuario.getIdRol() == 1) { // Administrador
                response.sendRedirect("FFERAAdminServlet?accion=dashboard");
            } else if (usuario.getIdRol() == 3) { // Soporte
                response.sendRedirect("FFERASoporteServlet?accion=listar");
            } else { // Usuario normal
                response.sendRedirect("FFERAReclamoServlet?accion=misreclamos");
            }
        } else {
            // Validar si existe el usuario pero con otra IP
            FFERAUsuario usuarioSinIP = usuarioDAO.validarUsuario(username, password);
            
            if (usuarioSinIP != null) {
                request.setAttribute("error", "Acceso denegado desde esta IP: " + ipCliente);
            } else {
                request.setAttribute("error", "Usuario o contraseña incorrectos");
            }
            
            request.setAttribute("username", username);
            request.getRequestDispatcher("vistas/login.jsp").forward(request, response);
        }
    }

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("FFERALoginServlet?accion=login");
    }

    private String obtenerIPCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
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