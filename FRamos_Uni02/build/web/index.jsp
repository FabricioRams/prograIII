<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="refresh" content="0;url=FFERALoginServlet?accion=login">
    <title>FFERA System - Redirigiendo...</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .ffera-loader {
            text-align: center;
            color: white;
        }

        .ffera-loader h2 {
            font-weight: 700;
            letter-spacing: 2px;
            margin-bottom: 1rem;
        }

        .ffera-spinner {
            width: 60px;
            height: 60px;
            border: 5px solid rgba(255, 255, 255, 0.2);
            border-top-color: white;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin: 2rem auto;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }

        .ffera-logo {
            font-size: 4rem;
            margin-bottom: 1rem;
            animation: pulse 2s ease-in-out infinite;
        }

        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); }
        }
    </style>
</head>
<body>
    <div class="ffera-loader">
        <div class="ffera-logo">🛡️</div>
        <h2>FFERA SYSTEM</h2>
        <p>Sistema de Gestión de Reclamos</p>
        <div class="ffera-spinner"></div>
        <p>Redirigiendo al inicio de sesión...</p>
        <small>Si no es redirigido automáticamente, 
            <a href="FFERALoginServlet?accion=login" style="color: white; text-decoration: underline;">
                haga clic aquí
            </a>
        </small>
    </div>

    <script>
        // Redirección JavaScript como respaldo
        setTimeout(function() {
            window.location.href = 'FFERALoginServlet?accion=login';
        }, 100);
    </script>
</body>
</html>