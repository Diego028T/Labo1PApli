package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;

public class VentanaPrincipal extends JFrame {

    private final ISistema sistema;
    private final JDesktopPane escritorio;

    public VentanaPrincipal(ISistema sistema) {
        this.sistema = sistema;
        this.escritorio = new JDesktopPane();

        setTitle("eventos.uy");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(escritorio);

        mostrarAltaInstitucion();
    }

    private void mostrarAltaInstitucion() {
        AltaInstitucionInternalFrame alta =
                new AltaInstitucionInternalFrame(sistema);

        escritorio.add(alta);
        alta.setVisible(true);
    }
}