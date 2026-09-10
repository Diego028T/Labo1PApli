package logica.Presentacion;

import logica.sistema01.ISistema;
import logica.DataTypes.DTFecha;
import logica.Clases.Categoria;

import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import java.awt.*;

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

        crearFormulario();
        configurarFecha();
        cargarCategorias();

        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        setContentPane(FormularioEvento);
        pack();
        setLocation(100, 80);
    }

    private void crearFormulario() {
        PrincipalEvento = new JPanel(new BorderLayout(10, 10));
        FormularioEvento = new JPanel(new GridLayout(7, 2, 10, 10));

        campoNombre = new JTextField(20);
        campoDescripcion = new JTextField(20);
        campoSiglas = new JTextField(20);
        campoFecha = new JSpinner();
        listaCategorias = new JList<>();

        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

        textoNombre = new JLabel("Nombre:");
        textoDescripcion = new JLabel("Descripción:");
        textoSigla = new JLabel("Sigla:");
        textoFecha = new JLabel("Fecha de alta:");
        textoCategoria = new JLabel("Categorías:");

        FormularioEvento.add(textoNombre);
        FormularioEvento.add(campoNombre);

        FormularioEvento.add(textoDescripcion);
        FormularioEvento.add(campoDescripcion);

        FormularioEvento.add(textoSigla);
        FormularioEvento.add(campoSiglas);

        FormularioEvento.add(textoFecha);
        FormularioEvento.add(campoFecha);

        FormularioEvento.add(textoCategoria);
        FormularioEvento.add(new JScrollPane(listaCategorias));

        FormularioEvento.add(btnConfirmar);
        FormularioEvento.add(btnCancelar);

        PrincipalEvento.add(FormularioEvento, BorderLayout.CENTER);
    }

    private void configurarFecha() {
        campoFecha.setModel(new SpinnerDateModel());
        campoFecha.setEditor(
                new JSpinner.DateEditor(campoFecha, "dd/MM/yyyy")
        );
    }

    private void cargarCategorias() {
        DefaultListModel<String> modelo = new DefaultListModel<>();

        for (Categoria categoria : sistema.listarNombresCategorias()) {
            modelo.addElement(categoria.getNombre());
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
