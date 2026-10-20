<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Detalle de evento | eventos.uy</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
    </head>
    <body class="disposicionPagina">
        <%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
        <main class="contenidoPrincipal">
            <div class="container">
                <h1 class="tituloPagina">Detalle de evento</h1>
                <section class="row disposicionDetalle">
                    <div class="col-lg-8 columnaPrincipal">
                        <article class="tarjetaContenido">
                            <c:if test="${evento.tieneImagen}">
                                <img class="img-fluid mb-3"
                                     src="${pageContext.request.contextPath}/imagen?tipo=evento&amp;id=${evento.id}"
                                     alt="Imagen de ${evento.nombre}">
                            </c:if>
                            <h2 class="h4">${evento.nombre} (${evento.sigla})</h2>
                            <p>${evento.descripcion}</p>
                            <p class="textoAyuda">
                                Alta: ${evento.fechaAlta.dia}/${evento.fechaAlta.mes}/${evento.fechaAlta.anio}
                            </p>
                            <p class="textoAyuda mb-0">
                                Categorías:
                                <c:forEach items="${evento.categorias}" var="cat" varStatus="s">
                                    <a href="<c:url value='/eventos'><c:param name='categoria' value='${cat}'/></c:url>">${cat}</a><c:if test="${not s.last}">, </c:if>
                                </c:forEach>
                            </p>
                        </article>
                        <article class="tarjetaContenido mt-3">
                            <h2 class="h5">Ediciones</h2>
                            <c:choose>
                                <c:when test="${empty ediciones}">
                                    <p class="mb-0">Este evento no tiene ediciones.</p>
                                </c:when>
                                <c:otherwise>
                                    <ul class="mb-0">
                                        <c:forEach items="${ediciones}" var="ed">
                                            <li>
                                                <a href="${pageContext.request.contextPath}/edicion?id=${ed.id}">${ed.nombre}</a>
                                                (${ed.ciudad}, ${ed.pais})
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </article>
                    </div>
                    <aside class="col-lg-4 columnaLateral">
                        <div class="tarjetaContenido">
                            <h2 class="h5">Acciones</h2>
                            <div class="bloqueAcciones">
                                <a class="btn botonPrincipal" href="${pageContext.request.contextPath}/ediciones/nueva?evento=${evento.id}">Nueva edición</a>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/eventos">Volver al listado</a>
                            </div>
                        </div>
                    </aside>
                </section>
            </div>
        </main>
    <%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
    </body>
</html>
