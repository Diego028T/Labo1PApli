package logica.Presentacion;

import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaEvento extends JInternalFrame {
    private JPanel PrincipalEvento;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoSiglas;
    private JSpinner campoFecha;
    private JList<String> listaCategorias;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JLabel textoNombre;
    private JLabel textoDescripcion;
    private JLabel textoSigla;
    private JLabel textoFecha;
    private JLabel textoCategoria;
    private JButton btnImagen;
    private JLabel lblImagen;
    private byte[] imagenBytes;
    private String tipoImagen;
    private final ISistema sistema;

    public AltaEvento(ISistema sistema) {
        super("Alta Evento", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;

        configurarFecha();
        cargarCategorias();

        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());
        btnImagen.addActionListener(e -> elegirImagen());

        setContentPane(PrincipalEvento);
        pack();
        setLocation(100, 80);
    }

    private void configurarFecha() {
        campoFecha.setModel(new SpinnerDateModel());
        campoFecha.setEditor(
                new JSpinner.DateEditor(campoFecha, "dd/MM/yyyy")
        );
    }

    private void cargarCategorias() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        List<String> categorias = sistema.listarNombresCategorias();

        for (String categoria : categorias) {
            modelo.addElement(categoria);
        }

        listaCategorias.setModel(modelo);
        listaCategorias.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
    }

    private void confirmarAlta() {
        try {
            List<String> nombresCategorias = listaCategorias.getSelectedValuesList();

            sistema.altaEvento(
                    campoNombre.getText().trim(),
                    campoDescripcion.getText().trim(),
                    campoSiglas.getText().trim(),
                    convertirAFecha(campoFecha),
                    nombresCategorias,
                    imagenBytes,
                    tipoImagen
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Evento dado de alta correctamente.",
                    "Alta de Evento",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error en el alta de evento",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private DTFecha convertirAFecha(JSpinner selector) {
        Date fechaSeleccionada = (Date) selector.getValue();

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaSeleccionada);

        return new DTFecha(
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH) + 1,
                calendario.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void elegirImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "gif"));

        if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path archivo = selector.getSelectedFile().toPath();
        try {
            imagenBytes = Files.readAllBytes(archivo);
            tipoImagen = Files.probeContentType(archivo);
            lblImagen.setText(archivo.getFileName().toString());
        } catch (IOException ex) {
            imagenBytes = null;
            tipoImagen = null;
            lblImagen.setText("Sin imagen (opcional)");
            JOptionPane.showMessageDialog(this, "No se pudo leer la imagen.",
                    "Alta de Evento", JOptionPane.ERROR_MESSAGE);
        }
    }
}