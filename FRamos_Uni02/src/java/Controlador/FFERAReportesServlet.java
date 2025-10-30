package controlador;

import modeloDAO.FFERAReclamoDAO;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet para Reportes y Estadísticas
 * @author FFERA
 */
@WebServlet(name = "FFERAReportesServlet", urlPatterns = {"/FFERAReportesServlet"})
public class FFERAReportesServlet extends HttpServlet {

    private FFERAReclamoDAO reclamoDAO = new FFERAReclamoDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("FFERALoginServlet?accion=login");
            return;
        }
        
        // Solo administradores pueden ver reportes
        int idRol = (Integer) session.getAttribute("idRol");
        if (idRol != 1) {
            response.sendRedirect("FFERALoginServlet?accion=login");
            return;
        }
        
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            accion = "general";
        }
        
        switch (accion) {
            case "general":
                mostrarReporteGeneral(request, response);
                break;
            case "porEstado":
                mostrarReportePorEstado(request, response);
                break;
            case "porCategoria":
                mostrarReportePorCategoria(request, response);
                break;
            case "porPrioridad":
                mostrarReportePorPrioridad(request, response);
                break;
            default:
                mostrarReporteGeneral(request, response);
        }
    }

    private void mostrarReporteGeneral(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener todas las estadísticas
        Map<String, Integer> estadisticasEstado = reclamoDAO.obtenerEstadisticasPorEstado();
        Map<String, Integer> estadisticasCategoria = reclamoDAO.obtenerEstadisticasPorCategoria();
        Map<String, Integer> estadisticasPrioridad = reclamoDAO.obtenerEstadisticasPorPrioridad();
        
        // Calcular totales
        int totalReclamos = estadisticasEstado.values().stream()
                .mapToInt(Integer::intValue).sum();
        
        request.setAttribute("estadisticasEstado", estadisticasEstado);
        request.setAttribute("estadisticasCategoria", estadisticasCategoria);
        request.setAttribute("estadisticasPrioridad", estadisticasPrioridad);
        request.setAttribute("totalReclamos", totalReclamos);
        
        request.getRequestDispatcher("vistas/admin/reportes/reporte_general.jsp").forward(request, response);
    }

    private void mostrarReportePorEstado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, Integer> estadisticasEstado = reclamoDAO.obtenerEstadisticasPorEstado();
        
        int totalReclamos = estadisticasEstado.values().stream()
                .mapToInt(Integer::intValue).sum();
        
        request.setAttribute("estadisticas", estadisticasEstado);
        request.setAttribute("totalReclamos", totalReclamos);
        request.setAttribute("tipoReporte", "Estado");
        
        request.getRequestDispatcher("vistas/admin/reportes/reporte_estado.jsp").forward(request, response);
    }

    private void mostrarReportePorCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, Integer> estadisticasCategoria = reclamoDAO.obtenerEstadisticasPorCategoria();
        
        int totalReclamos = estadisticasCategoria.values().stream()
                .mapToInt(Integer::intValue).sum();
        
        request.setAttribute("estadisticas", estadisticasCategoria);
        request.setAttribute("totalReclamos", totalReclamos);
        request.setAttribute("tipoReporte", "Categoría");
        
        request.getRequestDispatcher("vistas/admin/reportes/reporte_categoria.jsp").forward(request, response);
    }

    private void mostrarReportePorPrioridad(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Map<String, Integer> estadisticasPrioridad = reclamoDAO.obtenerEstadisticasPorPrioridad();
        
        int totalReclamos = estadisticasPrioridad.values().stream()
                .mapToInt(Integer::intValue).sum();
        
        request.setAttribute("estadisticas", estadisticasPrioridad);
        request.setAttribute("totalReclamos", totalReclamos);
        request.setAttribute("tipoReporte", "Prioridad");
        
        request.getRequestDispatcher("vistas/admin/reportes/reporte_prioridad.jsp").forward(request, response);
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