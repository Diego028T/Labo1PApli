<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Página no encontrada | eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body class="disposicionPagina">
<%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
<main class="contenidoPrincipal">
    <div class="container">
        <h1 class="tituloPagina">Página no encontrada</h1>
        <p>La dirección solicitada no existe o ya no está disponible.</p>
        <a class="btn botonPrincipal" href="${pageContext.request.contextPath}/">Ir al inicio</a>
    </div>
</main>
<%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
</body>
</html>