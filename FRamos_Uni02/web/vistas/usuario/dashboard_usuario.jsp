<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FFERA - Panel de Usuario</title>
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

        /* Sidebar */
        .ffera-sidebar {
            min-height: 100vh;
            background: linear-gradient(180deg, var(--ffera-dark) 0%, #334155 100%);
            color: white;
            position: fixed;
            top: 0;
            left: 0;
            width: 260px;
            z-index: 1000;
            box-shadow: 4px 0 15px rgba(0, 0, 0, 0.1);
        }

        .ffera-logo {
            padding: 1.5rem;
            text-align: center;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .ffera-logo h3 {
            font-weight: 700;
            letter-spacing: 2px;
            margin: 0;
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
            background: rgba(255, 255, 255, 0.1);
            color: white;
            border-left-color: var(--ffera-primary);
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

        /* Header */
        .ffera-header {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .ffera-welcome h2 {
            margin: 0;
            color: var(--ffera-dark);
            font-weight: 700;
        }

        .ffera-welcome p {
            margin: 0;
            color: #64748b;
        }

        .ffera-user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .ffera-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--ffera-primary), var(--ffera-info));
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.5rem;
            font-weight: 700;
        }

        .ffera-logout-btn {
            background: var(--ffera-danger);
            color: white;
            border: none;
            border-radius: 8px;
            padding: 0.5rem 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            transition: all 0.3s;
        }

        .ffera-logout-btn:hover {
            background: #dc2626;
            transform: translateY(-2px);
        }

        /* Stats Cards */
        .ffera-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .ffera-stat-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            transition: all 0.3s;
            border-left: 4px solid;
        }

        .ffera-stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        .ffera-stat-card.primary { border-left-color: var(--ffera-primary); }
        .ffera-stat-card.warning { border-left-color: var(--ffera-warning); }
        .ffera-stat-card.info { border-left-color: var(--ffera-info); }
        .ffera-stat-card.success { border-left-color: var(--ffera-success); }

        .ffera-stat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .ffera-stat-title {
            font-size: 0.9rem;
            color: #64748b;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .ffera-stat-icon {
            width: 50px;
            height: 50px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
        }

        .ffera-stat-icon.primary { background: rgba(37, 99, 235, 0.1); color: var(--ffera-primary); }
        .ffera-stat-icon.warning { background: rgba(245, 158, 11, 0.1); color: var(--ffera-warning); }
        .ffera-stat-icon.info { background: rgba(6, 182, 212, 0.1); color: var(--ffera-info); }
        .ffera-stat-icon.success { background: rgba(16, 185, 129, 0.1); color: var(--ffera-success); }

        .ffera-stat-value {
            font-size: 2.5rem;
            font-weight: 700;
            color: var(--ffera-dark);
            line-height: 1;
        }

        /* Action Buttons */
        .ffera-actions {
            display: flex;
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .ffera-btn {
            padding: 0.75rem 1.5rem;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            transition: all 0.3s;
            text-decoration: none;
        }

        .ffera-btn-primary {
            background: linear-gradient(135deg, var(--ffera-primary), var(--ffera-info));
            color: white;
        }

        .ffera-btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(37, 99, 235, 0.3);
        }

        /* Alert Messages */
        .ffera-alert {
            border-radius: 10px;
            border: none;
            padding: 1rem 1.5rem;
            margin-bottom: 1.5rem;
            animation: slideDown 0.3s ease-out;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .ffera-alert-success {
            background: #ecfdf5;
            color: #059669;
            border-left: 4px solid #10b981;
        }

        .ffera-alert-info {
            background: #eff6ff;
            color: #0284c7;
            border-left: 4px solid #06b6d4;
        }

        /* Content Card */
        .ffera-content-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        .ffera-content-card h3 {
            color: var(--ffera-dark);
            margin-bottom: 1.5rem;
            font-weight: 700;
        }

        /* Badge */
        .ffera-badge {
            padding: 0.4rem 0.8rem;
            border-radius: 6px;
            font-size: 0.85rem;
            font-weight: 600;
        }

        .ffera-badge-primary { background: rgba(37, 99, 235, 0.1); color: var(--ffera-primary); }
        .ffera-badge-success { background: rgba(16, 185, 129, 0.1); color: var(--ffera-success); }
        .ffera-badge-warning { background: rgba(245, 158, 11, 0.1); color: var(--ffera-warning); }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="ffera-sidebar">
        <div class="ffera-logo">
            <i class="bi bi-shield-check" style="font-size: 2.5rem;"></i>
            <h3>FFERA</h3>
            <small>Sistema de Reclamos</small>
        </div>

        <nav class="ffera-nav">
            <a href="FFERAReclamoServlet?accion=misreclamos" class="ffera-nav-item active">
                <i class="bi bi-house-door-fill"></i>
                <span>Inicio</span>
            </a>
            <a href="FFERAReclamoServlet?accion=nuevo" class="ffera-nav-item">
                <i class="bi bi-plus-circle-fill"></i>
                <span>Nuevo Reclamo</span>
            </a>
            <a href="FFERAReclamoServlet?accion=misreclamos" class="ffera-nav-item">
                <i class="bi bi-list-ul"></i>
                <span>Mis Reclamos</span>
            </a>
            <hr style="border-color: rgba(255,255,255,0.1);">
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
            <div class="ffera-welcome">
                <h2>¡Bienvenido/a, ${nombreUsuario}!</h2>
                <p><i class="bi bi-shield-check"></i> Panel de Usuario | ${rolUsuario}</p>
            </div>
            <div class="ffera-user-info">
                <div class="ffera-avatar">
                    <i class="bi bi-person-fill"></i>
                </div>
                <div>
                    <strong>${nombreUsuario}</strong><br>
                    <small class="text-muted">${usuario.email}</small>
                </div>
                <a href="FFERALoginServlet?accion=logout" class="ffera-logout-btn">
                    <i class="bi bi-box-arrow-right"></i>
                    Salir
                </a>
            </div>
        </div>

        <!-- Mensajes -->
        <c:if test="${not empty mensaje}">
            <div class="ffera-alert ffera-alert-success">
                <i class="bi bi-check-circle-fill me-2"></i>
                ${mensaje}
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.mensaje}">
            <div class="ffera-alert ffera-alert-success">
                <i class="bi bi-check-circle-fill me-2"></i>
                ${sessionScope.mensaje}
            </div>
            <c:remove var="mensaje" scope="session"/>
        </c:if>

        <!-- Stats Cards -->
        <div class="ffera-stats">
            <div class="ffera-stat-card primary">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">Total Reclamos</span>
                    <div class="ffera-stat-icon primary">
                        <i class="bi bi-file-earmark-text-fill"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">${totalReclamos != null ? totalReclamos : 0}</div>
            </div>

            <div class="ffera-stat-card warning">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">Pendientes</span>
                    <div class="ffera-stat-icon warning">
                        <i class="bi bi-clock-fill"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">${pendientes != null ? pendientes : 0}</div>
            </div>

            <div class="ffera-stat-card info">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">En Atención</span>
                    <div class="ffera-stat-icon info">
                        <i class="bi bi-gear-fill"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">${enAtencion != null ? enAtencion : 0}</div>
            </div>

            <div class="ffera-stat-card success">
                <div class="ffera-stat-header">
                    <span class="ffera-stat-title">Resueltos</span>
                    <div class="ffera-stat-icon success">
                        <i class="bi bi-check-circle-fill"></i>
                    </div>
                </div>
                <div class="ffera-stat-value">${resueltos != null ? resueltos : 0}</div>
            </div>
        </div>

        <!-- Actions -->
        <div class="ffera-actions">
            <a href="FFERAReclamoServlet?accion=nuevo" class="ffera-btn ffera-btn-primary">
                <i class="bi bi-plus-circle-fill"></i>
                Registrar Nuevo Reclamo
            </a>
        </div>

        <!-- Info Card -->
        <div class="ffera-content-card">
            <h3><i class="bi bi-info-circle-fill text-primary"></i> Información del Sistema</h3>
            <div class="row">
                <div class="col-md-6">
                    <p><strong>Usuario:</strong> ${usuario.username}</p>
                    <p><strong>Nombre Completo:</strong> ${nombreUsuario}</p>
                    <p><strong>Email:</strong> ${usuario.email}</p>
                </div>
                <div class="col-md-6">
                    <p><strong>Rol:</strong> <span class="ffera-badge ffera-badge-primary">${rolUsuario}</span></p>
                    <p><strong>Estado:</strong> <span class="ffera-badge ffera-badge-success">Activo</span></p>
                    <p><strong>Última Conexión:</strong> 
                        <fmt:formatDate value="${usuario.ultimaConexion}" pattern="dd/MM/yyyy HH:mm" />
                    </p>
                </div>
            </div>
            <hr>
            <div class="ffera-alert ffera-alert-info">
                <i class="bi bi-lightbulb-fill me-2"></i>
                <strong>Consejo:</strong> Puede registrar un nuevo reclamo haciendo clic en el botón superior.
                Todos sus reclamos serán atendidos en el menor tiempo posible.
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>