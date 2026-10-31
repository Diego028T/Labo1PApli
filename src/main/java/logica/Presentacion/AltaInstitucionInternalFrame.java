package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AltaInstitucionInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtSitioWeb;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JButton btnImagen;
    private JLabel lblImagen;
    private byte[] imagenBytes;
    private String tipoImagen;

    public AltaInstitucionInternalFrame(ISistema sistema) {
        super("Alta de institución", true, true, true, true);

        this.sistema = sistema;

        btnAceptar.addActionListener(e -> altaInstitucion());
        btnCancelar.addActionListener(e -> dispose());
        btnImagen.addActionListener(e -> elegirImagen());

        setContentPane(panelPrincipal);

        pack();
        setLocation(100, 80);
    }

    private void altaInstitucion() {
        try {
            sistema.altaInstitucion(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    txtSitioWeb.getText(),
                    imagenBytes,
                    tipoImagen
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

    private void elegirImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(
                new FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "gif"));
        if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path archivo = selector.getSelectedFile().toPath();
        try {
            imagenBytes = Files.readAllBytes(archivo);
            tipoImagen = Files.probeContentType(archivo);
            lblImagen.setText(archivo.getFileName().toString());
        } catch (IOException e) {
            imagenBytes = null;
            tipoImagen = null;
            lblImagen.setText("Sin imagen (opcional)");
            JOptionPane.showMessageDialog(this, "No se pudo leer la imagen.",
                    "Alta de institución", JOptionPane.ERROR_MESSAGE);
        }
    }

}
