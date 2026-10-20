<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Nuevo evento | eventos.uy</title>
        <link rel="stylesheet" href="../bootstrap/bootstrap.min.css">
        <link rel="stylesheet" href="../estilos/estilos.css">
    </head>
    <body class="disposicionPagina">
       <%@ include file="/WEB-INF/vistas/includes/encabezado.jsp" %>
            <main class="contenidoPrincipal">
                <div class="container">
                    <h1 class="tituloPagina">Nuevo evento</h1>
                    <section class="disposicionFormulario">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">${error}</div>
                        </c:if>
                        <form class="tarjetaFormulario" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/eventos/nuevo">
                            <div class="campoGrupo">
                                <label class="form-label" for="campoNombreEvento">Nombre</label>
                                <input class="form-control" id="campoNombreEvento" name="nombre" type="text" value="${param.nombre}">
                            </div>
                            <div class="campoGrupo">
                                <label class="form-label" for="campoDescripcionEvento">Descripción</label>
                                <textarea class="form-control" id="campoDescripcionEvento" name="descripcion" rows="4">${param.descripcion}</textarea>
                            </div>
                            <div class="campoGrupo">
                                <label class="form-label" for="campoSiglaEvento">Sigla</label>
                                <input class="form-control" id="campoSiglaEvento" name="sigla" type="text" maxlength="10" value="${param.sigla}">
                            </div>
                            <div class="campoGrupo">
                                <label class="form-label" for="campoCategoriasEvento">Categorías</label>
                                <select class="form-select" id="campoCategoriasEvento" name="categorias" multiple size="5">
                                    <c:forEach items="${categorias}" var="cat">
                                        <c:set var="marcada" value="false" />
                                        <c:forEach items="${seleccionadas}" var="s">
                                            <c:if test="${s == cat}"><c:set var="marcada" value="true" /></c:if>
                                        </c:forEach>
                                        <option value="${cat}" ${marcada ? 'selected' : ''}>${cat}</option>
                                    </c:forEach>
                                </select>
                                <p class="textoAyuda">Mantené Ctrl para elegir varias.</p>
                            </div>
                            <div class="campoGrupo">
                                <label class="form-label" for="campoImagenEvento">Imagen (opcional)</label>
                                <input class="form-control" id="campoImagenEvento" name="imagen" type="file" accept="image/*">
                            </div>
                            <div class="bloqueAcciones">
                                <button class="btn botonPrincipal" type="submit">Crear evento</button>
                                <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/eventos">Cancelar</a>
                            </div>
                        </form>
                    </section>
                </div>
            </main>
        <%@ include file="/WEB-INF/vistas/includes/pie.jsp" %>
    </body>
</html>
