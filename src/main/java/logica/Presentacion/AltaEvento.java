package logica.Presentacion;

import logica.Clases.Categoria;
import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaEvento extends JInternalFrame {
    private JPanel PrincipalEvento;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoSiglas;
    private JSpinner campoFecha;
    private JList<Categoria> listaCategorias;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JLabel textoNombre;
    private JLabel textoDescripcion;
    private JLabel textoSigla;
    private JLabel textoFecha;
    private JLabel textoCategoria;
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
        DefaultListModel<Categoria> modelo = new DefaultListModel<>();
        List<Categoria> categorias = sistema.listarNombresCategorias();

        for (Categoria categoria : categorias) {
            modelo.addElement(categoria);
        }

        listaCategorias.setModel(modelo);
        listaCategorias.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );
    }

    private void confirmarAlta() {
        try {
            List<String> nombresCategorias = listaCategorias.getSelectedValuesList()
                    .stream()
                    .map(Categoria::getNombre)
                    .toList();

            sistema.altaEvento(
                    campoNombre.getText().trim(),
                    campoDescripcion.getText().trim(),
                    campoSiglas.getText().trim(),
                    convertirAFecha(campoFecha),
                    nombresCategorias
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
}
