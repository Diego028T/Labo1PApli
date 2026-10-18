<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Consulta de tipo de registro | eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body class="disposicionPagina">
<%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
<main class="contenidoPrincipal">
    <div class="container">
        <h1 class="tituloPagina">Consulta de tipo de registro</h1>
        <h2 class="h5 mb-3">Eventos</h2>
        <c:choose>
            <c:when test="${empty requestScope.eventos}">
                <p>No hay eventos disponibles.</p>
            </c:when>
            <c:otherwise>
                <div class="row disposicionTarjetas">
                    <c:forEach items="${requestScope.eventos}" var="evento">
                        <article class="col-md-6 col-lg-4">
                            <div class="tarjetaEvento">
                                <h3 class="h5"><c:out value="${evento.nombre}" /></h3>
                                <p class="textoAyuda mb-0"><c:out value="${evento.descripcion}" /></p>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>
<%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
</body>
</html>