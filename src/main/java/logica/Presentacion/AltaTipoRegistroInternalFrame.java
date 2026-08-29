package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.TipoRegistro;

import javax.swing.*;
import java.awt.*;

public class AltaTipoRegistroInternalFrame extends JInternalFrame {
    private JPanel principalPanel;
    private JLabel lblEdicion;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCosto;
    private JPanel panelCupos;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JTextField txtCupo;
    private JTextField txtCantCupos;

    private final Edicion edicion;

    public AltaTipoRegistroInternalFrame(Edicion edicion) {
        super("Alta de tipos de registro", true, true, true, true);
        this.edicion = edicion;
        setContentPane(principalPanel);

        lblEdicion.setText("Tipos de registro para la edicion: " + edicion.getNombre());
        txtCupo = new JTextField();
        txtCantCupos = new JTextField();
        panelCupos.setLayout(new GridLayout(1, 2, 5, 0));
        panelCupos.add(txtCupo);
        panelCupos.add(txtCantCupos);

        btnGuardar.addActionListener(e -> guardarTipoRegistro());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        pack();
        setLocation(140, 100);
    }

    private void guardarTipoRegistro() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        try {
            float costo = Float.parseFloat(txtCosto.getText().trim());
            int cupo = Integer.parseInt(txtCupo.getText().trim());
            int cantCupos = Integer.parseInt(txtCantCupos.getText().trim());

            if (nombre.isBlank() || descripcion.isBlank() || costo < 0 || cupo <= 0 || cantCupos < 0) {
                throw new IllegalArgumentException();
            }

            edicion.agregarTipoRegistro(new TipoRegistro(nombre, descripcion, costo, cupo, cantCupos));
            JOptionPane.showMessageDialog(this, "Tipo de registro guardado. Puede cargar otro.");
            limpiarCampos();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Complete los campos con datos validos. El costo no puede ser negativo y el cupo debe ser mayor que cero.",
                    "Datos invalidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCosto.setText("");
        txtCupo.setText("");
        txtCantCupos.setText("");
        txtNombre.requestFocusInWindow();
    }
}
