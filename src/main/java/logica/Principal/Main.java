package logica.Principal;

import logica.Persistencia.JPAUtil;
import logica.sistema01.Fabrica;
import logica.sistema01.ISistema;
import logica.Presentacion.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(JPAUtil::cerrar));

        ISistema sistema = Fabrica.getInstancia().getISistema();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setVisible(true);
        });
    }
}
