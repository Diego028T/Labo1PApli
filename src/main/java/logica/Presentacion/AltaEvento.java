package logica.Presentacion;

import logica.sistema01.ISistema;
import logica.DataTypes.DTFecha;

import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

public class AltaEvento extends JInternalFrame {
    private JPanel PrincipalEvento;
    private JPanel FormularioEvento;
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
    private ISistema sistema;

    public AltaEvento(ISistema sistema) {
        super("Alta Evento", true, true, true, true);

        this.sistema = sistema;

        configurarFecha();
        cargarCategorias();

        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        setContentPane(FormularioEvento);
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

        for (String categoria : sistema.listarNombresCategorias()) {
            modelo.addElement(categoria);
        }

        listaCategorias.setModel(modelo);
        listaCategorias.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
    }

    private void confirmarAlta() {
        try {
            sistema.altaEvento(
                    campoNombre.getText().trim(),
                    campoDescripcion.getText().trim(),
                    campoSiglas.getText().trim(),
                    convertirAFecha(campoFecha),
                    listaCategorias.getSelectedValuesList()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Evento dado de alta correctamente."
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


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
