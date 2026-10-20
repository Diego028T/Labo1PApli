<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Alta de tipo de registro | eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/estilos.css">
</head>
<body class="disposicionPagina">
<%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
<main class="contenidoPrincipal">
    <div class="container">
        <h1 class="tituloPagina">Alta de tipo de registro</h1>
        <c:url var="urlAltaTipo" value="/alta-tipo-registro" />

        <c:if test="${not empty requestScope.mensajeExito}">
            <div class="alert alert-success" role="status">
                <c:out value="${requestScope.mensajeExito}" />
            </div>
        </c:if>
        <c:if test="${not empty requestScope.error}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${requestScope.error}" />
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty requestScope.edicionSeleccionada}">
                <h2 class="h5 mb-3">Mis ediciones</h2>
                <c:choose>
                    <c:when test="${empty requestScope.ediciones}">
                        <p>No tenes ediciones disponibles para crear tipos de registro.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="list-group">
                            <c:forEach items="${requestScope.ediciones}" var="edicion">
                                <c:url var="urlFormulario" value="/alta-tipo-registro">
                                    <c:param name="idEdicion" value="${edicion.id}" />
                                </c:url>
                                <li class="list-group-item">
                                    <a href="${urlFormulario}"><c:out value="${edicion.nombre}" /></a>
                                    <span class="textoAyuda">(<c:out value="${edicion.sigla}" />)</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <section class="disposicionFormulario">
                    <h2 class="h5 mb-3"><c:out value="${requestScope.edicionSeleccionada.nombre}" /></h2>
                    <form id="formAltaTipo" class="tarjetaFormulario"
                          action="${urlAltaTipo}" method="post" novalidate>
                        <input type="hidden" name="idEdicion"
                               value="${fn:escapeXml(requestScope.edicionSeleccionada.id)}">
                        <input type="hidden" name="csrf" value="${fn:escapeXml(requestScope.csrf)}">

                        <div class="campoGrupo">
                            <label class="form-label" for="campoNombreTipo">Nombre</label>
                            <input class="form-control" id="campoNombreTipo" name="nombre"
                                   type="text" required maxlength="100"
                                   value="${fn:escapeXml(requestScope.nombre)}">
                        </div>
                        <div class="campoGrupo">
                            <label class="form-label" for="campoDescripcionTipo">Descripcion</label>
                            <textarea class="form-control" id="campoDescripcionTipo" name="descripcion"
                                      rows="3" required maxlength="255"><c:out value="${requestScope.descripcion}" /></textarea>
                        </div>
                        <div class="campoGrupo">
                            <label class="form-label" for="campoCostoTipo">Costo</label>
                            <input class="form-control" id="campoCostoTipo" name="costo"
                                   type="number" required min="0" step="0.01"
                                   value="${fn:escapeXml(requestScope.costo)}">
                        </div>
                        <div class="campoGrupo">
                            <label class="form-label" for="campoCupoTipo">Cupo</label>
                            <input class="form-control" id="campoCupoTipo" name="cupo"
                                   type="number" required min="1" max="2147483647" step="1"
                                   value="${fn:escapeXml(requestScope.cupo)}">
                        </div>
                        <div class="bloqueAcciones">
                            <button class="btn botonPrincipal" type="submit">Crear tipo</button>
                            <a class="btn btn-outline-secondary" href="${urlAltaTipo}">Cancelar</a>
                        </div>
                    </form>
                </section>
            </c:otherwise>
        </c:choose>
    </div>
</main>
<%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
<script>
    const formulario = document.getElementById("formAltaTipo");
    if (formulario) {
        formulario.addEventListener("submit", (evento) => {
            const nombre = formulario.elements.namedItem("nombre");
            const descripcion = formulario.elements.namedItem("descripcion");
            const costo = formulario.elements.namedItem("costo");
            const cupo = formulario.elements.namedItem("cupo");

            nombre.setCustomValidity(nombre.value.trim() ? "" : "Ingresa un nombre.");
            descripcion.setCustomValidity(descripcion.value.trim() ? "" : "Ingresa una descripcion.");
            costo.setCustomValidity(
                Number.isFinite(costo.valueAsNumber) && costo.valueAsNumber >= 0
                    ? "" : "Ingresa un costo valido mayor o igual a cero."
            );
            cupo.setCustomValidity(
                Number.isInteger(cupo.valueAsNumber) && cupo.valueAsNumber > 0
                    ? "" : "Ingresa un cupo entero mayor que cero."
            );

            if (!formulario.checkValidity()) {
                evento.preventDefault();
                formulario.reportValidity();
            }
        });
    }
</script>
</body>
</html>