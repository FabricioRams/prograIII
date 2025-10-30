package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de configuración para la conexión a la base de datos
 * @author FFERA
 */
public class FFERAConexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/sisreclamos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Cambiar según tu configuración
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static Connection conexion = null;
    
    /**
     * Constructor privado para evitar instanciación
     */
    private FFERAConexion() {
    }
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     */
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName(DRIVER);
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ Conexión exitosa a la base de datos");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error: Driver no encontrado - " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Error SQL al conectar - " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al cerrar conexión - " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método para probar la conexión
     */
    public static void main(String[] args) {
        Connection conn = FFERAConexion.getConexion();
        if (conn != null) {
            System.out.println("=================================");
            System.out.println("  PRUEBA DE CONEXIÓN EXITOSA");
            System.out.println("=================================");
            FFERAConexion.cerrarConexion();
        } else {
            System.out.println("=================================");
            System.out.println("  ERROR EN LA CONEXIÓN");
            System.out.println("=================================");
        }
    }
}   