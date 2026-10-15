<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error interno | eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body class="disposicionPagina">
<%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
<main class="contenidoPrincipal">
    <div class="container">
        <h1 class="tituloPagina">No pudimos completar la solicitud</h1>
        <p>Ocurrió un problema interno. Intentá nuevamente más tarde.</p>
        <a class="btn botonPrincipal" href="${pageContext.request.contextPath}/">Ir al inicio</a>
    </div>
</main>
<%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
</body>
</html>