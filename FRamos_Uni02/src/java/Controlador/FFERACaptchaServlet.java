package controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet para generar CAPTCHA dinámico
 * @author FFERA
 */
@WebServlet(name = "FFERACaptchaServlet", urlPatterns = {"/FFERACaptchaServlet"})
public class FFERACaptchaServlet extends HttpServlet {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;
    private static final String CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int LONGITUD_CAPTCHA = 6;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Generar código CAPTCHA aleatorio
        String codigoCaptcha = generarCodigoCaptcha();
        
        // Guardar en sesión
        HttpSession session = request.getSession(true);
        session.setAttribute("captcha", codigoCaptcha);
        
        // Crear imagen
        BufferedImage imagen = crearImagenCaptcha(codigoCaptcha);
        
        // Configurar respuesta
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // Enviar imagen
        OutputStream out = response.getOutputStream();
        ImageIO.write(imagen, "png", out);
        out.close();
    }

    private String generarCodigoCaptcha() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        
        for (int i = 0; i < LONGITUD_CAPTCHA; i++) {
            int index = random.nextInt(CARACTERES.length());
            codigo.append(CARACTERES.charAt(index));
        }
        
        return codigo.toString();
    }

    private BufferedImage crearImagenCaptcha(String codigo) {
        BufferedImage imagen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        
        // Fondo
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Líneas de ruido
        Random random = new Random();
        g2d.setColor(new Color(200, 200, 200));
        for (int i = 0; i < 8; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // Puntos de ruido
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g2d.fillRect(x, y, 2, 2);
        }
        
        // Texto del CAPTCHA
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        
        int x = 10;
        for (int i = 0; i < codigo.length(); i++) {
            // Color aleatorio para cada letra
            g2d.setColor(new Color(
                random.nextInt(100),
                random.nextInt(100),
                random.nextInt(100)
            ));
            
            // Rotación ligera
            int y = 35 + random.nextInt(10);
            g2d.drawString(String.valueOf(codigo.charAt(i)), x, y);
            x += 22;
        }
        
        g2d.dispose();
        return imagen;
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