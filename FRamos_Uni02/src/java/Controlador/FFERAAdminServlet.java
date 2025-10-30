package controlador;

import modelo.FFERAReclamo;
import modeloDAO.FFERAReclamoDAO;
import modeloDAO.FFERAGeneralDAO;
import modeloDAO.FFERASeguimientoDAO;
import modelo.FFERASeguimiento;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet para Panel de Administrador
 * @author FFERA
 */
@WebServlet(name = "FFERAAdminServlet", urlPatterns = {"/FFERAAdminServlet"})
public class FFERAAdminServlet extends HttpServlet {

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
        
        // Verificar rol de administrador
        int idRol = (Integer) session.getAttribute("idRol");
        if (idRol != 1) {
            response.sendRedirect("FFERALoginServlet?accion=login");
            return;
        }
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "dashboard";
        }
        
        switch (accion) {
            case "dashboard":
                mostrarDashboard(request, response);
                break;
            case "listar":
                listarTodosReclamos(request, response);
                break;
            case "pendientes":
                listarPendientes(request, response);
                break;
            case "detalle":
                verDetalleReclamo(request, response);
                break;
            case "asignar":
                asignarReclamo(request, response);
                break;
            case "cambiarEstado":
                cambiarEstado(request, response);
                break;
            case "registrarSeguimiento":
                registrarSeguimiento(request, response);
                break;
            default:
                mostrarDashboard(request, response);
        }
    }

    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener estadísticas generales
        Map<String, Integer> estadisticasEstado = reclamoDAO.obtenerEstadisticasPorEstado();
        Map<String, Integer> estadisticasCategoria = reclamoDAO.obtenerEstadisticasPorCategoria();
        Map<String, Integer> estadisticasPrioridad = reclamoDAO.obtenerEstadisticasPorPrioridad();
        
        // Reclamos recientes
        List<FFERAReclamo> reclamosRecientes = reclamoDAO.listarTodosReclamos();
        if (reclamosRecientes.size() > 5) {
            reclamosRecientes = reclamosRecientes.subList(0, 5);
        }
        
        request.setAttribute("estadisticasEstado", estadisticasEstado);
        request.setAttribute("estadisticasCategoria", estadisticasCategoria);
        request.setAttribute("estadisticasPrioridad", estadisticasPrioridad);
        request.setAttribute("reclamosRecientes", reclamosRecientes);
        
        request.getRequestDispatcher("vistas/admin/dashboard.jsp").forward(request, response);
    }

    private void listarTodosReclamos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String filtroEstado = request.getParameter("estado");
        String filtroCategoria = request.getParameter("categoria");
        String filtroPrioridad = request.getParameter("prioridad");
        
        List<FFERAReclamo> reclamos;
        
        if (filtroEstado != null || filtroCategoria != null || filtroPrioridad != null) {
            Integer idEstado = (filtroEstado != null && !filtroEstado.isEmpty()) 
                ? Integer.parseInt(filtroEstado) : null;
            Integer idCategoria = (filtroCategoria != null && !filtroCategoria.isEmpty()) 
                ? Integer.parseInt(filtroCategoria) : null;
            String prioridad = (filtroPrioridad != null && !filtroPrioridad.isEmpty()) 
                ? filtroPrioridad : null;
            
            reclamos = reclamoDAO.filtrarReclamos(idEstado, idCategoria, prioridad);
        } else {
            reclamos = reclamoDAO.listarTodosReclamos();
        }
        
        request.setAttribute("reclamos", reclamos);
        request.setAttribute("estados", generalDAO.listarEstados());
        request.setAttribute("categorias", generalDAO.listarCategorias());
        
        request.getRequestDispatcher("vistas/admin/listar_reclamos.jsp").forward(request, response);
    }

    private void listarPendientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<FFERAReclamo> reclamosPendientes = reclamoDAO.listarReclamosPendientes();
        
        request.setAttribute("reclamos", reclamosPendientes);
        request.setAttribute("titulo", "Reclamos Pendientes");
        
        request.getRequestDispatcher("vistas/admin/reclamos_pendientes.jsp").forward(request, response);
    }

    private void verDetalleReclamo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReclamo = Integer.parseInt(request.getParameter("id"));
            
            FFERAReclamo reclamo = reclamoDAO.obtenerReclamoPorId(idReclamo);
            List<FFERASeguimiento> seguimientos = seguimientoDAO.listarSeguimientosPorReclamo(idReclamo);
            
            if (reclamo != null) {
                request.setAttribute("reclamo", reclamo);
                request.setAttribute("seguimientos", seguimientos);
                request.setAttribute("estados", generalDAO.listarEstados());
                request.getRequestDispatcher("vistas/admin/detalle_reclamo.jsp").forward(request, response);
            } else {
                response.sendRedirect("FFERAAdminServlet?accion=listar");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAAdminServlet?accion=listar");
        }
    }

    private void asignarReclamo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int idUsuarioActual = (Integer) session.getAttribute("idUsuario");
        
        try {
            int idReclamo = Integer.parseInt(request.getParameter("idReclamo"));
            int idAsignado = Integer.parseInt(request.getParameter("idAsignado"));
            
            boolean asignado = reclamoDAO.asignarReclamo(idReclamo, idAsignado);
            
            if (asignado) {
                // Registrar seguimiento
                FFERASeguimiento seguimiento = new FFERASeguimiento();
                seguimiento.setIdReclamo(idReclamo);
                seguimiento.setIdUsuario(idUsuarioActual);
                
                FFERAReclamo reclamo = reclamoDAO.obtenerReclamoPorId(idReclamo);
                seguimiento.setIdEstadoAnterior(reclamo.getIdEstado());
                seguimiento.setIdEstadoNuevo(reclamo.getIdEstado());
                seguimiento.setObservaciones("Reclamo asignado a usuario ID: " + idAsignado);
                seguimiento.setAccionRealizada("Asignación de reclamo");
                
                seguimientoDAO.registrarSeguimiento(seguimiento);
                
                session.setAttribute("mensaje", "Reclamo asignado exitosamente");
            } else {
                session.setAttribute("error", "Error al asignar el reclamo");
            }
            
            response.sendRedirect("FFERAAdminServlet?accion=detalle&id=" + idReclamo);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAAdminServlet?accion=listar");
        }
    }

    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        
        try {
            int idReclamo = Integer.parseInt(request.getParameter("idReclamo"));
            int nuevoEstado = Integer.parseInt(request.getParameter("nuevoEstado"));
            String observaciones = request.getParameter("observaciones");
            
            boolean cambiado = seguimientoDAO.registrarSeguimientoConCambioEstado(
                idReclamo, idUsuario, nuevoEstado, observaciones, "Cambio de estado"
            );
            
            if (cambiado) {
                session.setAttribute("mensaje", "Estado actualizado correctamente");
            } else {
                session.setAttribute("error", "Error al cambiar el estado");
            }
            
            response.sendRedirect("FFERAAdminServlet?accion=detalle&id=" + idReclamo);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAAdminServlet?accion=listar");
        }
    }

    private void registrarSeguimiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int idUsuario = (Integer) session.getAttribute("idUsuario");
        
        try {
            int idReclamo = Integer.parseInt(request.getParameter("idReclamo"));
            String observaciones = request.getParameter("observaciones");
            String accion = request.getParameter("accion");
            
            FFERAReclamo reclamo = reclamoDAO.obtenerReclamoPorId(idReclamo);
            
            FFERASeguimiento seguimiento = new FFERASeguimiento();
            seguimiento.setIdReclamo(idReclamo);
            seguimiento.setIdUsuario(idUsuario);
            seguimiento.setIdEstadoAnterior(reclamo.getIdEstado());
            seguimiento.setIdEstadoNuevo(reclamo.getIdEstado());
            seguimiento.setObservaciones(observaciones);
            seguimiento.setAccionRealizada(accion);
            
            boolean registrado = seguimientoDAO.registrarSeguimiento(seguimiento);
            
            if (registrado) {
                session.setAttribute("mensaje", "Seguimiento registrado exitosamente");
            } else {
                session.setAttribute("error", "Error al registrar seguimiento");
            }
            
            response.sendRedirect("FFERAAdminServlet?accion=detalle&id=" + idReclamo);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FFERAAdminServlet?accion=listar");
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