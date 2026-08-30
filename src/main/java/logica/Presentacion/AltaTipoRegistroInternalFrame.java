package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.List;

public class AltaTipoRegistroInternalFrame extends JInternalFrame {
    private JPanel principalPanel;
    private JLabel lblEdicion;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCosto;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JTextField txtCupo;
    private final ISistema sistema;
    private Edicion edicionSeleccionada;

    public AltaTipoRegistroInternalFrame(ISistema sistema) {
        super("Alta de tipos de registro", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null");
        }

        this.sistema = sistema;
        setContentPane(principalPanel);

        edicionSeleccionada = seleccionarEdicion();
        if (edicionSeleccionada == null) {
            // El llamador agrega y hace visible el InternalFrame después
            // de construirlo; por eso se cierra en el siguiente evento.
            SwingUtilities.invokeLater(this::dispose);
            return;
        }

        lblEdicion.setText(
                "Tipos de registro para la edición: "
                        + edicionSeleccionada.getNombre());

        btnGuardar.addActionListener(e -> guardarTipoRegistro());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        pack();
        setLocation(140, 100);
    }

    private Edicion seleccionarEdicion() {
        List<Evento> eventos = sistema.listarEventos();
        if (eventos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay eventos registrados.",
                    "Alta de tipo de registro",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return null;
        }

        Evento evento = (Evento) JOptionPane.showInputDialog(
                this,
                "Seleccione un evento:",
                "Evento",
                JOptionPane.QUESTION_MESSAGE,
                null,
                eventos.toArray(),
                eventos.get(0)
        );

        if (evento == null) {
            return null;
        }

        List<Edicion> ediciones = sistema.listarEdiciones(evento);
        if (ediciones.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El evento seleccionado no tiene ediciones.",
                    "Alta de tipo de registro",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return null;
        }

        return (Edicion) JOptionPane.showInputDialog(
                this,
                "Seleccione una edición:",
                "Edición",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ediciones.toArray(),
                ediciones.get(0)
        );
    }



    private void guardarTipoRegistro() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        try {
            float costo = Float.parseFloat(txtCosto.getText().trim());
            int cupo = Integer.parseInt(txtCupo.getText().trim());

            if (nombre.isBlank() || descripcion.isBlank() || costo < 0 || cupo <= 0) {
                throw new IllegalArgumentException();
            }

            sistema.altaTipoRegistro(
                    edicionSeleccionada,
                    nombre,
                    descripcion,
                    costo,
                    cupo
            );

            JOptionPane.showMessageDialog(this, "Tipo de registro guardado. Puede cargar otro.");
            limpiarCampos();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage() == null
                            ? "Complete los campos con datos válidos."
                            : ex.getMessage(),
                    "Datos invalidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCosto.setText("");
        txtCupo.setText("");
        txtNombre.requestFocusInWindow();
    }
}
