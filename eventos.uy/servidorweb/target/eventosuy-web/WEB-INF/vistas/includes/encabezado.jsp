<%@ page pageEncoding="UTF-8" %>
<c:set var="raiz" value="${pageContext.request.contextPath}" />
<header class="encabezadoSitio">
    <div class="container">
        <nav class="barraNavegacion" id="barraNavegacion" aria-label="Navegación principal">
            <a class="marcaSitio" href="${raiz}/">eventos.uy</a>
            <input class="interruptorMenu" id="interruptorMenu" type="checkbox">
            <label class="etiquetaInterruptor" for="interruptorMenu">Menú</label>
            <ul class="listaMenu">
                <li class="itemMenu">
                    <a class="enlaceMenu" href="${raiz}/">Inicio</a>
                </li>
                <li class="itemMenu itemConSubmenu">
                    <span class="enlaceMenu" tabindex="0">Eventos</span>
                    <ul class="listaSubmenu">
                        <li><a class="enlaceMenu" href="${raiz}/eventos">Listado de eventos</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/eventos/nuevo">Nuevo evento</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/ediciones/nueva">Nueva edición</a></li>
                    </ul>
                </li>
                <li class="itemMenu itemConSubmenu">
                    <span class="enlaceMenu" tabindex="0">Ediciones</span>
                    <ul class="listaSubmenu">
                        <li><a class="enlaceMenu" href="${raiz}/registros/nuevo">Registro a edición</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/tipos-registro/nuevo">Nuevo tipo de registro</a></li>
                    </ul>
                </li>
                <li class="itemMenu itemConSubmenu">
                    <span class="enlaceMenu" tabindex="0">Instituciones</span>
                    <ul class="listaSubmenu">
                        <li><a class="enlaceMenu" href="${raiz}/instituciones/nueva">Nueva institución</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/patrocinios/nuevo">Nuevo patrocinio</a></li>
                    </ul>
                </li>
                <li class="itemMenu itemConSubmenu">
                    <span class="enlaceMenu" tabindex="0">Usuarios</span>
                    <ul class="listaSubmenu">
                        <li><a class="enlaceMenu" href="${raiz}/usuarios">Listado de usuarios</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/perfil">Perfil</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/perfil/editar">Editar perfil</a></li>
                    </ul>
                </li>
                <li class="itemMenu itemConSubmenu">
                    <span class="enlaceMenu" tabindex="0">Cuenta</span>
                    <ul class="listaSubmenu">
                        <li><a class="enlaceMenu" href="${raiz}/login">Iniciar sesión</a></li>
                        <li><a class="enlaceMenu" href="${raiz}/registro">Registrarse</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
</header>