package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class AltaInstitucionInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final JTextField txtNombre;
    private final JTextArea txtDescripcion;
    private final JTextField txtSitioWeb;

    public AltaInstitucionInternalFrame(ISistema sistema) {
        super("Alta de institución", true, true, true, true);

        this.sistema = sistema;

        txtNombre = new JTextField();
        txtDescripcion = new JTextArea(4, 20);
        txtSitioWeb = new JTextField();

        construirInterfaz();

        pack();
        setLocation(100, 80);
    }

    private void construirInterfaz() {
        JPanel datos = new JPanel(new GridLayout(3, 2, 10, 10));

        datos.add(new JLabel("Nombre:"));
        datos.add(txtNombre);

        datos.add(new JLabel("Descripción:"));
        datos.add(new JScrollPane(txtDescripcion));

        datos.add(new JLabel("Sitio web:"));
        datos.add(txtSitioWeb);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> altaInstitucion());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAceptar);
        botones.add(btnCancelar);

        add(datos, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
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
