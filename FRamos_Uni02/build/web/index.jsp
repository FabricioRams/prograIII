<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Redirigiendo...</title>
</head>
<body>
    <%
        // Redirección directa desde el servidor
        response.sendRedirect(request.getContextPath() + "/FFERALoginServlet?accion=login");
    %>
</body>
</html>