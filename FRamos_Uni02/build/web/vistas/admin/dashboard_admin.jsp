<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FFERA - Panel de Administración</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        :root {
            --ffera-primary: #2563eb;
            --ffera-secondary: #1e40af;
            --ffera-success: #10b981;
            --ffera-danger: #ef4444;
            --ffera-warning: #f59e0b;
            --ffera-info: #06b6d4;
            --ffera-dark: #1e293b;
            --ffera-light: #f1f5f9;
        }

        body {
            background: var(--ffera-light);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        /* Sidebar Admin */
        .ffera-sidebar {
            min-height: 100vh;
            background: linear-gradient(180deg, #dc2626 0%, #991b1b 100%);
            color: white;
            position: fixed;
            top: 0;
            left: 0;
            width: 260px;
            z-index: 1000;
            box-shadow: 4px 0 15px rgba(0, 0, 0, 0.2);
        }

        .ffera-logo {
            padding: 1.5rem;
            text-align: center;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .ffera-logo h3 {
            font-weight: 700;
            letter-spacing: 2px;
            margin: 0;
        }

        .ffera-admin-badge {
            background: rgba(255, 255, 255, 0.2);
            padding: 0.3rem 0.8rem;
            border-radius: 15px;
            font-size: 0.75rem;
            margin-top: 0.5rem;
            display: inline-block;
        }

        .ffera-nav {
            padding: 1rem 0;
        }

        .ffera-nav-item {
            padding: 0.75rem 1.5rem;
            color: rgba(255, 255, 255, 0.8);
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            transition: all 0.3s;
            border-left: 3px solid transparent;
        }

        .ffera-nav-item:hover,
        .ffera-nav-item.active {
            background: rgba(255, 255, 255, 0.15);
            color: white;
            border-left-color: white;
        }

        .ffera-nav-item i {
            font-size: 1.2rem;
            width: 24px;
        }

        /* Main Content */
        .ffera-main {
            margin-left: 260px;
            padding: 2rem;
            min-height: 100vh;
        }

        /* Header Admin */
        .ffera-header {
            background: linear-gradient(135deg, var(--ffera-danger), #dc2626);
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            box-shadow: 0 8px 20px rgba(220, 38, 38, 0.3);
            color: white;
        }

        .ffera-header h2 {
            margin: 0;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .ffera-header p {
            margin: 0;
            opacity: 0.9;
        }

        /* Stats Cards Admin */
        .ffera-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .ffera-stat-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
            transition: all 0.3s;
            position: relative;
            overflow: hidden;
        }

        .ffera-stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 4px;
        }

        .ffera-stat-card.pendientes::before { background: var(--ffera-warning); }
        .ffera-stat-card.atencion::before { background: var(--ffera-info); }
        .ffera-stat-card.resueltos::before { background: var(--ffera-success); }
        .ffera-stat-card.rechazados::before { background: var(--ffera-danger); }

        .ffera-stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
        }

        .ffera-stat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .ffera-stat-title {
            font-size: 0.85rem;
            color: #64748b;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .ffera-stat-icon {
            width: 55px;
            height: 55px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.8rem;
        }

        .ffera-stat-icon.warning { background: rgba(245, 158, 11, 0.1); color: var(--ffera-warning); }
        .ffera-stat-icon.info { background: rgba(6, 182, 212, 0.1); color: var(--ffera-info); }
        .ffera-stat-icon.success { background: rgba(16, 185, 129, 0.1); color: var(--ffera-success); }
        .ffera-stat-icon.danger { background: rgba(239, 68, 68, 0.1); color: var(--ffera-danger); }

        .ffera-stat-value {
            font-size: 2.8rem;
            font-weight: 700;
            color: var(--ffera-dark);
            line-height: 1;
            margin-bottom: 0.5rem;
        }

        .ffera-stat-change {
            font-size: 0.85rem;
            color: #64748b;
        }

        /* Quick Actions */
        .ffera-quick-actions {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }

        .ffera-quick-actions h4 {
            margin-bottom: 1.5rem;
            color: var(--ffera-dark);
            font-weight: 700;
        }

        .ffera-action-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
        }

        .ffera-action-btn {
            padding: 1rem;
            border-radius: 10px;
            border: 2px solid #e2e8f0;
            background: white;
            text-decoration: none;
            text-align: center;
            transition: all 0.3s;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 0.5rem;
        }

        .ffera-action-btn:hover {
            border-color: var(--ffera-primary);
            background: var(--ffera-light);
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(37, 99, 235, 0.15);
        }

        .ffera-action-btn i {
            font-size: 2rem;
            color: var(--ffera-primary);
        }

        .ffera-action-btn span {
            color: var(--ffera-dark);
            font-weight: 600;
        }

        /* Recent Activity */
        .ffera-activity {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }

        .ffera-activity h4 {
            margin-bottom: 1.5rem;
            color: var(--ffera-dark);
            font-weight: 700;
        }

        .ffera-activity-item {
            padding: 1rem;
            border-left: 3px solid;
            margin-bottom: 1rem;
            border-radius: 8px;
            background: var(--ffera-light);
            transition: all 0.3s;
        }

        .ffera-activity-item:hover {
            transform: translateX(5px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }

        .ffera-activity-item.pendiente { border-left-color: var(--ffera-warning); }
        .ffera-activity-item.atencion { border-left-color: var(--ffera-info); }
        .ffera-activity-item.resuelto { border-left-color: var(--ffera-success); }

        .ffera-badge {
            padding: 0.4rem 0.8rem;
            border-radius: 6px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .ffera-badge-warning { background: rgba(245, 158, 11, 0.1); color: var(--ffera-warning); }
        .ffera-badge-info { background: rgba(6, 182, 212, 0.1); color: var(--ffera-info); }
        .ffera-badge-success { background: rgba(16, 185, 129, 0.1); color: var(--ffera-success); }
        .ffera-badge-danger { background: rgba(239, 68, 68, 0.1); color: var(--ffera-danger); }

        /* Chart Container */
        .ffera-chart {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
            margin-top: 2rem;
        }

        .ffera-chart h4 {
            margin-bottom: 1.5rem;
            color: var(--ffera-dark);
            font-weight: 700;
        }
    </style>
</head>
<body>
    <!-- Sidebar Admin -->
    <div class="ffera-sidebar">
        <div class="ffera-logo">
            <i class="bi bi-shield-fill-check" style="font-size: 2.5rem;"></i>
            <h3>FFERA ADMIN</h3>
            <span class="ffera-admin-badge">
                <i class="bi bi-star-fill"></i> Panel de Control
            </span>
        </div>

        <nav class="ffera-nav">
            <a href="FFERAAdminServlet?accion=dashboard" class="ffera-nav-item active">
                <i class="bi bi-speedometer2"></i>
                <span>Dashboard</span>
            </a>
            <a href="FFERAAdminServlet?accion=listar" class="ffera-nav-item">
                <i class="bi bi-file-earmark-text"></i>
                <span>Todos los Reclamos</span>
            </a>
            <a href="FFERAAdminServlet?accion=pendientes" class="ffera-nav-item">
                <i class="bi bi-exclamation-circle"></i>
                <span>Reclamos Pendientes</span>
            </a>
            <a href="FFERAUsuarioServlet?accion=listar" class="ffera-nav-item">
                <i class="bi bi-people-fill"></i>
                <span>Gestión de Usuarios</span>
            </a>
            <a href="FFERAReportesServlet?accion=general" class="ffera-nav-item">
                <i class="bi bi-bar-chart-fill"></i>
                <span>Reportes</span>
            </a>
            <hr style="border-color: rgba(255,255,255,0.2);">
            <a href="FFERALoginServlet?accion=logout" class="ffera-nav-item">
                <i class="bi bi-box-arrow-right"></i>
                <span>Cerrar Sesión</span>
            </a>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="ffera-main">
        <!-- Header -->
        <div class="ffera-header">
            <h2>
                <i class="bi bi-speedometer2"></i>
                Panel de Administración FFERA
            </h2>
            <p>Bienvenido/a, ${nombreUsuario} | Administrador del Sistema</p>
        </div>

        <!-- Stats Cards -->
        <div class="ffera-stats">
            <div class="ffera-stat-card pendientes">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">Pendientes</span>
                    <div class="ffera-stat-icon warning">
                        <i class="bi bi-clock-history"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">
                    ${estadisticasEstado['Pendiente'] != null ? estadisticasEstado['Pendiente'] : 0}
                </div>
                <div class="ffera-stat-change">
                    <i class="bi bi-arrow-up"></i> Requieren atención
                </div>
            </div>

            <div class="ffera-stat-card atencion">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">En Atención</span>
                    <div class="ffera-stat-icon info">
                        <i class="bi bi-gear-fill"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">
                    ${estadisticasEstado['En Atención'] != null ? estadisticasEstado['En Atención'] : 0}
                </div>
                <div class="ffera-stat-change">
                    <i class="bi bi-arrow-right"></i> En proceso
                </div>
            </div>

            <div class="ffera-stat-card resueltos">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">Resueltos</span>
                    <div class="ffera-stat-icon success">
                        <i class="bi bi-check-circle-fill"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">
                    ${estadisticasEstado['Resuelto'] != null ? estadisticasEstado['Resuelto'] : 0}
                </div>
                <div class="ffera-stat-change">
                    <i class="bi bi-check"></i> Finalizados
                </div>
            </div>

            <div class="ffera-stat-card rechazados">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">Total Sistema</span>
                    <div class="ffera-stat-icon danger">
                        <i class="bi bi-files"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">
                    <c:set var="total" value="0"/>
                    <c:forEach var="entry" items="${estadisticasEstado}">
                        <c:set var="total" value="${total + entry.value}"/>
                    </c:forEach>
                    ${total}
                </div>
                <div class="ffera-stat-change">
                    <i class="bi bi-database"></i> Todos los reclamos
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="ffera-quick-actions">
            <h4><i class="bi bi-lightning-fill text-warning"></i> Acciones Rápidas</h4>
            <div class="ffera-action-grid">
                <a href="FFERAAdminServlet?accion=pendientes" class="ffera-action-btn">
                    <i class="bi bi-exclamation-triangle"></i>
                    <span>Ver Pendientes</span>
                </a>
                <a href="FFERAAdminServlet?accion=listar" class="ffera-action-btn">
                    <i class="bi bi-list-check"></i>
                    <span>Todos los Reclamos</span>
                </a>
                <a href="FFERAUsuarioServlet?accion=nuevo" class="ffera-action-btn">
                    <i class="bi bi-person-plus"></i>
                    <span>Nuevo Usuario</span>
                </a>
                <a href="FFERAReportesServlet?accion=general" class="ffera-action-btn">
                    <i class="bi bi-graph-up"></i>
                    <span>Ver Reportes</span>
                </a>
            </div>
        </div>

        <!-- Recent Activity -->
        <div class="ffera-activity">
            <h4><i class="bi bi-clock-history text-info"></i> Reclamos Recientes</h4>
            
            <c:choose>
                <c:when test="${not empty reclamosRecientes}">
                    <c:forEach var="reclamo" items="${reclamosRecientes}">
                        <div class="ffera-activity-item ${reclamo.nombreEstado == 'Pendiente' ? 'pendiente' : reclamo.nombreEstado == 'En Atención' ? 'atencion' : 'resuelto'}">
                            <div class="d-flex justify-content-between align-items-start">
                                <div>
                                    <h6 class="mb-1">
                                        <strong>${reclamo.codigoReclamo}</strong> - ${reclamo.asunto}
                                    </h6>
                                    <small class="text-muted">
                                        <i class="bi bi-person"></i> ${reclamo.nombreUsuario} | 
                                        <i class="bi bi-tag"></i> ${reclamo.nombreCategoria}
                                    </small>
                                </div>
                                <div class="text-end">
                                    <span class="ffera-badge ffera-badge-${reclamo.nombreEstado == 'Pendiente' ? 'warning' : reclamo.nombreEstado == 'En Atención' ? 'info' : 'success'}">
                                        ${reclamo.nombreEstado}
                                    </span>
                                    <br>
                                    <small class="text-muted">
                                        <fmt:formatDate value="${reclamo.fechaRegistro}" pattern="dd/MM/yyyy" />
                                    </small>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="text-center text-muted py-4">
                        <i class="bi bi-inbox" style="font-size: 3rem;"></i>
                        <p>No hay reclamos registrados</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Statistics Chart -->
        <div class="ffera-chart">
            <h4><i class="bi bi-pie-chart-fill text-primary"></i> Distribución por Categoría</h4>
            <div class="row">
                <c:forEach var="entry" items="${estadisticasCategoria}">
                    <div class="col-md-6 mb-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <span><strong>${entry.key}</strong></span>
                            <span class="ffera-badge ffera-badge-info">${entry.value}</span>
                        </div>
                        <div class="progress mt-2" style="height: 8px;">
                            <c:set var="porcentaje" value="${(entry.value * 100) / total}"/>
                            <div class="progress-bar bg-primary" style="width: ${porcentaje}%"></div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>