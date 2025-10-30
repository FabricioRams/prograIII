package controlador;

import modelo.FFERAReclamo;
import modeloDAO.FFERAReclamoDAO;
import modeloDAO.FFERAGeneralDAO;
import modeloDAO.FFERASeguimientoDAO;
import modelo.FFERASeguimiento;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet para gestión de Reclamos (Usuario)
 * @author FFERA
 */
@WebServlet(name = "FFERAReclamoServlet", urlPatterns = {"/FFERAReclamoServlet"})
public class FFERAReclamoServlet extends HttpServlet {

    private FFERAReclamoDAO reclamoDAO = new FFERAReclamoDAO();
    private FFERAGeneralDAO generalDAO = new FFERAGeneralDAO();
    private FFERASeguimientoDAO seguimientoDAO = new FFERASeguimientoDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("FFERALoginServlet?accion=login");
            return;
        }
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "misreclamos";
        }
        
        switch (accion) {
            case "misreclamos":
                listarMisReclamos(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "registrar":
                registrarReclamo(request, response);
                break;
            case "detalle":
                verDetalle(request, response);
                break;
            case "seguimiento":
                verSeguimiento(request, response);
                break;
            default:
                listarMisReclamos(request, response);
        }
    }

    private void listarMisReclamos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        
        List<FFERAReclamo> misReclamos = reclamoDAO.listarReclamosPorUsuario(idUsuario);
        
        // Obtener estadísticas personales
        int totalReclamos = misReclamos.size();
        int pendientes = (int) misReclamos.stream()
                .filter(r -> r.getIdEstado() == 1).count();
        int enAtencion = (int) misReclamos.stream()
                .filter(r -> r.getIdEstado() == 2).count();
        int resueltos = (int) misReclamos.stream()
                .filter(r -> r.getIdEstado() == 3).count();
        
        request.setAttribute("misReclamos", misReclamos);
        request.setAttribute("totalReclamos", totalReclamos);
        request.setAttribute("pendientes", pendientes);
        request.setAttribute("enAtencion", enAtencion);
        request.setAttribute("resueltos", resueltos);
        
        request.getRequestDispatcher("vistas/usuario/misreclamos.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar categorías activas
        request.setAttribute("categorias", generalDAO.listarCategoriasActivas());
        request.getRequestDispatcher("vistas/usuario/nuevo_reclamo.jsp").forward(request, response);
    }

    private void registrarReclamo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        
        try {
            // Obtener datos del formulario
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            String asunto = request.getParameter("asunto");
            String descripcion = request.getParameter("descripcion");
            String prioridad = request.getParameter("prioridad");
            
            // Validar campos
            if (asunto == null || descripcion == null || prioridad == null ||
                asunto.trim().isEmpty() || descripcion.trim().isEmpty()) {
                request.setAttribute("error", "Todos los campos son obligatorios");
                request.setAttribute("categorias", generalDAO.listarCategoriasActivas());
                request.getRequestDispatcher("vistas/usuario/nuevo_reclamo.jsp").forward(request, response);
                return;
            }
            
            // Generar código de reclamo
            String codigoReclamo = reclamoDAO.generarCodigoReclamo();
            
            // Crear reclamo
            FFERAReclamo reclamo = new FFERAReclamo();
            reclamo.setCodigoReclamo(codigoReclamo);
            reclamo.setIdUsuario(idUsuario);
            reclamo.setIdCategoria(idCategoria);
            reclamo.setIdEstado(1); // Pendiente
            reclamo.setAsunto(asunto);
            reclamo.setDescripcion(descripcion);
            reclamo.setPrioridad(prioridad);
            
            // Registrar
            boolean registrado = reclamoDAO.registrarReclamo(reclamo);
            
            if (registrado) {
                // Obtener el reclamo registrado
                FFERAReclamo reclamoRegistrado = reclamoDAO.obtenerReclamoPorCodigo(codigoReclamo);
                
                // Registrar seguimiento inicial
                FFERASeguimiento seguimiento = new FFERASeguimiento();
                seguimiento.setIdReclamo(reclamoRegistrado.getIdReclamo());
                seguimiento.setIdUsuario(idUsuario);
                seguimiento.setIdEstadoAnterior(null);
                seguimiento.setIdEstadoNuevo(1);
                seguimiento.setObservaciones("Reclamo registrado por el usuario");
                seguimiento.setAccionRealizada("Creación de reclamo");
                seguimientoDAO.registrarSeguimiento(seguimiento);
                
                session.setAttribute("mensaje", "Reclamo registrado exitosamente con código: " + codigoReclamo);
                response.sendRedirect("FFERAReclamoServlet?accion=misreclamos");
            } else {
                request.setAttribute("error", "Error al registrar el reclamo");
                request.setAttribute("categorias", generalDAO.listarCategoriasActivas());
                request.getRequestDispatcher("vistas/usuario/nuevo_reclamo.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el reclamo: " + e.getMessage());
            request.setAttribute("categorias", generalDAO.listarCategoriasActivas());
            request.getRequestDispatcher("vistas/usuario/nuevo_reclamo.jsp").forward(request, response);
        }
    }

    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReclamo = Integer.parseInt(request.getParameter("id"));
            
            FFERAReclamo reclamo = reclamoDAO.obtenerReclamoPorId(idReclamo);
            
            if (reclamo != null) {
                request.setAttribute("reclamo", reclamo);
                request.getRequestDispatcher("vistas/usuario/detalle_reclamo.jsp").forward(request, response);
            } else {
                response.sendRedirect("FFERAReclamoServlet?accion=misreclamos");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAReclamoServlet?accion=misreclamos");
        }
    }

    private void verSeguimiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReclamo = Integer.parseInt(request.getParameter("id"));
            
            FFERAReclamo reclamo = reclamoDAO.obtenerReclamoPorId(idReclamo);
            List<FFERASeguimiento> seguimientos = seguimientoDAO.listarSeguimientosPorReclamo(idReclamo);
            
            if (reclamo != null) {
                request.setAttribute("reclamo", reclamo);
                request.setAttribute("seguimientos", seguimientos);
                request.getRequestDispatcher("vistas/usuario/seguimiento_reclamo.jsp").forward(request, response);
            } else {
                response.sendRedirect("FFERAReclamoServlet?accion=misreclamos");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAReclamoServlet?accion=misreclamos");
        }
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