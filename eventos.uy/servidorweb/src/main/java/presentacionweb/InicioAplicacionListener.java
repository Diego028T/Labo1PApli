package presentacionweb;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import logica.Persistencia.JPAUtil;
import logica.sistema01.Fabrica;
import logica.sistema01.ISistema;

@WebListener
public class InicioAplicacionListener implements ServletContextListener {

    private boolean persistenciaPreparada;

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ServletContext contexto = evento.getServletContext();

        try {
            JPAUtil.getEntityManagerFactory();
            persistenciaPreparada = true;

            ISistema sistema = Fabrica.getInstancia().getISistema();
            contexto.setAttribute("sistema", sistema);

            contexto.log("eventos.uy: logica inicializada");
        } catch (RuntimeException | Error error) {
            if (persistenciaPreparada) {
                JPAUtil.cerrar();
                persistenciaPreparada = false;
            }
            contexto.log("No se pudo iniciar la logica", error);
            throw error;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        evento.getServletContext().removeAttribute("sistema");

        if (persistenciaPreparada) {
            JPAUtil.cerrar();
            persistenciaPreparada = false;
        }
    }
}