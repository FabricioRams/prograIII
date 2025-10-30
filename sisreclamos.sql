-- =====================================================
-- SCRIPT SQL - SISTEMA DE RECLAMOS
-- Base de datos: sisreclamos
-- Autor: FRamos
-- =====================================================

-- Crear base de datos
DROP DATABASE IF EXISTS sisreclamos;
CREATE DATABASE sisreclamos CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE sisreclamos;

-- =====================================================
-- TABLA: Roles
-- =====================================================
CREATE TABLE fr_roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    estado TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: Usuarios
-- =====================================================
CREATE TABLE fr_usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    id_rol INT NOT NULL,
    ip_autorizada VARCHAR(45),
    estado TINYINT(1) DEFAULT 1,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_conexion TIMESTAMP NULL,
    FOREIGN KEY (id_rol) REFERENCES fr_roles(id_rol) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: Categorías de Reclamos
-- =====================================================
CREATE TABLE fr_categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    estado TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: Estados de Reclamos
-- =====================================================
CREATE TABLE fr_estados (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    orden_proceso INT DEFAULT 0
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: Reclamos
-- =====================================================
CREATE TABLE fr_reclamos (
    id_reclamo INT AUTO_INCREMENT PRIMARY KEY,
    codigo_reclamo VARCHAR(20) NOT NULL UNIQUE,
    id_usuario INT NOT NULL,
    id_categoria INT NOT NULL,
    id_estado INT NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    id_asignado INT NULL,
    prioridad ENUM('Baja', 'Media', 'Alta', 'Urgente') DEFAULT 'Media',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_resolucion TIMESTAMP NULL,
    FOREIGN KEY (id_usuario) REFERENCES fr_usuarios(id_usuario) ON UPDATE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES fr_categorias(id_categoria) ON UPDATE CASCADE,
    FOREIGN KEY (id_estado) REFERENCES fr_estados(id_estado) ON UPDATE CASCADE,
    FOREIGN KEY (id_asignado) REFERENCES fr_usuarios(id_usuario) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: Seguimiento de Reclamos
-- =====================================================
CREATE TABLE fr_seguimiento (
    id_seguimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_reclamo INT NOT NULL,
    id_usuario INT NOT NULL,
    id_estado_anterior INT,
    id_estado_nuevo INT NOT NULL,
    observaciones TEXT,
    accion_realizada VARCHAR(200),
    fecha_seguimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_reclamo) REFERENCES fr_reclamos(id_reclamo) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES fr_usuarios(id_usuario) ON UPDATE CASCADE,
    FOREIGN KEY (id_estado_anterior) REFERENCES fr_estados(id_estado) ON UPDATE CASCADE,
    FOREIGN KEY (id_estado_nuevo) REFERENCES fr_estados(id_estado) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- TABLA: Sesiones/Logs de Acceso
-- =====================================================
CREATE TABLE fr_sesiones (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    ip_conexion VARCHAR(45),
    navegador VARCHAR(200),
    fecha_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_logout TIMESTAMP NULL,
    estado_sesion ENUM('Activa', 'Cerrada', 'Expirada') DEFAULT 'Activa',
    FOREIGN KEY (id_usuario) REFERENCES fr_usuarios(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- INSERCIÓN DE DATOS INICIALES
-- =====================================================

-- Insertar roles
INSERT INTO fr_roles (nombre_rol, descripcion) VALUES
('Administrador', 'Acceso total al sistema, gestión de reclamos y usuarios'),
('Usuario', 'Puede crear y consultar sus propios reclamos'),
('Soporte', 'Puede atender y resolver reclamos asignados');

-- Insertar estados
INSERT INTO fr_estados (nombre_estado, descripcion, orden_proceso) VALUES
('Pendiente', 'Reclamo registrado, esperando atención', 1),
('En Atención', 'Reclamo siendo atendido por el equipo', 2),
('Resuelto', 'Reclamo finalizado satisfactoriamente', 3),
('Rechazado', 'Reclamo rechazado por no cumplir requisitos', 4),
('Cerrado', 'Reclamo cerrado definitivamente', 5);

-- Insertar categorías
INSERT INTO fr_categorias (nombre_categoria, descripcion) VALUES
('Servicio al Cliente', 'Problemas relacionados con la atención'),
('Producto Defectuoso', 'Fallas o defectos en productos'),
('Facturación', 'Errores en cobros o facturas'),
('Entrega', 'Problemas con tiempos o estado de entregas'),
('Garantía', 'Solicitudes relacionadas con garantías'),
('Devolución', 'Solicitudes de devolución de productos'),
('Consulta General', 'Consultas informativas generales'),
('Otros', 'Otros tipos de reclamos');

-- Insertar usuarios de prueba
-- Password: admin123 (debe ser hasheado en producción)
INSERT INTO fr_usuarios (username, password, nombres, apellidos, email, telefono, id_rol, ip_autorizada) VALUES
('admin', 'admin123', 'Fernando', 'Ramos', 'admin@sisreclamos.com', '999888777', 1, '192.168.1.100'),
('fsoporte', 'soporte123', 'Juan', 'Pérez', 'soporte@sisreclamos.com', '999777666', 3, '192.168.1.101'),
('cliente1', 'cliente123', 'María', 'González', 'maria@email.com', '999666555', 2, NULL),
('cliente2', 'cliente123', 'Pedro', 'López', 'pedro@email.com', '999555444', 2, NULL);

-- Insertar reclamos de prueba
INSERT INTO fr_reclamos (codigo_reclamo, id_usuario, id_categoria, id_estado, asunto, descripcion, prioridad) VALUES
('RCL-2025-001', 3, 2, 1, 'Producto con defecto de fábrica', 'El producto llegó con una pieza rota. Solicito cambio o reembolso.', 'Alta'),
('RCL-2025-002', 3, 4, 2, 'Retraso en entrega', 'Mi pedido debía llegar hace 5 días y aún no lo recibo.', 'Media'),
('RCL-2025-003', 4, 3, 1, 'Error en factura', 'Me cobraron doble el mismo producto.', 'Urgente'),
('RCL-2025-004', 4, 1, 3, 'Mal trato del personal', 'El personal fue grosero al atender mi consulta.', 'Baja');

-- Actualizar asignación de reclamo en atención
UPDATE fr_reclamos SET id_asignado = 2 WHERE codigo_reclamo = 'RCL-2025-002';

-- Insertar seguimiento inicial
INSERT INTO fr_seguimiento (id_reclamo, id_usuario, id_estado_anterior, id_estado_nuevo, observaciones, accion_realizada) VALUES
(1, 3, NULL, 1, 'Reclamo registrado por el usuario', 'Creación de reclamo'),
(2, 3, NULL, 1, 'Reclamo registrado por el usuario', 'Creación de reclamo'),
(2, 2, 1, 2, 'Reclamo asignado a soporte. Se contactará con el área de logística.', 'Cambio a En Atención'),
(3, 4, NULL, 1, 'Reclamo registrado por el usuario', 'Creación de reclamo'),
(4, 4, NULL, 1, 'Reclamo registrado por el usuario', 'Creación de reclamo'),
(4, 2, 1, 3, 'Se habló con el personal involucrado. Cliente satisfecho con la disculpa.', 'Resolución de reclamo');

-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================
CREATE INDEX idx_usuario_username ON fr_usuarios(username);
CREATE INDEX idx_usuario_email ON fr_usuarios(email);
CREATE INDEX idx_reclamo_codigo ON fr_reclamos(codigo_reclamo);
CREATE INDEX idx_reclamo_usuario ON fr_reclamos(id_usuario);
CREATE INDEX idx_reclamo_estado ON fr_reclamos(id_estado);
CREATE INDEX idx_reclamo_fecha ON fr_reclamos(fecha_registro);
CREATE INDEX idx_seguimiento_reclamo ON fr_seguimiento(id_reclamo);

-- =====================================================
-- VISTAS ÚTILES PARA REPORTES
-- =====================================================

-- Vista: Reclamos con información completa
CREATE VIEW vw_reclamos_completos AS
SELECT 
    r.id_reclamo,
    r.codigo_reclamo,
    r.asunto,
    r.descripcion,
    r.prioridad,
    r.fecha_registro,
    r.fecha_actualizacion,
    r.fecha_resolucion,
    u.username AS usuario_creador,
    CONCAT(u.nombres, ' ', u.apellidos) AS nombre_completo_usuario,
    u.email AS email_usuario,
    c.nombre_categoria,
    e.nombre_estado,
    CONCAT(ua.nombres, ' ', ua.apellidos) AS asignado_a,
    DATEDIFF(COALESCE(r.fecha_resolucion, NOW()), r.fecha_registro) AS dias_transcurridos
FROM fr_reclamos r
INNER JOIN fr_usuarios u ON r.id_usuario = u.id_usuario
INNER JOIN fr_categorias c ON r.id_categoria = c.id_categoria
INNER JOIN fr_estados e ON r.id_estado = e.id_estado
LEFT JOIN fr_usuarios ua ON r.id_asignado = ua.id_usuario;

-- Vista: Resumen por estado
CREATE VIEW vw_reclamos_por_estado AS
SELECT 
    e.nombre_estado,
    COUNT(r.id_reclamo) AS total_reclamos,
    ROUND(COUNT(r.id_reclamo) * 100.0 / (SELECT COUNT(*) FROM fr_reclamos), 2) AS porcentaje
FROM fr_estados e
LEFT JOIN fr_reclamos r ON e.id_estado = r.id_estado
GROUP BY e.id_estado, e.nombre_estado
ORDER BY e.orden_proceso;

-- Vista: Resumen por categoría
CREATE VIEW vw_reclamos_por_categoria AS
SELECT 
    c.nombre_categoria,
    COUNT(r.id_reclamo) AS total_reclamos,
    ROUND(COUNT(r.id_reclamo) * 100.0 / (SELECT COUNT(*) FROM fr_reclamos), 2) AS porcentaje
FROM fr_categorias c
LEFT JOIN fr_reclamos r ON c.id_categoria = r.id_categoria
WHERE c.estado = 1
GROUP BY c.id_categoria, c.nombre_categoria
ORDER BY total_reclamos DESC;

-- =====================================================
-- STORED PROCEDURES ÚTILES
-- =====================================================

DELIMITER $$

-- Procedimiento: Generar código único de reclamo
CREATE PROCEDURE sp_generar_codigo_reclamo(OUT nuevo_codigo VARCHAR(20))
BEGIN
    DECLARE anio INT;
    DECLARE contador INT;
    SET anio = YEAR(CURDATE());
    
    SELECT COUNT(*) + 1 INTO contador 
    FROM fr_reclamos 
    WHERE YEAR(fecha_registro) = anio;
    
    SET nuevo_codigo = CONCAT('RCL-', anio, '-', LPAD(contador, 3, '0'));
END$$

-- Procedimiento: Registrar seguimiento automático
CREATE PROCEDURE sp_registrar_seguimiento(
    IN p_id_reclamo INT,
    IN p_id_usuario INT,
    IN p_id_estado_nuevo INT,
    IN p_observaciones TEXT,
    IN p_accion VARCHAR(200)
)
BEGIN
    DECLARE v_estado_anterior INT;
    
    SELECT id_estado INTO v_estado_anterior 
    FROM fr_reclamos 
    WHERE id_reclamo = p_id_reclamo;
    
    INSERT INTO fr_seguimiento (
        id_reclamo, 
        id_usuario, 
        id_estado_anterior, 
        id_estado_nuevo, 
        observaciones, 
        accion_realizada
    ) VALUES (
        p_id_reclamo,
        p_id_usuario,
        v_estado_anterior,
        p_id_estado_nuevo,
        p_observaciones,
        p_accion
    );
    
    UPDATE fr_reclamos 
    SET id_estado = p_id_estado_nuevo,
        fecha_actualizacion = NOW()
    WHERE id_reclamo = p_id_reclamo;
    
    IF p_id_estado_nuevo = 3 THEN
        UPDATE fr_reclamos 
        SET fecha_resolucion = NOW()
        WHERE id_reclamo = p_id_reclamo;
    END IF;
END$$

DELIMITER ;

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================

-- Mostrar resumen de tablas creadas
SELECT 'Base de datos sisreclamos creada exitosamente' AS Mensaje;
SELECT TABLE_NAME AS Tabla, TABLE_ROWS AS Registros 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'sisreclamos' AND TABLE_TYPE = 'BASE TABLE';