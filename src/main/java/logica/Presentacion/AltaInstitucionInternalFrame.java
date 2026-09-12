package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AltaInstitucionInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtSitioWeb;
    private JButton btnAceptar;
    private JButton btnCancelar;

    public AltaInstitucionInternalFrame(ISistema sistema) {
        super("Alta de institución", true, true, true, true);

        this.sistema = sistema;

        btnAceptar.addActionListener(e -> altaInstitucion());
        btnCancelar.addActionListener(e -> dispose());

        setContentPane(panelPrincipal);

        pack();
        setLocation(100, 80);
    }

    private void altaInstitucion() {
        try {
            sistema.altaInstitucion(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    txtSitioWeb.getText()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Institución dada de alta correctamente."
            );

            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error en el alta",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
