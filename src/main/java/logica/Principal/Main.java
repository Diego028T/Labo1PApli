package logica.Principal;

import logica.sistema01.ISistema;
import logica.sistema01.Sistema;
import logica.Presentacion.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        ISistema sistema = Sistema.getInstancia();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setVisible(true);
        });
    }
}