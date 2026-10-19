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
        <c:if test="${empty requestScope.idEventoSeleccionado}">
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
                                    <h3 class="h5"><c:out value="${evento.nombre}"/></h3>
                                    <p class="textoAyuda mb-0"><c:out value="${evento.descripcion}"/></p>
                                    <c:url var="urlEdiciones" value="/consulta-tipo-registro">
                                        <c:param name="idEvento" value="${evento.id}"/>
                                    </c:url>
                                    <a class="btn botonPrincipal mt-3" href="${urlEdiciones}">Ver ediciones</a>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>
        <c:if test="${not empty requestScope.idEventoSeleccionado and empty requestScope.idEdicionSeleccionada}">
            <h2 class="h5 mb-3">Ediciones del evento seleccionado</h2>
            <c:if test="${empty requestScope.ediciones}">
                <p>Este evento no tiene ediciones disponibles.</p>
            </c:if>
            <c:forEach items="${requestScope.ediciones}" var="edicion">
                <p><c:out value="${edicion.nombre}"/> — <c:out value="${edicion.sigla}"/></p>
                <c:url var="urlTipos" value="/consulta-tipo-registro">
                    <c:param name="idEvento" value="${requestScope.idEventoSeleccionado}"/>
                    <c:param name="idEdicion" value="${edicion.id}"/>
                </c:url>
                <a class="btn botonPrincipal mb-3" href="${urlTipos}">Ver tipos de registro</a>
            </c:forEach>
            <c:url var="urlEventos" value="/consulta-tipo-registro"/>
            <a class="btn btn-outline-secondary" href="${urlEventos}">Volver a eventos</a>
        </c:if>
        <c:if test="${not empty requestScope.idEdicionSeleccionada and empty requestScope.tipoSeleccionado}">
            <h2 class="h5 mb-3">Tipos de registro de la edición seleccionada</h2>
            <c:if test="${empty requestScope.tiposRegistro}">
                <p>Esta edición no tiene tipos de registro disponibles.</p>
            </c:if>
            <c:forEach items="${requestScope.tiposRegistro}" var="tipo">
                <c:url var="urlDetalleTipo" value="/consulta-tipo-registro">
                    <c:param name="idEvento" value="${requestScope.idEventoSeleccionado}" />
                    <c:param name="idEdicion" value="${requestScope.idEdicionSeleccionada}" />
                    <c:param name="idTipoRegistro" value="${tipo.id}" />
                </c:url>
                <p><a href="${urlDetalleTipo}"><c:out value="${tipo.nombre}" /></a></p>
            </c:forEach>
            <c:url var="urlVolverEdiciones" value="/consulta-tipo-registro">
                <c:param name="idEvento" value="${requestScope.idEventoSeleccionado}"/>
            </c:url>
            <a class="btn btn-outline-secondary" href="${urlVolverEdiciones}">Volver a ediciones</a>
        </c:if>
        <c:if test="${not empty requestScope.tipoSeleccionado}">
            <h2 class="h5 mb-3">Detalle del tipo de registro</h2>
            <dl>
                <dt>Nombre</dt>
                <dd><c:out value="${requestScope.tipoSeleccionado.nombre}" /></dd>
                <dt>Descripción</dt>
                <dd><c:out value="${requestScope.tipoSeleccionado.descripcion}" /></dd>
                <dt>Costo</dt>
                <dd><c:out value="${requestScope.tipoSeleccionado.costo}" /></dd>
                <dt>Cupo</dt>
                <dd><c:out value="${requestScope.tipoSeleccionado.cupo}" /></dd>
            </dl>
            <c:url var="urlVolverTipos" value="/consulta-tipo-registro">
                <c:param name="idEvento" value="${requestScope.idEventoSeleccionado}" />
                <c:param name="idEdicion" value="${requestScope.idEdicionSeleccionada}" />
            </c:url>
            <a class="btn btn-outline-secondary" href="${urlVolverTipos}">Volver a tipos de registro</a>
        </c:if>
    </div>
</main>
<%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
</body>
</html>
