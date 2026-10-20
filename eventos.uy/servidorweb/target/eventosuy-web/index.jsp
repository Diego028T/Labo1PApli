<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Inicio | eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body class="disposicionPagina">
<%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
<main class="contenidoPrincipal">
    <div class="container">
        <h1 class="tituloPagina">eventos.uy</h1>
        <p>Eventos académicos y profesionales.</p>
    </div>
</main>
<%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
</body>
</html>