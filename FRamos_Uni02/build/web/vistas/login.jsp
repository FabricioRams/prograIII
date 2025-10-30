<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FFERA - Sistema de Reclamos | Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        :root {
            --ffera-primary: #2563eb;
            --ffera-secondary: #1e40af;
            --ffera-accent: #3b82f6;
            --ffera-dark: #1e293b;
            --ffera-light: #f1f5f9;
        }

        body {
            background: linear-gradient(135deg, var(--ffera-primary) 0%, var(--ffera-secondary) 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .login-container {
            max-width: 450px;
            width: 100%;
            margin: 0 auto;
        }

        .login-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            animation: slideUp 0.5s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .login-header {
            background: linear-gradient(135deg, var(--ffera-primary), var(--ffera-accent));
            color: white;
            padding: 2.5rem 2rem;
            text-align: center;
        }

        .login-header h1 {
            font-size: 1.8rem;
            font-weight: 700;
            margin: 0;
            letter-spacing: 2px;
        }

        .login-header p {
            margin: 0.5rem 0 0 0;
            opacity: 0.9;
            font-size: 0.95rem;
        }

        .login-body {
            padding: 2.5rem 2rem;
        }

        .form-floating {
            margin-bottom: 1.2rem;
        }

        .form-control {
            border-radius: 10px;
            border: 2px solid #e2e8f0;
            padding: 0.75rem 1rem;
            transition: all 0.3s;
        }

        .form-control:focus {
            border-color: var(--ffera-primary);
            box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.15);
        }

        .input-group-text {
            background: var(--ffera-light);
            border: 2px solid #e2e8f0;
            border-right: none;
            border-radius: 10px 0 0 10px;
        }

        .input-group .form-control {
            border-left: none;
            border-radius: 0 10px 10px 0;
        }

        .captcha-container {
            background: var(--ffera-light);
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 1.2rem;
            border: 2px solid #e2e8f0;
        }

        .captcha-image {
            border: 2px solid white;
            border-radius: 8px;
            cursor: pointer;
            transition: transform 0.2s;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .captcha-image:hover {
            transform: scale(1.02);
        }

        .btn-refresh {
            background: var(--ffera-primary);
            color: white;
            border: none;
            border-radius: 8px;
            padding: 0.5rem 1rem;
            transition: all 0.3s;
        }

        .btn-refresh:hover {
            background: var(--ffera-secondary);
            transform: translateY(-2px);
        }

        .btn-login {
            background: linear-gradient(135deg, var(--ffera-primary), var(--ffera-accent));
            border: none;
            border-radius: 10px;
            padding: 0.9rem;
            font-size: 1.1rem;
            font-weight: 600;
            letter-spacing: 1px;
            transition: all 0.3s;
            box-shadow: 0 4px 15px rgba(37, 99, 235, 0.3);
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(37, 99, 235, 0.4);
        }

        .alert {
            border-radius: 10px;
            border: none;
            animation: fadeIn 0.3s;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        .alert-danger {
            background: #fee;
            color: #c00;
            border-left: 4px solid #c00;
        }

        .login-footer {
            text-align: center;
            padding: 1.5rem;
            background: var(--ffera-light);
            font-size: 0.9rem;
            color: #64748b;
        }

        .security-badge {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.5rem 1rem;
            background: #ecfdf5;
            border-radius: 20px;
            color: #059669;
            font-size: 0.85rem;
            margin-top: 1rem;
        }

        .password-toggle {
            cursor: pointer;
            color: #64748b;
            transition: color 0.2s;
        }

        .password-toggle:hover {
            color: var(--ffera-primary);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="login-card">
                <!-- Header -->
                <div class="login-header">
                    <i class="bi bi-shield-lock" style="font-size: 3rem;"></i>
                    <h1>FFERA SYSTEM</h1>
                    <p>Sistema de Gestión de Reclamos</p>
                </div>

                <!-- Body -->
                <div class="login-body">
                    <!-- Mensaje de error -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            ${error}
                        </div>
                    </c:if>

                    <!-- Mensaje de éxito -->
                    <c:if test="${not empty mensaje}">
                        <div class="alert alert-success" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>
                            ${mensaje}
                        </div>
                    </c:if>

                    <!-- Formulario de Login -->
                    <form action="FFERALoginServlet" method="POST" id="fferaLoginForm">
                        <input type="hidden" name="accion" value="validar">

                        <!-- Usuario -->
                        <div class="input-group mb-3">
                            <span class="input-group-text">
                                <i class="bi bi-person-fill"></i>
                            </span>
                            <input type="text" 
                                   class="form-control" 
                                   name="username" 
                                   placeholder="Usuario" 
                                   value="${username}"
                                   required 
                                   autofocus>
                        </div>

                        <!-- Contraseña -->
                        <div class="input-group mb-3">
                            <span class="input-group-text">
                                <i class="bi bi-lock-fill"></i>
                            </span>
                            <input type="password" 
                                   class="form-control" 
                                   name="password" 
                                   id="fferaPassword"
                                   placeholder="Contraseña" 
                                   required>
                            <span class="input-group-text password-toggle" onclick="fferaTogglePassword()">
                                <i class="bi bi-eye-fill" id="fferaToggleIcon"></i>
                            </span>
                        </div>

                        <!-- CAPTCHA -->
                        <div class="captcha-container">
                            <label class="form-label fw-bold">
                                <i class="bi bi-shield-check"></i> Código de Seguridad
                            </label>
                            <div class="row align-items-center mb-2">
                                <div class="col-7">
                                    <img src="FFERACaptchaServlet" 
                                         alt="CAPTCHA" 
                                         class="captcha-image w-100"
                                         id="fferaCaptchaImage"
                                         onclick="fferaRefreshCaptcha()">
                                </div>
                                <div class="col-5 text-end">
                                    <button type="button" 
                                            class="btn btn-refresh w-100"
                                            onclick="fferaRefreshCaptcha()">
                                        <i class="bi bi-arrow-clockwise"></i> Renovar
                                    </button>
                                </div>
                            </div>
                            <input type="text" 
                                   class="form-control" 
                                   name="captcha" 
                                   placeholder="Ingrese el código de seguridad"
                                   maxlength="6"
                                   style="text-transform: uppercase;"
                                   required>
                            <small class="text-muted">
                                <i class="bi bi-info-circle"></i> 
                                Ingrese los caracteres que ve en la imagen
                            </small>
                        </div>

                        <!-- Botón de Login -->
                        <button type="submit" class="btn btn-login btn-primary w-100">
                            <i class="bi bi-box-arrow-in-right me-2"></i>
                            INICIAR SESIÓN
                        </button>

                        <!-- Badge de seguridad -->
                        <div class="text-center">
                            <div class="security-badge">
                                <i class="bi bi-shield-fill-check"></i>
                                <span>Conexión Segura</span>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Footer -->
                <div class="login-footer">
                    <p class="mb-0">
                        <i class="bi bi-c-circle"></i> 2025 FFERA System - Todos los derechos reservados
                    </p>
                    <small class="text-muted">
                        Sistema de Gestión de Reclamos v1.0
                    </small>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Función FFERA para refrescar CAPTCHA
        function fferaRefreshCaptcha() {
            const img = document.getElementById('fferaCaptchaImage');
            img.src = 'FFERACaptchaServlet?t=' + new Date().getTime();
        }

        // Función FFERA para mostrar/ocultar contraseña
        function fferaTogglePassword() {
            const passwordInput = document.getElementById('fferaPassword');
            const toggleIcon = document.getElementById('fferaToggleIcon');
            
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleIcon.classList.remove('bi-eye-fill');
                toggleIcon.classList.add('bi-eye-slash-fill');
            } else {
                passwordInput.type = 'password';
                toggleIcon.classList.remove('bi-eye-slash-fill');
                toggleIcon.classList.add('bi-eye-fill');
            }
        }

        // Auto-mayúsculas en CAPTCHA
        document.querySelector('input[name="captcha"]').addEventListener('input', function(e) {
            this.value = this.value.toUpperCase();
        });

        // Validación del formulario
        document.getElementById('fferaLoginForm').addEventListener('submit', function(e) {
            const username = document.querySelector('input[name="username"]').value.trim();
            const password = document.querySelector('input[name="password"]').value.trim();
            const captcha = document.querySelector('input[name="captcha"]').value.trim();

            if (!username || !password || !captcha) {
                e.preventDefault();
                alert('Por favor complete todos los campos');
                return false;
            }

            if (captcha.length !== 6) {
                e.preventDefault();
                alert('El código de seguridad debe tener 6 caracteres');
                return false;
            }
        });
    </script>
</body>
</html>