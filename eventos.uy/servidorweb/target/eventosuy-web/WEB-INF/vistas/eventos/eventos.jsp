<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Eventos | eventos.uy</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
    </head>
    <body class="disposicionPagina">
       <%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
        <main class="contenidoPrincipal">
            <div class="container">
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 tituloPagina">
                    <h1 class="mb-0">
                        Eventos<c:if test="${not empty categoria}"> de ${categoria}</c:if>
                    </h1>
                    <a class="btn botonPrincipal" href="${pageContext.request.contextPath}/eventos/nuevo">Nuevo evento</a>
                </div>
                <section class="row disposicionTarjetas">
                    <c:forEach items="${eventos}" var="e">
                        <article class="col-md-6 col-lg-4">
                            <div class="tarjetaEvento">
                                <h2 class="h5">${e.nombre}</h2>
                                <p class="textoAyuda mb-3">${e.descripcion}</p>
                                <a class="btn btn-outline-primary btn-sm"
                                   href="${pageContext.request.contextPath}/evento?id=${e.id}">Ver detalle</a>
                            </div>
                        </article>
                    </c:forEach>
                    <c:if test="${empty eventos}">
                        <p>No hay eventos para mostrar.</p>
                    </c:if>
                </section>
            </div>
        </main>
    <%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
    </body>
</html>
